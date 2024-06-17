package com.um.model;

public class TokenInitiatorResponse extends ApiResponse {

	private static final String TOKEN_SENT = "TOKEN_SENT";
	private static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
	private static final String EMAIL_ID_EMPTY = "EMAIL_ID_EMPTY";
	
	public TokenInitiatorResponse(int statusCode, String message) {
		super(statusCode, message);
	}
	
	public static TokenInitiatorResponse tokenSent() {
		return new TokenInitiatorResponse(200, TOKEN_SENT);
	}
	
	public static TokenInitiatorResponse userAlreadyExists() {
		return new TokenInitiatorResponse(403, USER_ALREADY_EXISTS);
	}
	
	public static TokenInitiatorResponse emailIdEmpty() {
		return new TokenInitiatorResponse(400, EMAIL_ID_EMPTY);
	}
	
	public static TokenInitiatorResponse reqdParametersNotProvided() {
		return new TokenInitiatorResponse(400, REQD_PARAMS_NOT_PROVIDED);
	}

}
