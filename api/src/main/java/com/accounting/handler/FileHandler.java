package com.accounting.handler;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.accounting.model.TransactionRecord;

public interface FileHandler {

	public String store(MultipartFile file);
	
	public List<TransactionRecord> parseData(String filePath);
	
}
