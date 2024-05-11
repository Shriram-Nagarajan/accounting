package com.accounting.handler;

import org.springframework.web.multipart.MultipartFile;

public interface FileHandler {

	public String store(MultipartFile file);
	
}
