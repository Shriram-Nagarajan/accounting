package com.um.handler.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.um.entity.UserEntity;
import com.common.exception.ValidationException;
import com.um.handler.LoginLogoutHandler;
import com.um.repository.UserRepository;
import com.um.util.LoginLogoutErrors;
import com.um.util.PasswordValidator;

@Service("loginLogoutHandler")
public class LoginLogoutHandlerImpl implements LoginLogoutHandler {

	private UserRepository userRepository;
	private PasswordValidator passwordValidator;
	
	public LoginLogoutHandlerImpl(UserRepository userRepository, PasswordValidator bCryptPasswordValidator) {
		this.userRepository = userRepository;
		this.passwordValidator = bCryptPasswordValidator;
	}
	
	@Override
	public UserEntity loginUser(String loginId, String password) throws ValidationException {
		if(loginId != null && !loginId.isBlank() &&
				password != null && !password.isBlank()) {
			List<UserEntity> userList = userRepository.findByLoginId(loginId);
			if(userList != null && !userList.isEmpty() && userList.get(0) != null) {
				UserEntity user = userList.get(0);
				if(passwordValidator.checkPassword(password, user.getPassword())) {
					return user;
				}	else {
					throw new ValidationException(LoginLogoutErrors.invalidPassword());
				}
			} else {
				throw new ValidationException(LoginLogoutErrors.invalidLoginId());
			}
		}	else {
			throw new IllegalArgumentException("Please provide required parameters: User Id and Password");
		}
	}

}
