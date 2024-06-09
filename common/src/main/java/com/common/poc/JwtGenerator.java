package com.common.poc;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class JwtGenerator {

    public static void main(String[] args) throws JOSEException, ParseException {
        // Your input string
        String inputString = "shriram";

        // Generate RSA key pair (you may replace this with your own key generation logic)
        KeyPair keyPair = generateRSAKeyPair();

        // Create JWT header
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256).build();

        // Create JWT claims set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(inputString)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 60 * 1000)) // Expires in 1 hour
                .build();

        // Create JWT signed with RSA private key
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        signedJWT.sign(new RSASSASigner((RSAPrivateKey) keyPair.getPrivate()));

        // Serialize to compact form
        String jwtString = signedJWT.serialize();

        // Print the JWT
        System.out.println("JWT: " + jwtString);
        
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        JWSVerifier verifier = new RSASSAVerifier(publicKey);

        // Verify the signature
        if (signedJWT.verify(verifier)) {
            // Signature is valid
            System.out.println("JWT Signature is valid");

            // Print JWT claims
            System.out.println("JWT Claims: " + signedJWT.getJWTClaimsSet());
        } else {
            // Signature is not valid
            System.out.println("JWT Signature is not valid");
        }
        
		/*
		 * System.out.println("Waiting 65 seconds..."); try { Thread.sleep(65_000); }
		 * catch (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * // Verify the signature if (signedJWT.verify(verifier)) { // Signature is
		 * valid System.out.println("JWT Signature is valid");
		 * 
		 * // Print JWT claims System.out.println("JWT Claims: " +
		 * signedJWT.getJWTClaimsSet()); } else { // Signature is not valid
		 * System.out.println("JWT Signature is not valid"); }
		 */
        
        
//        signedJWT.
    }

    // Method to generate RSA key pair
    private static KeyPair generateRSAKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // Key size
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
