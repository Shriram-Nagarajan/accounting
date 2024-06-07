package com.um.model;

public class ValidationResponse extends ApiResponse {
	
	private boolean isValid;

	public ValidationResponse(boolean isValid, String validationError, int statusCode) {
		super(statusCode, validationError);
		this.isValid = isValid;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

}
