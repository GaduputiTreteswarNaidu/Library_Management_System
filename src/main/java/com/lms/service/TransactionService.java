package com.lms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lms.entity.Transaction;
import com.lms.repository.TransactionRepository;

@Service
public class TransactionService {
	@Autowired
	private TransactionRepository tRepo;
	public void save(Transaction t) {
		// TODO Auto-generated method stub
		tRepo.save(t);
	}
	public List<Transaction> findAll() {
		return tRepo.findAll();
	}
	public Transaction getTransactionById(Integer transactionId) {
        return tRepo.findById(transactionId).orElse(null);
    }
	 public boolean updateTransaction(Integer transactionId, Integer finePaid) {
	        Optional<Transaction> optionalTransaction = tRepo.findById(transactionId);

	        if (optionalTransaction.isPresent()) {
	            Transaction transaction = optionalTransaction.get();
	            transaction.setStatus("Returned");
	            int currentFine = transaction.getFine() != null ? transaction.getFine() : 0;
	            int remainingFine = currentFine - (finePaid != null ? finePaid : 0);
	            transaction.setFine(Math.max(remainingFine, 0));
	            tRepo.save(transaction);
	            return true;
	        }
	        return false;
	    }
	 public List<Transaction> findByUserId(String userId) {
	        return tRepo.findByUserId(userId);
	    }
	   public Optional<Transaction> getTransactionDetails(Integer transactionId) {
	        return tRepo.findById(transactionId);
	    }
	
}
