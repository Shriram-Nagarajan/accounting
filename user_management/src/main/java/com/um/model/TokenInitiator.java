package com.um.model;

public class TokenInitiator {
	
	private String authenticationType;
	private String authenticationId;
	private String userName;
	
	public TokenInitiator(String authType, String authId, String userName) {
		this.authenticationType = authType;
		this.authenticationId = authId;
		this.userName = userName;
	}
	
	public String getAuthenticationType() {
		return authenticationType;
	}
	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}
	public String getAuthenticationId() {
		return authenticationId;
	}
	public void setAuthenticationId(String authenticationId) {
		this.authenticationId = authenticationId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
