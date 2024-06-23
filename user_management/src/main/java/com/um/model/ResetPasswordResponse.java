package com.um.model;

public class ResetPasswordResponse extends ApiResponse{

	private static final String USER_DOESNT_EXIST = "USER_DOESNT_EXIST";
	private static final String INVALID_TOKEN_OR_EMAIL = "INVALID_TOKEN_OR_EMAIL";
	private static final String UNVERIFIED_TOKEN = "TOKEN_NOT_VERIFIED";
	private static final String TOKEN_ALREADY_USED = "TOKEN_ALREADY_USED";
	private static final String TOKEN_EXPIRED = "TOKEN_EXPIRED";
	private static final String PASSWORDS_DOESNT_MATCH = "NEW_PASSWORD_AND_CONFIRM_PASSWORD_DOESNT_MATCH";

	public ResetPasswordResponse(int statusCode, String message) {
		super(statusCode, message);
	}

	public static ResetPasswordResponse userDoesntExist() {
		return new ResetPasswordResponse(403, USER_DOESNT_EXIST);
	}
	
	public static ResetPasswordResponse invalidTokenOrEmail() {
		return new ResetPasswordResponse(403, INVALID_TOKEN_OR_EMAIL);
	}
	
	public static ResetPasswordResponse tokenNotVerified() {
		return new ResetPasswordResponse(403, UNVERIFIED_TOKEN);
	}
	
	public static ResetPasswordResponse tokenAlreadyUsed() {
		return new ResetPasswordResponse(403, TOKEN_ALREADY_USED);
	}
	
	public static ResetPasswordResponse tokenExpired() {
		return new ResetPasswordResponse(403, TOKEN_EXPIRED);
	}
	
	public static ResetPasswordResponse newPasswordAndConfirmPasswordDoesntMatch() {
		return new ResetPasswordResponse(403, PASSWORDS_DOESNT_MATCH);
	}
	
	public static ResetPasswordResponse resetSuccessful() {
		return new ResetPasswordResponse(200, SUCCESS);
	}
	
}
