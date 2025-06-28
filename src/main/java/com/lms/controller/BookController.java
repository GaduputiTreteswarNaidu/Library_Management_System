package com.lms.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lms.entity.Book;
import com.lms.entity.User;
import com.lms.service.BookService;
import com.lms.service.TransactionService;
import com.lms.service.UserService;
@Controller
public class BookController {
	@Autowired
	private BookService bookService;
	@RequestMapping("/admin")
	public String show(Model model) {
		model.addAttribute("totalBorrowed", bookService.getTotalBorrowedBooks());
        model.addAttribute("availableBooks", bookService.getAvailableBooks());
        model.addAttribute("activeUsers", bookService.getActiveUsers());
        model.addAttribute("returnedBooks", bookService.getReturnedBooks());
        return "AdminDashboard";
		}
	    @RequestMapping("/lms/AvailableBooks")
	    public String showAvailableBooks(Model model) {
	    	model.addAttribute("books", bookService.findAll());
	        return "AvailableBooks";
	    }
	    @RequestMapping("/lms/AddBook")
	    public String showAddBookForm() {
	        return "AddBook";
	    }
	    @RequestMapping("/insertBook")
	    public String addbook(@ModelAttribute Book book) {
        bookService.add(book);
        System.out.println("Inserted");
        return "redirect:/lms/AddBook?success=true";
    }
	    @RequestMapping("/lms/DeleteBook")
	    public String deleteStudent(Model model) {
	    	model.addAttribute("books", bookService.findAll());
	        return "DeleteBook";
	    }
	    @RequestMapping("/lms/DeleteBook/{id}")
	    public String deleteStudent(@PathVariable int id) {
	       bookService.deleteById(id);
	        return "redirect:/lms/DeleteBook";
	    }
	    @GetMapping("/lms/UpdateBookCount")
	    public String upatesUser(Model model) {
	    	model.addAttribute("books",bookService.findAll());
	    	System.out.println("Post");
	    	return "UpdateBookCount";
	    }
	    @PostMapping("/lms/UpdateBookCount")
	    public String updateUserPassword(@RequestParam("userId") Integer userId, @RequestParam("available_count") Integer available_count, Model model) {
	    	System.out.println("Changing count");
	    	bookService.updatePasswordById(userId, available_count);
	    	return "redirect:/lms/UpdateBookCount";
	    	
	}
	    @RequestMapping("/book")
	    public String book() {
	    	return "HomeBookDisplay";
	    }
	    @RequestMapping("/PDFDisp")
	    public String pdfdisp() {
	    	return "BookPDFDisplay";
	    }
	    @RequestMapping("/search")
        public String searchRedirect(@RequestParam("query") String query) {
            query = query.trim().toLowerCase();
            
            switch (query) {
                case "biology":
                    return "BookDisplayBiology";
                case "history":
                    return "BookDisplayHistory";
                case "business":
                    return "BookDisplayBusiness";
                case "technology":
                    return "BookDisplayTechnology";
                case "environment":
                    return "BookDisplayEnvironment";
                case "economics":
                    return "BookDisplayEconomics";
                case "time management":
                    return "BookDisplayTimeManagement";
                default:
                    return "Home"; // Optional fallback page
            }
        }
	    @GetMapping("/booksearch")
	    public String showBookDetails(@RequestParam String title, @RequestParam String image, 
	                                  @RequestParam String author, @RequestParam String pdf,@RequestParam String bookid) {
	        // Handle the book details
	        return "HomeBookDisplay"; // Return the view to display book details
	    }
	    @GetMapping("/bookpdfdisplay")
	    public String BookDetailsPdf(@RequestParam String pdf) {
	        // Handle the book details
	        return "BookPDFDisplay"; // Return the view to display book details
	    }
	    
	}

