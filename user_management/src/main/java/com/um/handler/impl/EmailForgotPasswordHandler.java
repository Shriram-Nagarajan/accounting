package com.um.handler.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.um.entity.AuthenticationType;
import com.um.entity.ForgotPasswordToken;
import com.um.entity.ForgotPwdTokenStatus;
import com.um.handler.EmailService;
import com.um.handler.ForgotPasswordHandler;
import com.um.model.EmailMessage;
import com.um.model.ForgotPwdTokenRequest;
import com.um.model.ForgotPwdTokenResponse;
import com.um.model.ForgotPwdTokenVerificationResponse;
import com.um.model.VerifyForgotPwdTokenRequest;
import com.um.repository.ForgotPwdTokenRepository;
import com.um.repository.UserRepository;
import com.um.util.TextUtil;
import com.um.util.TokenUtil;

@Service("email"+ForgotPasswordHandler.HANDLER_NAME)
public class EmailForgotPasswordHandler implements ForgotPasswordHandler {
	
	private static final String FORGOT_PWD_EMAIL_PROP = "forgot-password.token.mail.";
	
	private UserRepository userRepository;
	private ForgotPwdTokenRepository tokenRepository;
	private EmailService emailService;
	private Environment env;
	
	public EmailForgotPasswordHandler(UserRepository userRepository,
			ForgotPwdTokenRepository tokenRepository,
			@Qualifier("simpleMailService") EmailService emailService,
			Environment env) {
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
		this.emailService = emailService;
		this.env = env;
	}
	

	@Override
	public ForgotPwdTokenResponse sendToken(ForgotPwdTokenRequest tokenRequest) {
		
		if(tokenRequest != null && tokenRequest.getAuthenticationId() != null) {
			
			// Validate userId
			var userList = userRepository.findByLoginId(tokenRequest.getAuthenticationId());
			
			if(userList == null || userList.isEmpty()) {
				userList = userRepository.findByEmailId(tokenRequest.getAuthenticationId());
				
				if(userList == null || userList.isEmpty()) {
					return ForgotPwdTokenResponse.userDoesntExist();
				}
			}
			
			// Send otp
			String userName = (userList != null && !userList.isEmpty() &&
					userList.get(0).getName() != null) ? userList.get(0).getName() : tokenRequest.getAuthenticationId();
			// NOTE: Currently using a 6 digit token and expecting it to be a unique key
			// TODO: Refine this approach to have a more appropriate unique key or remove the unique key
			String token = TokenUtil.generateRandomToken(Integer.parseInt(getMailProperty("num-digits-token")));
			Map<String, String> substitutionMap = new HashMap<String, String>();
			substitutionMap.put("userName", userName);
			substitutionMap.put("token", token);
			String mailBody = TextUtil.substituteVariables(getMailProperty("body"), substitutionMap);
			EmailMessage message = new EmailMessage(getMailProperty("from-address"),
					tokenRequest.getAuthenticationId(), getMailProperty("subject"), mailBody);
			emailService.sendEmail(message);
			
			// Store otp in DB
			ForgotPasswordToken tokenEntity = new ForgotPasswordToken();
			tokenEntity.setToken(token);
			tokenEntity.setAuthenticationId(tokenRequest.getAuthenticationId());
			tokenEntity.setAuthenticationType(AuthenticationType.email);
			tokenEntity.setEventTimeStamp(LocalDateTime.now());
			LocalDateTime expiryTimeStamp = LocalDateTime.now()
					.plusMinutes(Integer.parseInt(getMailProperty("expiry-minutes")));
			tokenEntity.setExpiryTimeStamp(expiryTimeStamp);
			tokenEntity.setTokenStatus(ForgotPwdTokenStatus.sent);
			tokenRepository.saveAndFlush(tokenEntity);
			
			// return success response
			return ForgotPwdTokenResponse.tokenSent();
			
		}
		return ForgotPwdTokenResponse.reqdParamsNotProvided();
		
	}

	@Override
	public ForgotPwdTokenVerificationResponse verifyToken(VerifyForgotPwdTokenRequest tokenRequest) {
		
		// Validate the user Id
		var userList = userRepository.findByLoginId(tokenRequest.getAuthenticationId());
		
		if(userList == null || userList.isEmpty()) {
			userList = userRepository.findByEmailId(tokenRequest.getAuthenticationId());
			
			if(userList == null || userList.isEmpty()) {
				return ForgotPwdTokenVerificationResponse.userDoesntExist();
			}
		}
		
		// Validate the token
		List<ForgotPasswordToken> forgotPasswordTokenList =  tokenRepository.findByToken(tokenRequest.getToken())
				.stream()
				.filter(result -> AuthenticationType.email.equals(AuthenticationType.email)
							&& tokenRequest.getAuthenticationId().equals(result.getAuthenticationId()))
				.toList();
		
		if(forgotPasswordTokenList != null && !forgotPasswordTokenList.isEmpty()) {
			
			ForgotPasswordToken token = forgotPasswordTokenList.get(0);
			
			if(token != null) {
				
				if(ForgotPwdTokenStatus.sent.equals(token.getTokenStatus()) ) {
					
					LocalDateTime currentTime = LocalDateTime.now();
					
					if(currentTime.isAfter(token.getExpiryTimeStamp())) {
						token.setTokenStatus(ForgotPwdTokenStatus.expired);
						tokenRepository.save(token);
						return ForgotPwdTokenVerificationResponse.tokenExpired();
					}
					
					token.setTokenStatus(ForgotPwdTokenStatus.verified);
					tokenRepository.save(token);
					return ForgotPwdTokenVerificationResponse.tokenVerifiedSuccessfully();
				}	else if(ForgotPwdTokenStatus.verified.equals(token.getTokenStatus())) {
					return ForgotPwdTokenVerificationResponse.tokenVerifiedSuccessfully();
				}	else if(ForgotPwdTokenStatus.expired.equals(token.getTokenStatus()) ) {
					return ForgotPwdTokenVerificationResponse.tokenExpired();
				}	else {
					return ForgotPwdTokenVerificationResponse.tokenAlreadyUsed();
				}
			}	else {
				return ForgotPwdTokenVerificationResponse.invalidTokenOrEmail();
			}
			
		}	else {
			return ForgotPwdTokenVerificationResponse.invalidTokenOrEmail();
		}
		
	}

	private String getMailProperty(String key) {
		return env.getProperty(FORGOT_PWD_EMAIL_PROP+key);
	}

}
