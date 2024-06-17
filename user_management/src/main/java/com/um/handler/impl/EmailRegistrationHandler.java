package com.um.handler.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.um.entity.AuthenticationType;
import com.um.entity.RegistrationToken;
import com.um.entity.TokenStatus;
import com.um.handler.EmailService;
import com.um.handler.RegistrationHandler;
import com.um.model.EmailMessage;
import com.um.model.TokenInitiator;
import com.um.model.TokenInitiatorResponse;
import com.um.model.TokenVerificationResponse;
import com.um.model.TokenVerifier;
import com.um.repository.RegistrationTokenRepository;
import com.um.repository.UserRepository;
import com.um.util.TextUtil;
import com.um.util.TokenUtil;

@Service("email"+RegistrationHandler.BEAN_SUFFIX)
public class EmailRegistrationHandler implements RegistrationHandler {
	
	private static final String REGN_EMAIL_PROP = "registration.token.mail.";
	
	private EmailService emailService;
	private UserRepository userRepository;
	private RegistrationTokenRepository tokenRepository;
	private Environment env;
	
	public EmailRegistrationHandler(@Qualifier("simpleMailService") EmailService emailService,
			UserRepository userRepository,
			RegistrationTokenRepository tokenRepository,
			Environment env) {
		this.emailService = emailService;
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
		this.env = env;
	}

	@Override
	public TokenInitiatorResponse sendRegistrationToken(TokenInitiator tokenInitiator) {

		if (tokenInitiator != null) {

			if (tokenInitiator.getAuthenticationId() != null) {
				if (doesUserExist(tokenInitiator.getAuthenticationId())) {
					return TokenInitiatorResponse.userAlreadyExists();
				} else {
					
					// Send OTP in email
					String token = TokenUtil.generateRandomToken(Integer.parseInt(getMailProperty("num-digits-token")));
					Map<String, String> substitutionMap = new HashMap<String, String>();
					substitutionMap.put("userName", tokenInitiator.getUserName());
					substitutionMap.put("token", token);
					String mailBody = TextUtil.substituteVariables(getMailProperty("body"), substitutionMap);
					EmailMessage message = new EmailMessage(getMailProperty("from-address"),
							tokenInitiator.getAuthenticationId(), getMailProperty("subject"), mailBody);
					emailService.sendEmail(message);
					
					// Save the token sent
					RegistrationToken tokenEntity = new RegistrationToken();
					tokenEntity.setToken(token);
					tokenEntity.setAuthenticationId(tokenInitiator.getAuthenticationId());
					tokenEntity.setAuthenticationType(AuthenticationType.email);
					tokenEntity.setEventTimeStamp(LocalDateTime.now());
					LocalDateTime expiryTimeStamp = LocalDateTime.now()
							.plusMinutes(Integer.parseInt(getMailProperty("expiry-minutes")));
					tokenEntity.setExpiryTimeStamp(expiryTimeStamp);
					tokenEntity.setTokenStatus(TokenStatus.sent);
					tokenRepository.saveAndFlush(tokenEntity);
					
					return TokenInitiatorResponse.tokenSent();
				}
			}

		}
		return TokenInitiatorResponse.emailIdEmpty();

	}
	
	@Override
	public TokenVerificationResponse verifyRegistrationToken(TokenVerifier tokenVerifier) {

		if(tokenVerifier != null && tokenVerifier.getAuthenticationType() != null
				&& tokenVerifier.getAuthenticationId() != null && tokenVerifier.getToken() != null) {
			
			if (doesUserExist(tokenVerifier.getAuthenticationId())) {
				return TokenVerificationResponse.userAlreadyExists();
			}	else {
				List<RegistrationToken> registrationTokenList =  tokenRepository.findByToken(tokenVerifier.getToken())
					.stream()
					.filter(result -> AuthenticationType.email.equals(AuthenticationType.email))
					.toList();
				
				if(registrationTokenList != null && !registrationTokenList.isEmpty()) {
					
					RegistrationToken token = registrationTokenList.get(0);
					
					if(token != null) {
						if(TokenStatus.sent.equals(token.getTokenStatus()) ) {
							
							LocalDateTime currentTime = LocalDateTime.now();
							
							if(currentTime.isAfter(token.getExpiryTimeStamp())) {
								token.setTokenStatus(TokenStatus.expired);
								tokenRepository.save(token);
								return TokenVerificationResponse.tokenExpired();
							}
							
							token.setTokenStatus(TokenStatus.verified);
							tokenRepository.save(token);
							return TokenVerificationResponse.tokenVerifiedSuccessfully();
						}	else if(TokenStatus.verified.equals(token.getTokenStatus()) ) {
							return TokenVerificationResponse.tokenVerifiedSuccessfully();
						}	else if(TokenStatus.expired.equals(token.getTokenStatus())) {
							return TokenVerificationResponse.tokenExpired();
						}	else {
							return TokenVerificationResponse.userAlreadyExists();
						}
					}	 else {
						return TokenVerificationResponse.invalidToken();
					}
				}	 else {
					return TokenVerificationResponse.invalidToken();
				}
			}
		}
		
		return TokenVerificationResponse.reqdParamsNotProvided();
	}
	
	@Override
	public boolean doesUserExist(String authId) {

		var userList = userRepository.findByLoginId(authId);

		if(userList == null || userList.isEmpty()) {
			userList = userRepository.findByEmailId(authId);
		}
		
		return userList != null && !userList.isEmpty();
		
	}
	
	private String getMailProperty(String key) {
		return env.getProperty(REGN_EMAIL_PROP+key);
	}

}
