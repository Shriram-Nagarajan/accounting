package com.accounting.dao.impl;

import java.util.List;

import com.accounting.dao.TransactionsDao;
import com.accounting.model.TransactionRecord;

public class TransactionsDaoImpl implements TransactionsDao {
	
	
	public TransactionsDaoImpl() {}

	@Override
	public int saveTransactions(long accountId, List<TransactionRecord> transactionRecordList) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<TransactionRecord> getTransaction(long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

}
