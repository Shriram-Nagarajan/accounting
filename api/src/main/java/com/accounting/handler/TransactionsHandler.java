package com.accounting.handler;

import java.util.List;

import com.accounting.model.CategoryWiseExpense;
import com.accounting.model.TransactionRecord;

public interface TransactionsHandler {
	
	public String saveTransactions(long accountId, List<TransactionRecord> txnRecords);
	
	public List<CategoryWiseExpense> getCategoryWiseExpenses(long accountId);

}
