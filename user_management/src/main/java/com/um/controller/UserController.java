package com.um.controller;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.um.entity.UserEntity;
import com.um.exception.ValidationException;
import com.um.handler.LoginLogoutHandler;
import com.um.model.LoginRequest;
import com.um.model.LoginResponse;

@RestController
public class UserController {

	private LoginLogoutHandler loginLogoutHandler;
	
	public UserController(LoginLogoutHandler loginLogoutHandler) {
		this.loginLogoutHandler = loginLogoutHandler;
	}
	
	@PostMapping(path="/login")
	public ResponseEntity<LoginResponse> login(@RequestBody(required = true) LoginRequest loginRequest) throws ValidationException {
		
		String reqId = UUID.randomUUID().toString();
		log.info("Request received login- reqId: " + reqId);
		UserEntity user = loginLogoutHandler.loginUser(loginRequest.getUserId(), loginRequest.getPassword());
		
		LoginResponse loginResponse = new LoginResponse();
		if(user != null) {
			loginResponse.setSuccessful(true);
			loginResponse.setUser(user);
		}
		
		log.info("Responding to login request - reqId: "+ reqId +" with message: " + loginResponse.getMessage());
		return ResponseEntity.ok(loginResponse);
	}
	
	private static final Logger log = LogManager.getLogger(UserController.class);
	
}
