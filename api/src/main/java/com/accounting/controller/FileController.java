package com.accounting.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.env.Environment;
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
import com.accounting.handler.TransactionsHandler;
import com.accounting.model.ApiResponse;
import com.accounting.model.TransactionRecord;

@RestController
public class FileController {

	private final FileHandler fileHandler;
	
	private final TransactionsHandler transactionsHandler;
	
	private final Environment env;
	
	public FileController(FileHandler fileHandler, TransactionsHandler transactionsHandler,
			Environment env) {
		this.fileHandler = fileHandler;
		this.transactionsHandler = transactionsHandler;
		this.env = env;
	}
	
	private static final long DEFAULT_ACCOUNT_ID = 1;
	
	@PostMapping("/upload")
	public ResponseEntity<ApiResponse> upload(@RequestParam("file") MultipartFile file) {

		String uploadStatus = fileHandler.store(file);
		
		if("SUCCESS".equals(uploadStatus)) {
			String destinationPath = env.getProperty("file.storage.destination");
			Path destinationFile = Paths.get(destinationPath ,file.getOriginalFilename())
					.normalize().toAbsolutePath();
			List<TransactionRecord> txnRecords = fileHandler.parseData(destinationFile.toString());
			if(txnRecords == null || txnRecords.isEmpty()) {
				uploadStatus = "NO_VALID_RECORDS_FOUND_IN_FILE";
			}	else {
				uploadStatus = transactionsHandler.saveTransactions(DEFAULT_ACCOUNT_ID, txnRecords, true);
			}
		}
		return ResponseEntity.ok(new ApiResponse(uploadStatus));
		
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
