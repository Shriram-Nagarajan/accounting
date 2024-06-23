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
import com.um.model.ForgotPwdTokenVerificationResponse;
import com.um.model.ResetPasswordRequest;
import com.um.model.ResetPasswordResponse;
import com.um.model.VerifyForgotPwdTokenRequest;

@RestController
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

	private ApplicationContext applicationContext;
	
	public ForgotPasswordController(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@PostMapping("/send-token/{authenticationType}")
	public ResponseEntity<ForgotPwdTokenResponse> sendToken(@PathVariable("authenticationType") AuthenticationType authenticationType, @RequestBody ForgotPwdTokenRequest tokenRequest) {
		
		// Choose appropriate implementation handler
		ForgotPasswordHandler forgotPasswordHandler = (ForgotPasswordHandler) applicationContext.getBean(
				authenticationType.name()+ForgotPasswordHandler.HANDLER_NAME);
				
		// Generate token, send the token to use and then store it in DB
		ForgotPwdTokenResponse response = forgotPasswordHandler.sendToken(tokenRequest);
		
		// Return response
		return ResponseEntity.status(response.getStatusCode()).body(response);
		
	}
	
	@PostMapping("/verify-token/{authenticationType}")
	public ResponseEntity<ForgotPwdTokenVerificationResponse> verifyToken(@PathVariable("authenticationType") AuthenticationType authenticationType, @RequestBody VerifyForgotPwdTokenRequest tokenRequest) {
		
		// Choose appropriate implementation handler
		ForgotPasswordHandler forgotPasswordHandler = (ForgotPasswordHandler) applicationContext.getBean(
				authenticationType.name()+ForgotPasswordHandler.HANDLER_NAME);
				
		// Verify the token and then update the token status in DB
		ForgotPwdTokenVerificationResponse tokenResponse = forgotPasswordHandler.verifyToken(tokenRequest);
		
		// Return response
		return ResponseEntity.status(tokenResponse.getStatusCode()).body(tokenResponse);
		
	}
	
	@PostMapping("/reset-password/{authenticationType}")
	public ResponseEntity<ResetPasswordResponse> resetPassword(@PathVariable("authenticationType") AuthenticationType authenticationType, @RequestBody ResetPasswordRequest resetPasswordRequest) {

		// Choose appropriate implementation handler
		ForgotPasswordHandler forgotPasswordHandler = (ForgotPasswordHandler) applicationContext.getBean(
				authenticationType.name()+ForgotPasswordHandler.HANDLER_NAME);
		
		// Validate that user exists, the token has been verified
		// And then, Update the password
		ResetPasswordResponse response = forgotPasswordHandler.resetPassword(resetPasswordRequest);
		
		// Return appropriate response
		return ResponseEntity.status(response.getStatusCode()).body(response);

	}
	
}
