package com.um.handler;

import com.um.model.TokenInitiator;
import com.um.model.TokenInitiatorResponse;
import com.um.model.TokenVerificationResponse;
import com.um.model.TokenVerifier;

public interface RegistrationHandler {
	
	public static final String BEAN_SUFFIX = "RegistrationHandler";

	public TokenInitiatorResponse sendRegistrationToken(TokenInitiator tokenInitiator);
	
	public TokenVerificationResponse verifyRegistrationToken(TokenVerifier tokenVerifier);
	
	public boolean doesUserExist(String authId);
	
}
