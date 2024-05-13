package com.accounting.dao;

import java.util.List;

import com.accounting.model.TransactionRecord;

public interface TransactionsDao {
	
	public int saveTransactions(long accountId, List<TransactionRecord> transactionRecordList);
	
	public List<TransactionRecord> getTransaction(long accountId);

}
