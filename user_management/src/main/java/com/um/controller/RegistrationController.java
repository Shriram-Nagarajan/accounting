package com.um.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.um.model.SendEmailRegistrationTokenRequest;
import com.um.model.TokenInitiator;
import com.um.model.TokenInitiatorResponse;
import com.um.model.TokenVerificationResponse;
import com.um.model.TokenVerifier;
import com.um.model.VerifyEmailTokenRequest;
import com.um.registration.RegistrationFactory;

@RestController
@RequestMapping(path = "/registration")
public class RegistrationController {
	
	private static final String EMAIL = "email";
	private RegistrationFactory registrationFactory;
	
	public RegistrationController(RegistrationFactory registrationFactory) {
		this.registrationFactory = registrationFactory; 
	}
	
	@PostMapping("/send-email-otp")
	public ResponseEntity<TokenInitiatorResponse> sendEmailRegistrationToken(@RequestBody SendEmailRegistrationTokenRequest request) {
		
		TokenInitiatorResponse response = TokenInitiatorResponse.tokenSent();
		if(request != null && request.getEmailId() != null && !request.getEmailId().isBlank()
				&& request.getName() != null && !request.getName().isBlank()) {
			
			TokenInitiator initiator = new TokenInitiator(EMAIL, request.getEmailId(), request.getName());
			response = registrationFactory.getHandler(EMAIL)
									.sendRegistrationToken(initiator);
			
		}	else {
			response = TokenInitiatorResponse.reqdParametersNotProvided();
		}
		return ResponseEntity.status(response.getStatusCode())
					.body(response);
		
	}

	@PostMapping("/verify-email-otp")
	public ResponseEntity<TokenVerificationResponse> verifyEmailRegistrationToken(@RequestBody VerifyEmailTokenRequest request) {
		
		TokenVerificationResponse response = null;
		
		if(request != null && request.getEmailId() != null && !request.getEmailId().isBlank()
				&& request.getToken() != null && !request.getToken().isBlank()) {
			
			TokenVerifier tokenVerifier = new TokenVerifier();
			tokenVerifier.setAuthenticationId(request.getEmailId());
			tokenVerifier.setAuthenticationType(EMAIL);
			tokenVerifier.setToken(request.getToken());
			
			response = registrationFactory.getHandler(EMAIL)
						.verifyRegistrationToken(tokenVerifier);
			
		}
		return ResponseEntity.status(response.getStatusCode()).body(response);
		
	}
}
