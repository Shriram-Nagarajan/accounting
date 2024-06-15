package com.um.handler;

import com.um.entity.UserEntity;
import com.common.exception.ValidationException;

public interface LoginLogoutHandler {

	public UserEntity loginUser(String loginId, String password) throws ValidationException;
	
}
