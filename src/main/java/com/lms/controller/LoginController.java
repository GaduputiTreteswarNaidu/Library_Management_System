package com.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lms.entity.User;
import com.lms.entity.Admin;
import com.lms.repository.AdminRepository;
import com.lms.repository.UserRepository;

import ch.qos.logback.core.model.Model;

@Controller
public class LoginController {
	@Autowired
	private UserRepository userRepo;
	@Autowired 
	private AdminRepository adminRepo;
	@Autowired 
	private TransactionController tController;
	@GetMapping("/login")
	public String login() {
		return "Login";
	}
	@RequestMapping("/lms/UserLogin")
	public String userLogin() {
		return "UserLogin";
	}
	@RequestMapping("/lms/AdminLogin")
	public String adminLogin() {
		return "AdminLogin";
	}
	@RequestMapping("/home") 
	public String home() {
		return "homes";
	}
	
	@RequestMapping("/userVerify")
	public String verifyUser(@RequestParam("userId") Integer userId,
	                         @RequestParam("password") String password,
	                         jakarta.servlet.http.HttpSession session,
	                         org.springframework.ui.Model model) {
	    tController.setUid(userId);
	    User user = userRepo.findById(userId).orElse(null);

	    if (user != null && user.getPassword().equals(password)) {
	        // ✅ Save user ID in session
	        session.setAttribute("userId", userId);
	        return "redirect:/homes";
	    } else {
	        model.addAttribute("error", "Invalid user ID or password");
	        return "ErrorLogin";
	    }
	}

	 @RequestMapping("/adminVerify")
	    public String verifyAdmin(@RequestParam("id") String id,
	                             @RequestParam("password") String password,
	                             Model model) {

	        Admin admin = adminRepo.findById(id).orElse(null);

	        if (admin != null && admin.getPassword().equals(password)) {
	            return "redirect:/admin";  
	        } else {
	            return "ErrorLogin"; 
	        }
	    }
}
