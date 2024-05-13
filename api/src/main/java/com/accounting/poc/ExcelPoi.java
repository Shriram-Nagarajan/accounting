package com.accounting.poc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.accounting.model.TransactionRecord;

public class ExcelPoi {
	public static void main(String[] args) {
		try {
			
			Map<String, Integer> columnMapping = new HashMap<String, Integer>();
			
			Workbook workbook = WorkbookFactory.create(new File("/home/life/Downloads/Stmt_Feb-Apr.xlsx"));
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
			
			List<TransactionRecord> txnRecords = new ArrayList<TransactionRecord>();
			for(int rowNum = 3; rowNum < sheet.getPhysicalNumberOfRows(); rowNum++) {
				Row row = sheet.getRow(rowNum);
				TransactionRecord record = new TransactionRecord();
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YY");
				Cell dateCell = row.getCell(columnMapping.get("Date"));
//				cell.setCellType(CellType.STRING)
	            if (dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
	                // If it's a date cell, then retrieve the date value
	                Date date = dateCell.getDateCellValue();
	                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
	                record.setDate(date);
//	                System.out.println(rowNum+"::Number::Date from Excel: " + date);
	            } else if (dateCell.getCellType() == CellType.STRING) {
	                // If it's a string cell, then parse the string as a date
	                String dateString = dateCell.getStringCellValue();
	                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
	                Date date = dateFormat.parse(dateString);
//	                System.out.println(rowNum+"::String::Date from Excel: " + date);
	                record.setDate(date);
	            } else {
	                System.out.println("The cell is not a valid date");
	            }
	            Cell descCell = row.getCell(columnMapping.get("Narration"));
	            record.setDescription(descCell.getStringCellValue());
	            
	            // Format the cell value
	            Cell txnRefNumCell = row.getCell(columnMapping.get("Chq./Ref.No."));
	            String txnRefNum = txnRefNumCell.getCellType() == CellType.NUMERIC ?
	            		String.valueOf(Double.valueOf(txnRefNumCell.getNumericCellValue()).longValue())
	            		: txnRefNumCell.getStringCellValue();
	            record.setTxnRefNumber(txnRefNum);
	            
	            Cell wdAmtCell = row.getCell(columnMapping.get("Withdrawal Amt."));
	            Cell depAmtCell = row.getCell(columnMapping.get("Deposit Amt."));
	            
	            boolean creditTxn = depAmtCell != null && depAmtCell.getNumericCellValue() != 0.0;
	            boolean debitTxn = wdAmtCell != null && wdAmtCell.getNumericCellValue() != 0.0;
	            
	            if(!creditTxn && !debitTxn) {
	            	System.err.println("Must be a credit or debit txn..");
	            	return;
	            }
	            
	            record.setCreditTxn(creditTxn);
	            record.setAmount(creditTxn ? BigDecimal.valueOf(depAmtCell.getNumericCellValue())
	            		: BigDecimal.valueOf(wdAmtCell.getNumericCellValue()));
	            
	            record.setReversalTxn(record.getAmount().doubleValue() < 0.0);
	            
				txnRecords.add(record);
			}
			txnRecords.stream().forEach(record -> {
				System.out.println(record);
			});
			
			
//			sheet.getRow
			workbook.close();
		} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}