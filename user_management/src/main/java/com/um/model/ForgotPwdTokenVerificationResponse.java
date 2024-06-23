package com.um.model;

public class ForgotPwdTokenVerificationResponse extends ApiResponse {

	private static final String USER_DOESNT_EXIST = "USER_DOESNT_EXIST";
	private static final String TOKEN_EXPIRED = "TOKEN_EXPIRED";
	private static final String TOKEN_ALREADY_USED = "TOKEN_ALREADY_USED";
	private static final String INVALID_TOKEN_OR_EMAIL = "INVALID_TOKEN_OR_EMAIL";
	private static final String VERIFIED_SUCCESSFULLY = "VERIFIED_SUCCESSFULLY";
	
	public ForgotPwdTokenVerificationResponse(int statusCode, String message) {
		super(statusCode, message);
	}
	
	public static ForgotPwdTokenVerificationResponse userDoesntExist() {
		return new ForgotPwdTokenVerificationResponse(403, USER_DOESNT_EXIST);
	}
	
	public static ForgotPwdTokenVerificationResponse reqdParamsNotProvided() {
		return new ForgotPwdTokenVerificationResponse(400, REQD_PARAMS_NOT_PROVIDED);
	}

	public static ForgotPwdTokenVerificationResponse tokenExpired() {
		return new ForgotPwdTokenVerificationResponse(403, TOKEN_EXPIRED);
	}
	
	public static ForgotPwdTokenVerificationResponse tokenAlreadyUsed() {
		return new ForgotPwdTokenVerificationResponse(403, TOKEN_ALREADY_USED);
	}
	
	public static ForgotPwdTokenVerificationResponse tokenVerifiedSuccessfully() {
		return new ForgotPwdTokenVerificationResponse(200, VERIFIED_SUCCESSFULLY);
	}
	
	public static ForgotPwdTokenVerificationResponse invalidTokenOrEmail() {
		return new ForgotPwdTokenVerificationResponse(403, INVALID_TOKEN_OR_EMAIL);
	}
	
}
