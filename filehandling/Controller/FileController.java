package com.demo.authentication.filehandling.Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.authentication.filehandling.Service.FileService;
import com.demo.authentication.filehandling.task.Entity.FormData;
import com.demo.authentication.responseHandler.ResponseHandler;

@RestController
@RequestMapping("/api")
public class FileController {

	@Value("${file.upload-dir}")
	String FILE_DIRECTORY;

	@Autowired
	FileService fileService;

	@PostMapping("/savefile")
	public ResponseEntity<Object> fileUpload(@RequestParam("File") MultipartFile file) throws IOException {

//		File myfile = new File(FILE_DIRECTORY+file.getOriginalFilename());
//		myfile.createNewFile();
//		FileOutputStream fos = new FileOutputStream(myfile);
//		fos.write(file.getBytes());
//		fos.close();
//
//		return ResponseHandler.generateResponse("file uploaded ", HttpStatus.OK);
		return fileService.fileUploadService(file);

	}

	@PostMapping("/savefiles")
	public ResponseEntity<Object> multiFileUpload(@RequestParam("Files") MultipartFile[] files) {
		return fileService.multiFileUploadService(files);
	}

	@GetMapping("/getfilebyid/{id}")
	public ResponseEntity<Resource> getFileById(@PathVariable Integer id) {
		return fileService.getFileById(id);
	}

	@PostMapping("/zip")
	public ResponseEntity<Object> ZipFile(@RequestParam("Files") MultipartFile[] files) {
		try {
//			return fileService.zipFile(file); // single file zip
			return fileService.zipMultipleFile(files);
		} catch (Exception e) { 
			return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.OK);
		}
	}

	@PostMapping("/unzip")
	public ResponseEntity<Object> UnZipFile(@RequestParam("Files") MultipartFile[] files) {
		try {
//			return fileService.unzipFile(file); // single file unzip
			return fileService.unzipMultipleFile(files);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.OK);
		}

	}

}
