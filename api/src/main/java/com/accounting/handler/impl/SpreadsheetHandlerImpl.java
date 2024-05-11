package com.accounting.handler.impl;

import static com.accounting.util.FileUtil.getFileExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.accounting.handler.FileHandler;

@Service
public class SpreadsheetHandlerImpl implements FileHandler{

	private static final String SPREADSHEET_ALLOWED_EXTN = "spreadsheet.allowed.extensions";
	
	private final Environment env;
	
	public SpreadsheetHandlerImpl(Environment env) {
		this.env = env;
	}
	
	@Override
	public String store(MultipartFile file) {
		
		String destinationPath = env.getProperty("file.storage.destination");
		try {
			if(!isAllowed(file)) {
				return "EXTN_NOT_ALLOWED";
			}
			if (file.isEmpty()) {
				return "EMPTY_FILE";
			}
			Path destinationFile = Paths.get(destinationPath ,file.getOriginalFilename())
					.normalize().toAbsolutePath();
			try (InputStream inputStream = file.getInputStream()) {
				return Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING) > 0 ? "SUCCESS" : "WRITE_ERROR";
			}
		}
		catch (IOException e) {
			System.out.println("Failed to store file."+ e);
			return "UNEXPECTED_ERROR";
		}
	}
	
	private boolean isAllowed(MultipartFile file) {
		var allowedExtn = getAllowedExtensions();
		return !(allowedExtn == null || allowedExtn.isEmpty()) && 
			allowedExtn.contains(getFileExtension(file.getOriginalFilename()));
	}
	
	private List<String> getAllowedExtensions() {
		String allowedExtensions = env.getProperty(SPREADSHEET_ALLOWED_EXTN);
		return allowedExtensions == null ? List.of() :
					Arrays.asList(allowedExtensions.split(","));
	}

}
