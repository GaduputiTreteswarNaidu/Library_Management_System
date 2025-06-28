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

import com.lms.entity.User;
import com.lms.service.UserService;
@Controller
public class UserController {
	@Autowired
	private UserService userService;
	@RequestMapping("/lms/AddUser")
    public String showAddUserForm() {
        return "AddUser";
    }
	@RequestMapping("/insertUsers")
	public String adduser(@ModelAttribute User user) {
        userService.add(user);
        System.out.println("Inserted");
        return "redirect:/lms/AddUser?success=true";
    }
    @RequestMapping("/lms/ShowUsers")
    public String showAllUsers(Model model) {
    	model.addAttribute("users", userService.findAll());
    	return "ShowUsers";
    }
    @RequestMapping("/lms/DeleteUser")
    public String deleteStudent(Model model) {
    	model.addAttribute("users", userService.findAll());
        return "DeleteUser";
    }
    @RequestMapping("/lms/DeleteUser/{userId}")
    public String deleteStudent(@PathVariable int userId) {
       userService.deleteById(userId);
        return "redirect:/lms/DeleteUser";
    }
    @GetMapping("lms/UpdateUser")
    public String upatesUser(Model model) {
    	model.addAttribute("users",userService.findAll());

    	return "UpdateUser";
    }
    @PostMapping("/lms/UpdateUser")
    public String updateUserPassword(@RequestParam("userId") Integer userId, @RequestParam("password") String password, Model model) {
    	userService.updatePasswordById(userId, password);
   		return "redirect:/lms/UpdateUser";
    	
}
}
