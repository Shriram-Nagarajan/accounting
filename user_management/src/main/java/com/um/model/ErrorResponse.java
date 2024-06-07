package com.um.model;

public class ErrorResponse extends ApiResponse {

	private String errorRefCode;
	
	public ErrorResponse(int statusCode, String message) {
		super(statusCode, message);
	}
	
	public ErrorResponse(int statusCode, String message, String errorRefCode) {
		this(statusCode, message);
		setErrorRefCode(errorRefCode);
	}

	public String getErrorRefCode() {
		return errorRefCode;
	}

	public void setErrorRefCode(String errorRefCode) {
		this.errorRefCode = errorRefCode;
	}

}
