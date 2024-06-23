package com.um.model;

public class VerifyEmailRegistrationTokenRequest {
	
	private String emailId;
	private String token;
	
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}
