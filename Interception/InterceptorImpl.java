package com.demo.authentication.Interception;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class InterceptorImpl implements HandlerInterceptor{
	 
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
	       try {
	    	   System.out.println("PreHandlev method !!!!!!!!!!!!!!!!!");
	       }catch(Exception e) {
	    	   e.printStackTrace();
	    	   return false;
	       }
		return true;
	}
	 
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//		// TODO Auto-generated method stub
//		return HandlerInterceptor.super.preHandle(request, response, handler);
//	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		try {
			System.out.println("postHandle method @@@@@@@@@@@@@@@@");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		try {
			System.out.println("afterCompletion method $$$$$$$$$$$$$$$");
		}catch(Exception e) {
		    e.printStackTrace();	
		}
	}

}
