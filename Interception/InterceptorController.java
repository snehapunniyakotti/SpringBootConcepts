package com.demo.authentication.Interception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api")
public class InterceptorController {
	
	@GetMapping("/api/interception")
	public List<Student> interceptorTest(){
		
		List<Student> list = new ArrayList<Student>();
		
		list.add(new Student(1,"Sneha","P"));
		list.add(new Student(2,"naaae","gyrf"));
		list.add(new Student(2,"jdfjds","wety"));
		
		return list;
		
	}

}
