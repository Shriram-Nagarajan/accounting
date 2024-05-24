package com.accounting.controller;

import java.text.ParseException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.accounting.model.ErrorResponse;

@ControllerAdvice(basePackages = "com.accounting.controller")
public class ExceptionController {

	@ExceptionHandler(Exception.class) 
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		String errorRef = UUID.randomUUID().toString();
		ErrorResponse errorRsp = new ErrorResponse(500, "INTERNAL_SERVER_ERROR", errorRef);
		System.out.println("Uncaught exception in ExceptionController: Reference: " + errorRef);
		e.printStackTrace();
		return ResponseEntity.internalServerError().body(errorRsp);
	}
	
	@ExceptionHandler(IllegalArgumentException.class) 
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		String errorRef = UUID.randomUUID().toString();
		ErrorResponse errorRsp = new ErrorResponse(400, e.getMessage(), errorRef);
		System.out.println("Uncaught exception in ExceptionController: Reference: " + errorRef);
		e.printStackTrace();
		return ResponseEntity.badRequest().body(errorRsp);
	}
	
	@ExceptionHandler(ParseException.class)
	public ResponseEntity<ErrorResponse> handleParseException(ParseException e) {
		return handleException(e);
	}
	
}
