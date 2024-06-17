package com.um.util;

import java.security.SecureRandom;

public class TokenUtil {

	public static String generateRandomToken(int length) {
		SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        // Generate a random number between 0 and 9 for each digit
        for (int i = 0; i < length; i++) {
            int randomDigit = random.nextInt(10);
            otp.append(randomDigit);
        }

        return otp.toString();
	}
	
}
