package com.demo.authentication.filehandling.task.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.PutExchange;

import com.demo.authentication.filehandling.task.Entity.FormData;
import com.demo.authentication.filehandling.task.Service.FormDataService;

@RestController
@RequestMapping("/api")
public class FormDataController {

	@Autowired
	FormDataService  formDataService;
	
	@PostMapping("/saveformdate")
	public ResponseEntity<Object> saveform(@ModelAttribute FormData formdata,
			@RequestParam("Files") MultipartFile[] files){
		return formDataService.saveform(formdata,files	);
	}
	
	@GetMapping("/getAllData")
	public ResponseEntity<Object> getAllrecords(){
		return formDataService.getAllRecords();
	}
	
	@PutMapping("/updateFilebyId/{id}")
	public ResponseEntity<Object> updateFiles(@PathVariable Integer id ,@RequestParam("Files") MultipartFile[] files){
		return formDataService.updateFiles(id,files);
	}
	
	@GetMapping("/getById/{id}")
	public ResponseEntity<Object> getById(@PathVariable Integer id){
		return formDataService.getById(id); 
	}
}








