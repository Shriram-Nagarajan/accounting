package com.accounting.handler;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.accounting.model.TransactionRecord;
import com.common.exception.ValidationException;

public interface FileHandler {

	public String store(MultipartFile file);
	
	public List<TransactionRecord> parseData(String filePath) throws ValidationException;
	
}
