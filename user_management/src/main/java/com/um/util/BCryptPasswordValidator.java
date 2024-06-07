package com.um.util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service("bCryptPasswordValidator")
public class BCryptPasswordValidator implements PasswordValidator {
	
	private static final String LOG_ROUNDS = "password.encryption.rounds";
	private Environment env;
	
	public BCryptPasswordValidator(Environment env) {
		this.env = env;
	}
	
    public String hashPassword(String plainTextPassword) {
        // Define the workload 
        int workload = Integer.parseInt(String.valueOf(env.getProperty(LOG_ROUNDS)));
        // Generate a salt and hash the password
        String salt = BCrypt.gensalt(workload);
        return BCrypt.hashpw(plainTextPassword, salt);
    }
    
    public boolean checkPassword(String plainTextPassword, String hashedPassword) {
        // Verify the password
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

}
