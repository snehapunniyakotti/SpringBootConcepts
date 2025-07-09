package com.demo.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//import com.demo.authentication.ThreadTaskRunner.PrintTreadRunner;
import com.demo.authentication.service.MultipleThreadService;

@RestController
@RequestMapping("/thread")
public class ThreadController {

	@Autowired
	MultipleThreadService multipleThreadService;

//	@Autowired
//	PrintTreadRunner thread;

	@GetMapping("/test")
	public ResponseEntity<?> callMultipleThread() {
		try {
//			thread.run("dssdf");
			multipleThreadService.print();
			multipleThreadService.asyncMethod();
			multipleThreadService.call();
			return new ResponseEntity<>("Printing the test resuolt !!!!", HttpStatus.OK);  
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/test2")
	public ResponseEntity<?> callMailAndFileService(@RequestParam("Files") MultipartFile[] files){
		try {
			multipleThreadService.sendMail();
			multipleThreadService.saveMultipleFile(files);
			return new ResponseEntity<>("The mail and file service have been called !!!!", HttpStatus.OK);  
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
