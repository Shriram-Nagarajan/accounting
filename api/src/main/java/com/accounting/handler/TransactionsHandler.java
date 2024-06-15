package com.accounting.handler;

import java.util.List;

import com.accounting.entity.Category;
import com.accounting.model.CategoryWiseExpense;
import com.accounting.model.ExpenseDetails;
import com.accounting.model.TransactionRecord;

public interface TransactionsHandler {
	
	public String saveTransactions(long accountId, List<TransactionRecord> txnRecords, boolean deleteExisting);
	
	public List<CategoryWiseExpense> getCategoryWiseExpenses(List<Long> accountIds, String fromDate, String toDate);
	
	public List<ExpenseDetails> getExpenses(List<Long> accountIds, int categoryId, String fromDate, String toDate);
	
	public List<TransactionRecord> getIncomeDetails(List<Long> accountIds, String fromDate, String toDate);
	
	public List<Category> getCategoriesForAccounts(List<Long> accountIds);

}
