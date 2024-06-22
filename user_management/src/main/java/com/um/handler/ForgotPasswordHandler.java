package com.um.handler;

import com.um.model.ForgotPwdTokenRequest;
import com.um.model.ForgotPwdTokenResponse;

public interface ForgotPasswordHandler {
	
	public final String HANDLER_NAME = "ForgotPasswordHandler";
	
	public ForgotPwdTokenResponse sendToken(ForgotPwdTokenRequest tokenRequest);

}
