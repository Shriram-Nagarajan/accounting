package com.um.util;

public interface PasswordValidator {
	
	public String hashPassword(String plainTextPassword);
	
	public boolean checkPassword(String plainTextPassword, String hashedPassword);

}
