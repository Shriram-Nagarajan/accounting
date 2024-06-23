package com.um.handler;

import com.um.model.ForgotPwdTokenRequest;
import com.um.model.ForgotPwdTokenResponse;
import com.um.model.ForgotPwdTokenVerificationResponse;
import com.um.model.ResetPasswordRequest;
import com.um.model.ResetPasswordResponse;
import com.um.model.VerifyForgotPwdTokenRequest;

public interface ForgotPasswordHandler {
	
	public final String HANDLER_NAME = "ForgotPasswordHandler";
	
	public ForgotPwdTokenResponse sendToken(ForgotPwdTokenRequest tokenRequest);
	
	public ForgotPwdTokenVerificationResponse verifyToken(VerifyForgotPwdTokenRequest pwdTokenRequest);
	
	public ResetPasswordResponse resetPassword(ResetPasswordRequest request);

}
