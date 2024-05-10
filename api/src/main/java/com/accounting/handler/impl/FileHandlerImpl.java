package com.accounting.handler.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.accounting.handler.FileHandler;

@Service
public class FileHandlerImpl implements FileHandler{

	@Autowired
	private Environment env;
	
	@Override
	public void store(MultipartFile file) {
		
		String destinationPath = env.getProperty("file.storage.destination");
		try {
			if (file.isEmpty()) {
				System.out.println("Failed to store empty file.");
			}
			Path destinationFile = Paths.get(destinationPath ,file.getOriginalFilename())
					.normalize().toAbsolutePath();
			/*
			 * if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath()))
			 * { // This is a security check throw new Exception(
			 * "Cannot store file outside current directory."); }
			 */
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			System.out.println("Failed to store file."+ e);
		}
	}

}
