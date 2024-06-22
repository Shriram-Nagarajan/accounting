package com.um.model;

public class ForgotPwdTokenResponse extends ApiResponse {
	
	private static final String USER_DOESNT_EXIST = "USER_DOESNT_EXIST";
	private static final String TOKEN_SENT = "TOKEN_SENT";


	public ForgotPwdTokenResponse(int statusCode, String message) {
		super(statusCode, message);
	}
	
	public static ForgotPwdTokenResponse userDoesntExist() {
		return new ForgotPwdTokenResponse(403, USER_DOESNT_EXIST);
	}
	
	public static ForgotPwdTokenResponse reqdParamsNotProvided() {
		return new ForgotPwdTokenResponse(400, REQD_PARAMS_NOT_PROVIDED);
	}
	
	public static ForgotPwdTokenResponse tokenSent() {
		return new ForgotPwdTokenResponse(200, TOKEN_SENT);
	}

}
