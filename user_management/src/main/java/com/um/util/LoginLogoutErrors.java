package com.um.util;

public class LoginLogoutErrors {

	private static final String PASSWORD_INCORRECT = "PASSWORD_INCORRECT";
	private static final String USERID_INCORRECT = "USERID_INCORRECT";
	
	public static String invalidPassword() {
		return PASSWORD_INCORRECT;
	}
	
	public static String invalidLoginId() {
		return USERID_INCORRECT;
	}
	
}
