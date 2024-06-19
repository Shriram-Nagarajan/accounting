package com.um.model;

import com.common.model.User;

public class RegistrationResponse extends ApiResponse{

	private static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
	private static final String INVALID_TOKEN_OR_EMAIL = "INVALID_TOKEN_OR_EMAIL";
	private static final String UNVERIFIED_TOKEN = "TOKEN_NOT_VERIFIED";
	private static final String TOKEN_EXPIRED = "TOKEN_EXPIRED";
	private static final String PASSWORDS_DOESNT_MATCH = "PASSWORD_AND_CONFIRM_PASSWORD_DOESNT_MATCH";

	private User user;
	
	public RegistrationResponse(int statusCode, String message) {
		super(statusCode, message);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public static RegistrationResponse userAlreadyExists() {
		return new RegistrationResponse(403, USER_ALREADY_EXISTS);
	}
	
	public static RegistrationResponse invalidTokenOrEmail() {
		return new RegistrationResponse(403, INVALID_TOKEN_OR_EMAIL);
	}
	
	public static RegistrationResponse tokenNotVerified() {
		return new RegistrationResponse(403, UNVERIFIED_TOKEN);
	}
	
	public static RegistrationResponse tokenExpired() {
		return new RegistrationResponse(403, TOKEN_EXPIRED);
	}
	
	public static RegistrationResponse passwordAndConfirmPasswordDoesntMatch() {
		return new RegistrationResponse(403, PASSWORDS_DOESNT_MATCH);
	}
	
	public static RegistrationResponse registrationSuccess(User user) {
		RegistrationResponse response = new RegistrationResponse(200, SUCCESS);
		response.setUser(user);
		return response;
	}
	
}
