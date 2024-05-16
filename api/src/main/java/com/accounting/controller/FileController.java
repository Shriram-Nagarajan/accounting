package com.accounting.controller;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.accounting.handler.FileHandler;
import com.accounting.model.ApiResponse;

@RestController
public class FileController {

	@Autowired
	private FileHandler fileHandler;
	
	@PostMapping("/upload")
	public ResponseEntity<ApiResponse> upload(@RequestParam("file") MultipartFile file) {

		String fileStatus = fileHandler.store(file);
		return ResponseEntity.ok(new ApiResponse(fileStatus));
		
	}
	
	@GetMapping("/template/excel/download")
	public ResponseEntity<byte[]> downloadExcelTemplate() {
        // Access the file from src/main/resources
        Resource resource = new ClassPathResource("template.xlsx");
        
        // Read the file content into a byte array
        byte[] fileContent = null;
        HttpHeaders headers = new HttpHeaders();
		try {
			fileContent = Files.readAllBytes(resource.getFile().toPath());
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "template.xlsx");
			
			// Return the file content in the response body along with appropriate headers
		} catch (IOException e) {
			e.printStackTrace();
		}//resource.getFile().toPath());
		return ResponseEntity.ok()
				.headers(headers)
				.body(fileContent);

		
	}
	
}
