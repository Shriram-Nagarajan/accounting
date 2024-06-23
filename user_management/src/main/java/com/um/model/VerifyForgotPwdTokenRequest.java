package com.um.model;

public class VerifyForgotPwdTokenRequest {
	
	private String authenticationId;
	private String token;

	public String getAuthenticationId() {
		return authenticationId;
	}
	public void setAuthenticationId(String authenticationId) {
		this.authenticationId = authenticationId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}
