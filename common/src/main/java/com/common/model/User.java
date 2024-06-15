package com.common.model;

import java.io.Serializable;


public class User implements Serializable, Cloneable{
	
	private static final long serialVersionUID = 1L;
	
	private UserDetails userDetails;
	private String sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
	
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}