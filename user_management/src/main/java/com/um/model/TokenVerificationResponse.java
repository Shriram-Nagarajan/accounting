package com.um.model;

public class TokenVerificationResponse extends ApiResponse {

	private static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
	private static final String TOKEN_EXPIRED = "TOKEN_EXPIRED";
	private static final String INVALID_TOKEN = "INVALID_TOKEN";
	private static final String VERIFIED_SUCCESSFULLY = "VERIFIED_SUCCESSFULLY";
	
	public TokenVerificationResponse(int statusCode, String message) {
		super(statusCode, message);
	}
	
	public static TokenVerificationResponse userAlreadyExists() {
		return new TokenVerificationResponse(403, USER_ALREADY_EXISTS);
	}
	
	public static TokenVerificationResponse reqdParamsNotProvided() {
		return new TokenVerificationResponse(400, REQD_PARAMS_NOT_PROVIDED);
	}

	public static TokenVerificationResponse tokenExpired() {
		return new TokenVerificationResponse(403, TOKEN_EXPIRED);
	}
	
	public static TokenVerificationResponse tokenVerifiedSuccessfully() {
		return new TokenVerificationResponse(200, VERIFIED_SUCCESSFULLY);
	}
	
	public static TokenVerificationResponse invalidToken() {
		return new TokenVerificationResponse(403, INVALID_TOKEN);
	}
	
}
