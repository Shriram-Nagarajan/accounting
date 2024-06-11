package com.accounting.controller;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.handler.TransactionsHandler;
import com.accounting.model.CategoryWiseExpense;
import com.accounting.model.ExpenseDetails;
import com.accounting.model.SaveExpensesRequest;
import com.accounting.model.SaveIncomeDetails;
import com.accounting.model.TransactionRecord;
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
	
	@GetMapping("/income-details")
	public ResponseEntity<List<TransactionRecord>> getIncomeDetails(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate) throws ParseException {
		validateDate(fromDate, toDate);
		return ResponseEntity.ok(transactionsHandler.getIncomeDetails(DEFAULT_ACCOUNT_ID, fromDate, toDate));
	}
	
	
	@PostMapping("/save-expenses")
	public ResponseEntity<String> saveExpenses(@RequestBody SaveExpensesRequest saveExpensesRequest) {
		String reqId = UUID.randomUUID().toString();
		log.info("Request received saveExpenses- reqId: " + reqId);
		if(saveExpensesRequest != null) {
			if(saveExpensesRequest.getDeleteExisting() == null) {
				saveExpensesRequest.setDeleteExisting(false);
			}
		}
		String status = transactionsHandler.saveTransactions(DEFAULT_ACCOUNT_ID,
				ExpenseDetails.getTransactionRecords(saveExpensesRequest.getExpenseList()),
				saveExpensesRequest.getDeleteExisting());
		log.info("Responding to saveExpenses request - reqId: "+ reqId +" with status: " + status);
		return ResponseEntity.ok(status);
	}
	
	@PostMapping("/save-income-details")
	public ResponseEntity<String> saveIncomeDetails(@RequestBody SaveIncomeDetails saveIncomeDetails) {
		String reqId = UUID.randomUUID().toString();
		log.info("Request received saveIncomeDetails- reqId: " + reqId);

		String status = transactionsHandler.saveTransactions(DEFAULT_ACCOUNT_ID,
				saveIncomeDetails.getIncomeDetails(),
				saveIncomeDetails.isDeleteExisting());
		log.info("Responding to saveIncomeDetails request - reqId: "+ reqId +" with status: " + status);
		return ResponseEntity.ok(status);
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
	
	private static final Logger log = LogManager.getLogger(AccountingController.class);
	
}
