package com.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.entity.User;
import com.lms.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	public void add(User user) {
		userRepo.save(user);
	}
	public List<User> findAll() {
        return userRepo.findAll();
    }
	public void deleteById(int userId) {
		userRepo.deleteById(userId);
	}
	@Transactional
    public boolean updatePasswordById(Integer userId, String newPassword) {
        return userRepo.findById(userId).map(user -> {
            user.setPassword(newPassword);  // Consider encoding the password
            userRepo.save(user);
            return true;
        }).orElse(false);
    }
}
