package com.demo.authentication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.authentication.responseHandler.ResponseHandler;

@RestController
public class RoleController {
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin/test")
	public ResponseEntity<Object> adminApi(){
		return ResponseHandler.generateResponse("Welcome admin!!", HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/user/test")
	public ResponseEntity<Object> userApi(){
		System.out.println("request !!!!!!!!!!!!!!!!!!!!!");
		return ResponseHandler.generateResponse("Welcome user!!", HttpStatus.OK); 
	}
	
	@GetMapping("/api/all/test")
	public ResponseEntity<Object> all(){
		return ResponseHandler.generateResponse("Welcome !!!", HttpStatus.OK);
	}
	
}
