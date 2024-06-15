package com.accounting.dao;

import java.time.LocalDate;
import java.util.List;

import com.accounting.entity.Category;
import com.accounting.entity.ExpenseDetailsEntity;
import com.accounting.entity.TransactionEntity;
import com.accounting.model.CategoryWiseExpense;
import com.accounting.model.ExpenseDetails;
import com.accounting.model.TransactionRecord;

public interface TransactionsDao {
	
	public int saveTransactions(long accountId, List<TransactionRecord> transactionRecordList);
	
	public int deleteTransactions(long accountId, LocalDate minDate, LocalDate maxDate);
	
	public List<TransactionEntity> saveTransactionEntities(List<TransactionEntity> transactionEntityList);
	
	public void saveExpenseDetailEntities(List<ExpenseDetailsEntity> expenseDetails);
	
	public List<CategoryWiseExpense> getCategoryWiseExpenses(List<Long> accountIds, String fromDate, String toDate);
	
	public List<ExpenseDetails> getExpenses(List<Long> accountIds, int categoryId, String fromDate, String toDate);
	
	public List<TransactionRecord> getIncomeDetails(List<Long> accountIds, String fromDate, String toDate);
	
	public List<Category> getCategoriesForAccounts(List<Long> accountIds);
	
}
