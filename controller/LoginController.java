package com.demo.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.demo.authentication.entity.LoginRequest;
import com.demo.authentication.responseHandler.ResponseHandler;
import com.demo.authentication.service.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
	
	@Autowired
	UserDetailsServiceImpl serviceRepo;

	@GetMapping("/login")
	public String login(@RequestParam(value = "error-message",required = false)boolean error, Model model) {
		if(error) {
			model.addAttribute("errorMessage", "Invalid Username or Password");
		}
		return "login";
	}
	
	@GetMapping("/demo")
	public String demo(HttpServletRequest request,Model model) {
		String sessionID = (String) request.getSession().getAttribute("SessionID");
		model.addAttribute("sessionID", sessionID);
		return "demo"; 
	}
	
}
