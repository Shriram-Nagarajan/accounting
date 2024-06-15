package com.um.model;

import com.common.model.UserDetails;

public class LoginResponse extends ApiResponse {

	private boolean successful;
	private UserDetails user;
	
	public LoginResponse(int statusCode, String message) {
		super(statusCode, message);
	}
	
	public LoginResponse() {
		this(200, SUCCESS);
		successful = true;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public UserDetails getUser() {
		return user;
	}

	public void setUser(UserDetails user) {
		this.user = user;
	}

}
