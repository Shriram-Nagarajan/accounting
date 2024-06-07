package com.um.poc;

import org.mindrot.jbcrypt.BCrypt;

public class Main {
	public static void main(String[] args) {
		int workload = 12;
        // Generate a salt and hash the password
        String salt = BCrypt.gensalt(workload);
        System.out.println(BCrypt.hashpw("shalini", salt));
	}
}
