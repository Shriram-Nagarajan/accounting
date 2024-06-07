package com.um.handler;

import com.um.entity.UserEntity;
import com.um.exception.ValidationException;

public interface LoginLogoutHandler {

	public UserEntity loginUser(String loginId, String password) throws ValidationException;
	
}
