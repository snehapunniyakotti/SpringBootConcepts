package com.demo.authentication.controller;

import java.io.UncheckedIOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.authentication.Exception.CheckedException;
import com.demo.authentication.Exception.UncheckedException;
import com.demo.authentication.JWTUtil.JWTUtil;
import com.demo.authentication.entity.LoginRequest;
import com.demo.authentication.entity.User;
import com.demo.authentication.repository.UserRepository;
import com.demo.authentication.responseHandler.ResponseHandler;
import com.demo.authentication.service.UserDetailsServiceImpl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
//@RequestMapping("/authentication")
public class AuthController {

//	@Autowired
//	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserDetailsServiceImpl serviceRepo;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@PostMapping("/authentication/api/register") // used custom exception
	public ResponseEntity<Object> registerUser(@RequestBody User data,HttpServletResponse response) throws CheckedException {
		try {

			if (userRepository.findByUsername(data.getUsername()) != null) {
				throw new CheckedException("UserName Already exsist " + data.getUsername());
				// return ResponseHandler.generateResponse("UserName Already exsist " +
				// data.getUsername() , HttpStatus.OK);
			}

			String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
			User user = new User(data.getUsername(), data.getEmail(), encryptedPassword, data.getRole());
			System.out.println("user.getUsername()" + user.getUsername() + " data username " + data.getUsername());
			System.out.println(
					user.getEmail() + " " + user.getPassword() + " " + user.getRole() + " " + user.getUsername());
			userRepository.save(user);
			System.err.println("printing the response of the user : "+user); 
			
			
			Cookie cookie = new Cookie("demo_cookie",data.getUsername());
			cookie.setMaxAge(10);
			response.addCookie(cookie);
			return ResponseHandler.generateResponseWithBody(user, "User Added", HttpStatus.OK);
		} catch (CheckedException e) {
			return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.OK);
		}
	}

	@PostMapping("/api/login/wrong") // used @Exception handler
	public ResponseEntity<Object> LoginUser(@RequestBody LoginRequest req) throws CheckedException {

		UserDetails user = serviceRepo.loadUserByUsername(req.getUsername());

		if (user == null) {
			throw new UncheckedException("user id not found , Login failed");
//		throw new CheckedException("username not matched");
			// return ResponseHandler.generateResponse("user id not found , Login
			// failed",HttpStatus.BAD_REQUEST);
		}

		String username = user.getUsername();

		Cookie cookie = new Cookie("username", username);

		System.out.println("Cookiee : " + cookie.toString());

//		if(username.equals(req.getUsername())) {
		return ResponseHandler.generateResponse("username matched , login success and Cookie is " + cookie.toString(),
				HttpStatus.OK);
//		}else {
//			throw new CheckedException("username not matched");
//            //return ResponseHandler.generateResponse("username not matched",HttpStatus.OK);  
//		}

	}

	@GetMapping("/api/getapi")
	public ResponseEntity<Object> getApi() {
		return ResponseHandler.generateResponse("Testing api", HttpStatus.OK);
	}

	@PostMapping("/authentication/api/login") // used @Exception handler
	public ResponseEntity<Object> LoginUserr(@RequestBody LoginRequest req) throws CheckedException {

		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

			String token = JWTUtil.generateToken(req.getUsername());

			System.err.println("printing the token on geneation :  "+ token);
//			return new ResponseEntity<Object>("Successs!!! and generated token : " + token,
//					HttpStatusCode.valueOf(200));
			return ResponseHandler.generateResponseWithBody('S', token, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatusCode.valueOf(401));
		}

	}

}
