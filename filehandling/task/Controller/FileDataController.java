package com.demo.authentication.filehandling.task.Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.authentication.filehandling.task.Entity.FileData;
import com.demo.authentication.filehandling.task.Service.FileDataService;
import com.demo.authentication.responseHandler.ResponseHandler;

@RestController
@RequestMapping("/api")
public class FileDataController {
	@Value("${file.upload-dir}")
	String FILE_DIRECTORY;
	
//	@Autowired
//	FileDataService fileDataService;
	
//	@PostMapping("/savefiles")
//	public ResponseEntity<Object> fileUpload(@RequestParam("File") MultipartFile file) throws IOException{
//		
//		File myfile = new File(FILE_DIRECTORY+file.getOriginalFilename());
//		myfile.createNewFile();
//		FileOutputStream fos = new FileOutputStream(myfile);
//		fos.write(file.getBytes());
//		fos.close();
//
//		return ResponseHandler.generateResponse("file uploaded ", HttpStatus.OK);
//	
//		
//	}
	
	
}
