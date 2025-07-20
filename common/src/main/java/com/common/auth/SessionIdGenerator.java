package com.common.auth;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class SessionIdGenerator {

	private static SessionIdGenerator sessionIdGenerator;
	private KeyPair keyPair;
	
	private SessionIdGenerator() {
		generateRSAKeyPair();
	}
	
	private static synchronized SessionIdGenerator getSessionIdGenerator() {
		if(sessionIdGenerator == null) {
			sessionIdGenerator = new SessionIdGenerator();
		}
		return sessionIdGenerator;
	}
			
	public static String createSessionId(String entityIdentifier) {
		/*
		 * SessionIdGenerator sessionIdGenerator = getSessionIdGenerator();
		 * 
		 * // Create JWT header JWSHeader header = new
		 * JWSHeader.Builder(JWSAlgorithm.RS256).build();
		 * 
		 * // Create JWT claims set JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
		 * .subject(entityIdentifier) .issueTime(new Date()) .expirationTime(new
		 * Date(System.currentTimeMillis() + 3600 * 1000)) // TODO: Move it to
		 * constants! .build();
		 * 
		 * // Create JWT signed with RSA private key SignedJWT signedJWT = new
		 * SignedJWT(header, claimsSet); try { signedJWT.sign(new
		 * RSASSASigner((RSAPrivateKey) sessionIdGenerator.keyPair.getPrivate())); }
		 * catch (JOSEException e) { e.printStackTrace(); }
		 * 
		 * // Serialize to compact form String jwtString = signedJWT.serialize();
		 * 
		 * return jwtString;
		 */
		return UUID.randomUUID().toString();
	}
	
	public static boolean isValidSessionId(String sessionId) {

		/*
		 * try { SignedJWT signedJWT = SignedJWT.parse(sessionId); SessionIdGenerator
		 * sessionIdGenerator = getSessionIdGenerator(); RSAPublicKey publicKey =
		 * (RSAPublicKey) sessionIdGenerator.keyPair.getPublic(); JWSVerifier verifier =
		 * new RSASSAVerifier(publicKey); return signedJWT.verify(verifier); } catch
		 * (ParseException | JOSEException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); return false; }
		 */
		return sessionId != null && !sessionId.trim().isEmpty();
        
	}
	
	// Generate key pair
    private void generateRSAKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // Key size
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
}
