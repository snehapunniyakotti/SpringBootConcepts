package com.demo.authentication.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.demo.authentication.responseHandler.ResponseHandler;

@ControllerAdvice
public class GlobalExceptionController {
 
	 @ExceptionHandler(UncheckedException.class) 
	 public  ResponseEntity<Object> uncheckedException(UncheckedException ex){
		return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.OK); 
	 }
	  
	 @ExceptionHandler(CheckedException.class)
	 public ResponseEntity<Object> checked(CheckedException ex){
		 return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.OK);
	 }
	 
	 @ExceptionHandler(MaxUploadSizeExceededException.class )
	 public ResponseEntity<Object> maxUploadSizeExceededException(MaxUploadSizeExceededException ex){
		  return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST); 
	 }
	 
	 @ExceptionHandler(HttpRequestMethodNotSupportedException.class	)
	 public ResponseEntity<Object> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex){
		 return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST); 
	 }
}
    