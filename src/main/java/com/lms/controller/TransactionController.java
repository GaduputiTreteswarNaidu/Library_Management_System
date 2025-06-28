package com.lms.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lms.entity.Book;
import com.lms.entity.Transaction;
import com.lms.entity.User;
import com.lms.repository.AdminRepository;
import com.lms.repository.BookRepository;
import com.lms.repository.TransactionRepository;
import com.lms.repository.UserRepository;
import com.lms.service.MailService;
import com.lms.service.TransactionService;


@Controller
public class TransactionController {

    private final AdminRepository adminRepository;
	@Autowired
	private TransactionService tService;
	@Autowired
	private TransactionRepository tRepo;
	@Autowired
	private BookRepository bRepo;
	@Autowired
	private MailService mailService;

	@Autowired
	private UserRepository userRepository;


    TransactionController(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
	@RequestMapping("/lms/borrowed")
	public String showBorrowedRecords(Model model) {
	    List<Transaction> transactions = tService.findAll();
	    LocalDate today = LocalDate.now();

	    for (Transaction t : transactions) {
	        if (t.getDueDate() != null && today.isAfter(t.getDueDate()) ) {
	            t.setFine(100);
	            t.setStatus("Due"); // Change status to Due
	            tService.save(t);
	                // Notify user
	                User user = userRepository.findById(Integer.parseInt(t.getUserId())).orElse(null);
	                if (user != null && user.getEmail() != null) {
	                    String subject = "Late Return Notice";
	                    String body = "Hello " + user.getName() + ",\n\n" +
	                                  "The book with ID " + t.getBookid() + " is overdue.\n" +
	                                  "A fine of ₹100 has been applied. Please return the book or pay the fine.\n\n" +
	                                  "Regards,\nLibrary Admin";
	                    mailService.sendMail(user.getEmail(), subject, body);
	                }
	            

	        } else  {
	            // Only reset fine to 0 for active borrowings
	            t.setFine(0);
	            tService.save(t);
	        }
	    }

	    model.addAttribute("transactions", transactions);
	    return "BorrowedRecords";
	}

	
	@RequestMapping("/lms/BorrowBook")
    public String showBorrowBookForm() {
        return "BorrowBook";
    }
	@RequestMapping("/BorrowBook")
	public String borrowBook(@ModelAttribute Transaction transaction, Model model) {
	    Optional<Book> bookOpt = bRepo.findById(transaction.getBookid());

	    if (bookOpt.isPresent()) {
	        Book book = bookOpt.get();

	        if (book.getAvailable_count() > 0) {
	            // Reduce available count
	            book.setAvailable_count(book.getAvailable_count() - 1);
	            bRepo.save(book);

	            // Save transaction
	            tRepo.save(transaction);

	            model.addAttribute("message", "Book borrowed successfully!");
	            User user = userRepository.findById(Integer.parseInt(transaction.getUserId())).orElse(null);
	            if (user != null && user.getEmail() != null) {
	                String subject = "Book Borrowed Confirmation";
	                String body = "Hello " + user.getName() + ",\n\n" +
	                              "You have borrowed Book ID: " + transaction.getBookid() + "\n" +
	                              "Due Date: " + transaction.getDueDate() + "\n\n" +
	                              "Regards,\nPresidency University Library";
	                mailService.sendMail(user.getEmail(), subject, body);
	            }
	        } else {
	            model.addAttribute("message", "Book not available!");
	        }
	    } else {
	         model.addAttribute("message", "Book ID not found!");
	    }

	    return "BorrowBook";
	}
	 @RequestMapping("/lms/CollectBook")
	    public String showCollectBookForm() {
	        return "CollectBook";
	    }
	 @RequestMapping("/lms/CollectBook/Details")
	    public String getTransactionDetails(@RequestParam("transactionId") Integer transactionId,
	                                        Model model) {
	        Transaction transaction = tService.getTransactionById(transactionId);
	        if (transaction != null) {
	            model.addAttribute("t", transaction);
	        } else {
	            model.addAttribute("error", "Transaction not found");
	        }
	        return "CollectBook"; 
	    }
	 @RequestMapping("/lms/CollectBook/Update")
	 public String updateTransaction(
	         @RequestParam("transactionId") Integer transactionId,
	         @RequestParam("fine") Integer finePaid,
	         Model model) {

	     boolean updated = tService.updateTransaction(transactionId, finePaid);

	     if (updated) {
	         Transaction transaction = tService.getTransactionById(transactionId);
	         
	         if (transaction != null) {
	             Optional<User> userOpt = userRepository.findById(Integer.parseInt(transaction.getUserId()));
	             if (userOpt.isPresent()) {
	                 User user = userOpt.get();
	                 if (user.getEmail() != null && !user.getEmail().isEmpty()) {
	                     String subject = "Book Return Confirmation";
	                     String body = "Hello " + user.getName() + ",\n\n"
	                             + "You have successfully returned the book with ID: " + transaction.getBookid() + ".\n"
	                             + (finePaid > 0 ? "Fine Paid: ₹" + finePaid + "\n" : "")
	                             + "Thank you.\n\nPresidency University Library";
	                     mailService.sendMail(user.getEmail(), subject, body);
	                 }
	             }
	         }
	     }

	     return "CollectBook";
	 }

	 @RequestMapping("/viewTransaction")
	    public String getTransactionsByUserId(Model model) {
		 model.addAttribute("transactions", tService.findByUserId(String.valueOf(getUid())));
		 return "ViewTransaction";
	    }
	    private Integer uid;
	    
	    public Integer getUid() {
			return uid;
		}
		public void setUid(Integer uid) {
			this.uid = uid;
		}
		@RequestMapping("/lms/LateReturn")
	    public String showLateReturnPage() {
	    	return "LateReturn";
	    }


		
	    
}
