package com.um.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.um.entity.AuthenticationType;
import com.um.handler.ForgotPasswordHandler;
import com.um.model.ForgotPwdTokenRequest;
import com.um.model.ForgotPwdTokenResponse;

@RestController
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

	private ApplicationContext applicationContext;
	
	public ForgotPasswordController(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@PostMapping("/{authenticationType}")
	public ResponseEntity<ForgotPwdTokenResponse> sendToken(@PathVariable("authenticationType") AuthenticationType authenticationType, @RequestBody ForgotPwdTokenRequest tokenRequest) {
		
		// Choose appropriate implementation handler
		ForgotPasswordHandler forgotPasswordHandler = (ForgotPasswordHandler) applicationContext.getBean(
				authenticationType.name()+ForgotPasswordHandler.HANDLER_NAME);
				
		// Generate token, send the token to use and then store it in DB
		ForgotPwdTokenResponse response = forgotPasswordHandler.sendToken(tokenRequest);
		
		// Return response
		return ResponseEntity.status(response.getStatusCode()).body(response);
		
	}
	
	
	
}
