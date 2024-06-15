package com.common.exception;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ValidationException(String validationError) {
		super(validationError);
	}

}
