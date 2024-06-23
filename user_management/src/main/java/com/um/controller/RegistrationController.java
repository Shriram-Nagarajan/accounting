package com.um.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.exception.ValidationException;
import com.um.auth.UserSessionHandler;
import com.um.model.RegistrationRequest;
import com.um.model.RegistrationResponse;
import com.um.model.SendEmailRegistrationTokenRequest;
import com.um.model.TokenInitiator;
import com.um.model.TokenInitiatorResponse;
import com.um.model.TokenVerificationResponse;
import com.um.model.TokenVerifier;
import com.um.model.VerifyEmailRegistrationTokenRequest;
import com.um.registration.RegistrationFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/registration")
public class RegistrationController {
	
	private static final String EMAIL = "email";
	private RegistrationFactory registrationFactory;
	private UserSessionHandler userSessionHandler;
	
	public RegistrationController(RegistrationFactory registrationFactory, UserSessionHandler userSessionHandler) {
		this.registrationFactory = registrationFactory; 
		this.userSessionHandler = userSessionHandler;
	}
	
	@PostMapping("/send-email-otp")
	public ResponseEntity<TokenInitiatorResponse> sendEmailRegistrationToken(@RequestBody SendEmailRegistrationTokenRequest request) {
		
		TokenInitiatorResponse response = TokenInitiatorResponse.tokenSent();
		if(request != null && request.getEmailId() != null && !request.getEmailId().isBlank()) {
			
			TokenInitiator initiator = new TokenInitiator(EMAIL, request.getEmailId(), request.getEmailId());
			response = registrationFactory.getHandler(EMAIL)
									.sendRegistrationToken(initiator);
			
		}	else {
			response = TokenInitiatorResponse.reqdParametersNotProvided();
		}
		return ResponseEntity.status(response.getStatusCode())
					.body(response);
		
	}

	@PostMapping("/verify-email-otp")
	public ResponseEntity<TokenVerificationResponse> verifyEmailRegistrationToken(@RequestBody VerifyEmailRegistrationTokenRequest request) {
		
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
	
	@PostMapping("/register-user")
	public ResponseEntity<RegistrationResponse> registerUser(@RequestBody RegistrationRequest regnRequest,
			HttpServletRequest request,
			HttpServletResponse response) throws ValidationException {
		
		RegistrationResponse regnResponse = null;
		
		if(regnRequest != null
				&& regnRequest.getUserId() != null && !regnRequest.getUserId().isBlank()
				&& regnRequest.getEmailId() != null && !regnRequest.getEmailId().isBlank()
				&& regnRequest.getToken() != null && !regnRequest.getToken().isBlank()
				&& regnRequest.getPassword() != null && !regnRequest.getPassword().isBlank()) {
			
			regnResponse = registrationFactory.getHandler(EMAIL)
						.registerUser(regnRequest);
			if(regnResponse.getStatusCode() == 200 && regnResponse.getUser() != null) {
				userSessionHandler.createSessionOnLogin(regnResponse.getUser(), request, response);
			}
			
		}	else {
			regnResponse = new RegistrationResponse(400, RegistrationResponse.REQD_PARAMS_NOT_PROVIDED);
		}
		return ResponseEntity.status(regnResponse.getStatusCode()).body(regnResponse);
		
	}
}
