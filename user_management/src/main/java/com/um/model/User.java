package com.um.model;

import java.io.Serializable;

import com.um.entity.UserEntity;

public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private UserEntity userEntity;
	private String sessionId;

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
