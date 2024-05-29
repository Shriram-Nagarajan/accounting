package com.accounting.handler.impl;

import static com.accounting.util.FileUtil.getFileExtension;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.accounting.handler.FileHandler;
import com.accounting.handler.TransactionsHandler;
import com.accounting.model.ExpenseDetails;
import com.accounting.model.TransactionRecord;

@Service
public class SpreadsheetHandlerImpl implements FileHandler{

	private static final String SPREADSHEET_ALLOWED_EXTN = "spreadsheet.allowed.extensions";
	
	private final Environment env;
	
	public SpreadsheetHandlerImpl(Environment env, TransactionsHandler transactionsHandler) {
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
				
				if(Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING) > 0) {
					return "SUCCESS";
				}
				else {
					return "WRITE_ERROR";
				}
			}
		}
		catch (IOException e) {
			System.out.println("Failed to store file."+ e);
			return "UNEXPECTED_ERROR";
		}
	}
	
	@Override
	public List<TransactionRecord> parseData(String filePath) {
		Map<String, Integer> columnMapping = new HashMap<String, Integer>();
		List<TransactionRecord> txnRecords = new ArrayList<TransactionRecord>();

		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(new File(filePath));
			Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet
			// Assuming headers are in the first row
			Row headerRow = sheet.getRow(1);
			if (headerRow != null) {
				int columnCount = headerRow.getLastCellNum();
				for (int i = 0; i < columnCount; i++) {
					Cell cell = headerRow.getCell(i);
					if (cell != null && !cell.getStringCellValue().isBlank()) {
						String header = cell.getStringCellValue();
						System.out.println("Header in column " + (i) + ": " + header);
						columnMapping.put(header, i);
					}
				}
			}

			for (int rowNum = 3; rowNum < sheet.getPhysicalNumberOfRows(); rowNum++) {
				Row row = sheet.getRow(rowNum);

				if(isValidRow(row, columnMapping)) {
					TransactionRecord txnRecord = new TransactionRecord();
					Cell expenseCategory = row.getCell(columnMapping.get("Category"));
					if (expenseCategory != null && expenseCategory.getCellType() == CellType.STRING
							&& !expenseCategory.getStringCellValue().isBlank()) {
						txnRecord = new ExpenseDetails();
						((ExpenseDetails) txnRecord).setCategory(expenseCategory.getStringCellValue());
					}

					Cell dateCell = row.getCell(columnMapping.get("Date"));
					if (dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
						// If it's a date cell, then retrieve the date value
						Date date = dateCell.getDateCellValue();
						LocalDate localDate = date.toInstant()
								.atZone(ZoneId.systemDefault())
								.toLocalDate();
						txnRecord.setDate(localDate);
					} else if (dateCell.getCellType() == CellType.STRING) {
						// If it's a string cell, then parse the string as a date
						String dateString = dateCell.getStringCellValue();
						SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
				        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");

						LocalDate date = LocalDate.parse(dateString, formatter);
						txnRecord.setDate(date);
					} else {
						System.out.println("The cell is not a valid date");
					}
					Cell descCell = row.getCell(columnMapping.get("Narration"));
					txnRecord.setDescription(descCell.getStringCellValue());

					// Format the cell value
					Cell txnRefNumCell = row.getCell(columnMapping.get("Chq./Ref.No."));
					String txnRefNum = txnRefNumCell.getCellType() == CellType.NUMERIC
							? String.valueOf(Double.valueOf(txnRefNumCell.getNumericCellValue()).longValue())
							: txnRefNumCell.getStringCellValue();
					txnRecord.setTxnRefNumber(txnRefNum);

					Cell wdAmtCell = row.getCell(columnMapping.get("Withdrawal Amt."));
					Cell depAmtCell = row.getCell(columnMapping.get("Deposit Amt."));

					boolean creditTxn = depAmtCell != null && depAmtCell.getNumericCellValue() != 0.0;
					boolean debitTxn = wdAmtCell != null && wdAmtCell.getNumericCellValue() != 0.0;

					if (!creditTxn && !debitTxn) {
						System.err.println("Must be a credit or debit txn..row Number: " + rowNum);
					}

					txnRecord.setCreditTxn(creditTxn);
					txnRecord.setAmount(creditTxn ? BigDecimal.valueOf(depAmtCell.getNumericCellValue())
							: BigDecimal.valueOf(wdAmtCell.getNumericCellValue()));

					txnRecord.setReversalTxn(txnRecord.getAmount().doubleValue() < 0.0);

					txnRecords.add(txnRecord);
				}
			}
			txnRecords.stream().forEach(record -> {
				System.out.println(record);
			});


		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			try {
				if(workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return txnRecords;
	}
	
	private boolean isValidRow(Row row, Map<String, Integer> columnMapping) {
		if(row == null) {
			return false;
		}
		Cell txnRefNumCell = row.getCell(columnMapping.get("Chq./Ref.No."));
		if(txnRefNumCell == null) {
			return false;
		}	else {
			String txnRefNum = txnRefNumCell.getCellType() == CellType.NUMERIC
					? String.valueOf(Double.valueOf(txnRefNumCell.getNumericCellValue()).longValue())
					: txnRefNumCell.getStringCellValue();
			if(txnRefNum == null || txnRefNum.isBlank()) {
				return false;
			}
			
			Cell wdAmtCell = row.getCell(columnMapping.get("Withdrawal Amt."));
			Cell depAmtCell = row.getCell(columnMapping.get("Deposit Amt."));

			boolean creditTxn = depAmtCell != null && depAmtCell.getNumericCellValue() != 0.0;
			boolean debitTxn = wdAmtCell != null && wdAmtCell.getNumericCellValue() != 0.0;
			
			if(!creditTxn && !debitTxn) {
				return false;
			}
		}
		return true;
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
