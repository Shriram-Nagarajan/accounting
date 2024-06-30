package com.um.handler.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.common.model.User;
import com.common.model.UserDetails;
import com.um.accounting.entity.AccountDetails;
import com.um.accounting.entity.UserAccountId;
import com.um.accounting.entity.UserAccountMapping;
import com.um.entity.AuthenticationType;
import com.um.entity.RegistrationToken;
import com.um.entity.RegistrationTokenStatus;
import com.um.entity.UserEntity;
import com.um.handler.EmailService;
import com.um.handler.RegistrationHandler;
import com.um.model.EmailMessage;
import com.um.model.RegistrationRequest;
import com.um.model.RegistrationResponse;
import com.um.model.TokenInitiator;
import com.um.model.TokenInitiatorResponse;
import com.um.model.TokenVerificationResponse;
import com.um.model.TokenVerifier;
import com.um.repository.RegistrationTokenRepository;
import com.um.repository.UserRepository;
import com.um.util.PasswordValidator;
import com.um.util.TextUtil;
import com.um.util.TokenUtil;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service("email"+RegistrationHandler.BEAN_SUFFIX)
public class EmailRegistrationHandler implements RegistrationHandler {
	
	private static final String REGN_EMAIL_PROP = "registration.token.mail.";
	
	private EmailService emailService;
	private UserRepository userRepository;
	private EntityManager accountingEntityManagerFactory;
	private RegistrationTokenRepository tokenRepository;
	private PasswordValidator passwordValidator;
	private Environment env;
	
	public EmailRegistrationHandler(@Qualifier("simpleMailService") EmailService emailService,
			UserRepository userRepository,
			@Qualifier("accountingEntityManagerFactory") EntityManager accountingEntityManagerFactory,
			RegistrationTokenRepository tokenRepository,
			@Qualifier("bCryptPasswordValidator") PasswordValidator passwordValidator,
			Environment env) {
		this.emailService = emailService;
		this.userRepository = userRepository;
		this.accountingEntityManagerFactory = accountingEntityManagerFactory;
		this.tokenRepository = tokenRepository;
		this.passwordValidator = passwordValidator;
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
					// NOTE: Currently using a 6 digit token and expecting it to be a unique key
					// TODO: Refine this approach to have a more appropriate unique key or remove the unique key
					String token = TokenUtil.generateRandomToken(Integer.parseInt(getMailProperty("num-digits-token")));
					Map<String, String> substitutionMap = new HashMap<String, String>();
					substitutionMap.put("userName", tokenInitiator.getAuthenticationId());
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
					tokenEntity.setTokenStatus(RegistrationTokenStatus.sent);
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
					.filter(result -> AuthenticationType.email.equals(AuthenticationType.email)
								&& tokenVerifier.getAuthenticationId().equals(result.getAuthenticationId()))
					.toList();
				
				if(registrationTokenList != null && !registrationTokenList.isEmpty()) {
					
					RegistrationToken token = registrationTokenList.get(0);
					
					if(token != null) {
						if(RegistrationTokenStatus.sent.equals(token.getTokenStatus()) ) {
							
							LocalDateTime currentTime = LocalDateTime.now();
							
							if(currentTime.isAfter(token.getExpiryTimeStamp())) {
								token.setTokenStatus(RegistrationTokenStatus.expired);
								tokenRepository.save(token);
								return TokenVerificationResponse.tokenExpired();
							}
							
							token.setTokenStatus(RegistrationTokenStatus.verified);
							tokenRepository.save(token);
							return TokenVerificationResponse.tokenVerifiedSuccessfully();
						}	else if(RegistrationTokenStatus.verified.equals(token.getTokenStatus()) ) {
							return TokenVerificationResponse.tokenVerifiedSuccessfully();
						}	else if(RegistrationTokenStatus.expired.equals(token.getTokenStatus())) {
							return TokenVerificationResponse.tokenExpired();
						}	else {
							return TokenVerificationResponse.userAlreadyExists();
						}
					}	 else {
						return TokenVerificationResponse.invalidTokenOrEmail();
					}
				}	 else {
					return TokenVerificationResponse.invalidTokenOrEmail();
				}
			}
		}
		
		return TokenVerificationResponse.reqdParamsNotProvided();
	}

	@Override
	@Transactional
	public RegistrationResponse registerUser(RegistrationRequest request) {
		
		RegistrationResponse registrationResponse = null;
		
		if(request != null) {
			
			if(request.getUserId() != null && request.getEmailId() != null) {
				
				if(doesUserExist(request.getUserId())) {
					registrationResponse = RegistrationResponse.userAlreadyExists();
				}	else {
					if(request.getToken() != null) {
						List<RegistrationToken> registrationTokenList =  tokenRepository.findByToken(request.getToken())
								.stream()
								.filter(result -> AuthenticationType.email.equals(AuthenticationType.email)
											&& request.getUserId().equals(result.getAuthenticationId()))
								.toList();
						if(registrationTokenList != null && !registrationTokenList.isEmpty()) {
							RegistrationToken token = registrationTokenList.get(0);
							
							if(token != null) {
								if(RegistrationTokenStatus.sent.equals(token.getTokenStatus()) ) {
									registrationResponse = RegistrationResponse.tokenNotVerified();
								}	else if(RegistrationTokenStatus.expired.equals(token.getTokenStatus())) {
									registrationResponse = RegistrationResponse.tokenExpired();
								}	else if(RegistrationTokenStatus.verified.equals(token.getTokenStatus())) {
									
									if(request.getPassword() != null && request.getConfirmPassword() != null
											&& request.getPassword().equals(request.getConfirmPassword())) {
										token.setTokenStatus(RegistrationTokenStatus.registered);
										tokenRepository.save(token);
										UserEntity userEntity = new UserEntity();
										userEntity.setLoginId(request.getUserId());
										userEntity.setEmailId(request.getEmailId());
										userEntity.setName(request.getName());
										userEntity.setPassword(passwordValidator.hashPassword(request.getPassword()));
										userRepository.save(userEntity);
										
										User user = new User();
										UserDetails userDetails = userEntity.getUserDetails();
										user.setUserDetails(userDetails);
										
										//TODO: Fetch this data from user later
										int accountNumberLength = Integer.parseInt(env.getProperty("account-number.default.length"));
										AccountDetails accountDetails = new AccountDetails();
										accountDetails.setAccountNumber(TokenUtil.generateRandomToken(accountNumberLength));
										accountDetails.setName(userEntity.getName());
										accountingEntityManagerFactory.persist(accountDetails);
										
										UserAccountId userAccountId = new UserAccountId();
										userAccountId.setAccountId(accountDetails.getAccountId());
										userAccountId.setUserId(userEntity.getUserId());
										UserAccountMapping userAccountMapping = new UserAccountMapping(userAccountId);
										accountingEntityManagerFactory.persist(userAccountMapping);
										
										registrationResponse = RegistrationResponse.registrationSuccess(user);
										
									}	else {
										registrationResponse = RegistrationResponse.passwordAndConfirmPasswordDoesntMatch();
									}
									
									
								}	else {
									registrationResponse = RegistrationResponse.userAlreadyExists();
								}
							}	else {
								registrationResponse = RegistrationResponse.invalidTokenOrEmail();
							}

						}	else {
							registrationResponse = RegistrationResponse.invalidTokenOrEmail();
						}

					}	else {
						registrationResponse = new RegistrationResponse(403, RegistrationResponse.REQD_PARAMS_NOT_PROVIDED);
					}
				}
				
			}	else {
				registrationResponse = new RegistrationResponse(403, RegistrationResponse.REQD_PARAMS_NOT_PROVIDED);
			}
			
		}	else {
			registrationResponse = new RegistrationResponse(403, RegistrationResponse.REQD_PARAMS_NOT_PROVIDED);
		}
		
		return registrationResponse;
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
