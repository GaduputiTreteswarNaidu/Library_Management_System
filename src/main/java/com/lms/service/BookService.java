package com.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.entity.Book;
import com.lms.repository.BookRepository;
import com.lms.repository.TransactionRepository;
import com.lms.repository.UserRepository;

import jakarta.transaction.Transactional;
@Service
public class BookService {
	@Autowired
	private BookRepository bookRepo;
	@Autowired
	private TransactionRepository tRepo;
	@Autowired
	private UserRepository userRepo;

	public void add(Book book) {
		bookRepo.save(book);
	}
	public void deleteById(int id) {
		bookRepo.deleteById(id);
	}
	@Transactional
    public boolean updatePasswordById(Integer userId, Integer available_count) {
        return bookRepo.findById(userId).map(user -> {
            user.setAvailable_count(available_count);  // Consider encoding the password
            bookRepo.save(user);
            return true;
        }).orElse(false);
    }

	public List<Book> findAll() {
		return bookRepo.findAll();
	}
	public long getTotalBorrowedBooks() {
        return tRepo.count();
    }

    public long getAvailableBooks() {
        return bookRepo.findAll().stream()
                .mapToInt(book -> book.getAvailable_count() != null ? book.getAvailable_count() : 0)
                .sum();
    }

    public long getActiveUsers() {
        return userRepo.count();
    }
    public long getReturnedBooks() {
        return tRepo.countByStatus("Returned");
    }
}
