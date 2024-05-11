package com.accounting.poc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.accounting.model.TransactionRecord;

public class ExcelPoi {
	public static void main(String[] args) {
		try {
			
			Map<String, Integer> columnMapping = new HashMap<String, Integer>();
			
			Workbook workbook = WorkbookFactory.create(new File("/home/life/Downloads/Acct_stmt_Feb-Apr.xlsx"));
			Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet
			// Assuming headers are in the first row
			Row headerRow = sheet.getRow(20);
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
			for(int rowNum = 22; rowNum < sheet.getPhysicalNumberOfRows(); rowNum++) {
				Row row = sheet.getRow(rowNum);
				TransactionRecord record = new TransactionRecord();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YY");
				record.setDate(LocalDate.parse(row.getCell(columnMapping.get("Date")).getStringCellValue(), formatter));
				txnRecords.add(record);
			}
			
//			sheet.getRow
			workbook.close();
		} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}