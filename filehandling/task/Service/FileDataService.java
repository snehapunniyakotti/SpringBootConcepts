package com.demo.authentication.filehandling.task.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.demo.authentication.filehandling.task.Entity.FileData;
import com.demo.authentication.filehandling.task.Repository.FileDataRepository;
import com.demo.authentication.responseHandler.ResponseHandler;

@Service
public class FileDataService {

//	@Autowired
//	FileDataRepository fileDataRepository;
//
//	public ResponseEntity<Object> saveFileData(FileData fd) {
//		fileDataRepository.save(fd);
//		return ResponseHandler.generateResponse("Files Uploaded", HttpStatus.OK);
//	}

}
