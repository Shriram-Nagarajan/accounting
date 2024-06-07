package com.um.model;

import com.um.entity.UserEntity;

public class LoginResponse extends ApiResponse {

	private boolean successful;
	private UserEntity user;
	
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

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}
