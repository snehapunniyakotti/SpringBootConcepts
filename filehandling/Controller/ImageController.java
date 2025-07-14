package com.demo.authentication.filehandling.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.authentication.filehandling.Service.ImageService;
import com.demo.authentication.responseHandler.ResponseHandler;

@RestController
@RequestMapping("/api")
public class ImageController {

	@Autowired
	ImageService imageService;

	@PostMapping("/compress")
	public ResponseEntity<Object> imageUploadAndCompress(@RequestParam("File") MultipartFile file) {
		String outputPath = "Compressed_" + file.getOriginalFilename();
		try {
			imageService.imageCompress(file, outputPath, 0.1f);
			return ResponseHandler.generateResponse("Compression done successfully and saved as :" + outputPath,
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/compress/usingLib")
	public ResponseEntity<Object> imageUploadAndCompressLib(@RequestParam("File") MultipartFile file) {
		String outputPath = "Compressed_" + file.getOriginalFilename();
		try {
			imageService.usingThumbnailatorLibrary(file);
			return ResponseHandler.generateResponse("Compression done successfully and saved as :" + outputPath,
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/imageConverter")
	public ResponseEntity<Object> imageConverter(@RequestParam("File") MultipartFile file,
			@RequestParam("name") String formatName) {
		try {
			System.out.println(" formatName : "+formatName);
			imageService.converter(file, formatName);
			return ResponseHandler.generateResponse("File converted successfully to " + formatName + " formate",
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.OK);
		}
	}

}
