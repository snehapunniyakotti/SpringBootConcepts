package com.demo.authentication.filehandling.Controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.authentication.filehandling.Service.VideoService;
import com.demo.authentication.responseHandler.ResponseHandler;

import io.github.techgnious.exception.VideoException;

@RestController
@RequestMapping("/api")
public class VideoController {

	@Autowired
	VideoService videoService;

	@PostMapping("/videocompress")
	public ResponseEntity<Object> videoCompressor(@RequestParam("Video") MultipartFile videoFile)
			throws VideoException {
		try {
			File compressedVideofile = videoService.videoCompressor(videoFile);
//			File compressedVideofile = videoService.compressVideo3(videoFile);
			return ResponseHandler.generateResponse(
					"Compressed video is saved path : " + compressedVideofile.getAbsolutePath(), HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.OK);
		}

	}

}
