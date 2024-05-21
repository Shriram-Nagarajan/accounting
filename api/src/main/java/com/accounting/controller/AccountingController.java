package com.accounting.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.handler.TransactionsHandler;
import com.accounting.model.CategoryWiseExpense;

@RestController
public class AccountingController {

	private final TransactionsHandler transactionsHandler;
	private static final long DEFAULT_ACCOUNT_ID = 1;
	
	public AccountingController(TransactionsHandler transactionsHandler) {
		this.transactionsHandler = transactionsHandler;
	}
	
	@GetMapping("/expenses-by-category")
	public ResponseEntity<List<CategoryWiseExpense>> getCategoryWiseExpenses() {
		return ResponseEntity.ok(transactionsHandler.getCategoryWiseExpenses(DEFAULT_ACCOUNT_ID));
	}
	
}
