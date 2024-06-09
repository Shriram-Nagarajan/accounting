package com.um.controller;

import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.um.auth.UserSessionHandler;
import com.um.entity.UserEntity;
import com.um.exception.ValidationException;
import com.um.handler.LoginLogoutHandler;
import com.um.model.ApiResponse;
import com.um.model.LoginRequest;
import com.um.model.LoginResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UserController {

	private LoginLogoutHandler loginLogoutHandler;
	private UserSessionHandler sessionHandler;
	
	public UserController(LoginLogoutHandler loginLogoutHandler, UserSessionHandler userSessionHandler) throws IOException {
		this.loginLogoutHandler = loginLogoutHandler;
		sessionHandler = userSessionHandler;
	}
	
	@PostMapping(path="/login")
	public ResponseEntity<LoginResponse> login(@RequestBody(required = true) LoginRequest loginRequest, HttpServletResponse response) throws ValidationException {
		
		String reqId = UUID.randomUUID().toString();
		log.info("Request received login- reqId: " + reqId);
		UserEntity user = loginLogoutHandler.loginUser(loginRequest.getUserId(), loginRequest.getPassword());
		
		LoginResponse loginResponse = new LoginResponse();
		if(user != null) {
			sessionHandler.addSession(user, response);
			loginResponse.setSuccessful(true);
			loginResponse.setUser(user);
		}
		
		log.info("Responding to login request - reqId: "+ reqId +" with message: " + loginResponse.getMessage());
		return ResponseEntity.ok(loginResponse);
	}
	
	@GetMapping("/get-session")
	public ResponseEntity<UserEntity> getSession(HttpServletRequest request) throws ValidationException {
		return ResponseEntity.ok(sessionHandler.getSession(request));
	}
	
	@GetMapping("/logout")
	public ResponseEntity<ApiResponse> logout(HttpServletRequest request) throws ValidationException {
		sessionHandler.removeSession(request);
		return ResponseEntity.ok().body(new ApiResponse(200, ApiResponse.SUCCESS));
	}
	
	private static final Logger log = LogManager.getLogger(UserController.class);
	
}
