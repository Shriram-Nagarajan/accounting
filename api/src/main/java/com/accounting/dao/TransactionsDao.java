package com.accounting.dao;

import java.util.List;

import com.accounting.entity.ExpenseDetailsEntity;
import com.accounting.entity.TransactionEntity;
import com.accounting.model.TransactionRecord;

public interface TransactionsDao {
	
	public int saveTransactions(long accountId, List<TransactionRecord> transactionRecordList);
	
	public int deleteTransactions(long accountId);
	
	public List<TransactionRecord> getTransaction(long accountId);
	
	public List<TransactionEntity> saveTransactionEntities(List<TransactionEntity> transactionEntityList);
	
	public void saveExpenseDetailEntities(List<ExpenseDetailsEntity> expenseDetails);

}
