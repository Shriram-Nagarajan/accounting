package com.um.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.um.handler.EmailService;
import com.um.model.ApiResponse;
import com.um.model.EmailMessage;

@RestController
public class TestEmailController {

	private EmailService emailService;
	
	public TestEmailController(@Qualifier("simpleMailService") EmailService emailService) {
		this.emailService = emailService;
	}
	
	@PostMapping("/testEmail")
	public ResponseEntity<ApiResponse> sendEmail(@RequestBody EmailMessage emailMessage) {
		emailService.sendEmail(emailMessage);
		return ResponseEntity.ok(new ApiResponse(ApiResponse.SUCCESS));
	}
	
}
