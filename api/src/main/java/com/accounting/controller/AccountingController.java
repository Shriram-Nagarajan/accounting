package com.accounting.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.handler.TransactionsHandler;
import com.accounting.model.CategoryWiseExpense;
import com.accounting.model.ExpenseDetails;
import com.accounting.util.DateUtil;

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
	
	@GetMapping("/expenses")
	public ResponseEntity<List<ExpenseDetails>> getExpenses(
			@RequestParam(value = "categoryId") int categoryId,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate) throws ParseException {
		validateDate(fromDate, toDate);
		return ResponseEntity.ok(transactionsHandler.getExpenses(DEFAULT_ACCOUNT_ID, categoryId,
				fromDate, toDate));
	}
	

	private void validateDate(String fromDateStr, String toDateStr) throws ParseException {
		
		if(fromDateStr == null || fromDateStr.isBlank()) {
			throw new IllegalArgumentException("fromDate parameter is not provided");
		}
		
		if(toDateStr == null || toDateStr.isBlank()) {
			throw new IllegalArgumentException("toDate parameter is not provided");
		}
		
        String dateFormat = env.getProperty("mysql.date.format"); // Expected date format
        
        if(DateUtil.isBefore(toDateStr, fromDateStr, dateFormat)) {
        	throw new IllegalArgumentException("toDate must be after fromDate");
        }

	}
	
}
