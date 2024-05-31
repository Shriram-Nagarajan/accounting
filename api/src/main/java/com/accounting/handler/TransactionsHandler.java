package com.accounting.handler;

import java.util.List;

import com.accounting.model.CategoryWiseExpense;
import com.accounting.model.ExpenseDetails;
import com.accounting.model.TransactionRecord;

public interface TransactionsHandler {
	
	public String saveTransactions(long accountId, List<TransactionRecord> txnRecords, boolean deleteExisting);
	
	public List<CategoryWiseExpense> getCategoryWiseExpenses(long accountId, String fromDate, String toDate);
	
	public List<ExpenseDetails> getExpenses(long accountId, int categoryId, String fromDate, String toDate);

}
