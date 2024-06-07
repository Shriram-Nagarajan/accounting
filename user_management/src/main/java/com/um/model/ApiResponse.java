package com.um.model;

public class ApiResponse {
	
	public static final String SUCCESS = "SUCCESS";
	
	private int statusCode;
	private String message;
	
	public ApiResponse(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}
	
	public ApiResponse(String message) {
		this(200, message);
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
