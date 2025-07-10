package com.demo.authentication.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		// generate session id
		String sessionId = UUID.randomUUID().toString();
		
		// store session id in session
		request.getSession().setAttribute("SessionID",sessionId);
		
		// redirect to url endpoint in controller
		response.sendRedirect("/demo");
		
	}

}
