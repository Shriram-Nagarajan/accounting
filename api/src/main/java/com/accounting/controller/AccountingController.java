package com.accounting.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.handler.TransactionsHandler;
import com.accounting.model.CategoryWiseExpense;

@RestController
public class AccountingController {

	private static final long DEFAULT_ACCOUNT_ID = 1;

	private final TransactionsHandler transactionsHandler;
	private final Environment env;

	public AccountingController(TransactionsHandler transactionsHandler, Environment env) {
		this.transactionsHandler = transactionsHandler;
		this.env = env;
	}
	
	@GetMapping("/expenses-by-category")
	public ResponseEntity<List<CategoryWiseExpense>> getCategoryWiseExpenses(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate) throws ParseException {
		validateDate(fromDate, toDate);
		return ResponseEntity.ok(transactionsHandler.getCategoryWiseExpenses(DEFAULT_ACCOUNT_ID,
				fromDate, toDate));
	}
	

	private void validateDate(String fromDate, String toDate) throws ParseException {
		
		if(fromDate == null || fromDate.isBlank()) {
			throw new IllegalArgumentException("fromDate parameter is not provided");
		}
		
		if(toDate == null || toDate.isBlank()) {
			throw new IllegalArgumentException("toDate parameter is not provided");
		}
		
        String dateFormat = env.getProperty("mysql.date.format"); // Expected date format
        
        // Create a SimpleDateFormat object with the expected format
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        
        // ParseException to be thrown if given dates are not valid.
		sdf.parse(fromDate);
		sdf.parse(toDate);
				
	}
	
}
