package com.accounting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.accounting.handler.FileHandler;
import com.accounting.model.ApiResponse;

@RestController
public class FileUploadController {

	@Autowired
	private FileHandler fileHandler;
	
	@PostMapping("/upload")
	public ResponseEntity<ApiResponse> handleFileUpload(@RequestParam("file") MultipartFile file) {

		String fileStatus = fileHandler.store(file);
		return ResponseEntity.ok(new ApiResponse(fileStatus));
		
	}
	
}
