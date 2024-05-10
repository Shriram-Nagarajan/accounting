package com.accounting.handler;

import org.springframework.web.multipart.MultipartFile;

public interface FileHandler {

	public void store(MultipartFile file);
	
}
