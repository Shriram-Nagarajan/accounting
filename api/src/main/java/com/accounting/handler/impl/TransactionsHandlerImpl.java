package com.accounting.handler.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.dao.TransactionsDao;
import com.accounting.entity.ExpenseDetailsEntity;
import com.accounting.entity.TransactionEntity;
import com.accounting.handler.TransactionsHandler;
import com.accounting.model.ExpenseDetails;
import com.accounting.model.TransactionRecord;

@Service("transactionsHandler")
public class TransactionsHandlerImpl implements TransactionsHandler{
	
	private final TransactionsDao transactionsDao;
	
	public TransactionsHandlerImpl(TransactionsDao transactionsDao) {
		this.transactionsDao = transactionsDao;
	}

	@Override
	public String saveTransactions(long accountId, List<TransactionRecord> txnRecords) {
		transactionsDao.deleteTransactions(accountId);
		List<TransactionEntity> txnEntityList = txnRecords
				.stream()
				.map((txnRecord) -> TransactionEntity.getEntity(accountId, txnRecord))
				.toList();
		txnEntityList = transactionsDao.saveTransactionEntities(txnEntityList);
		if (txnEntityList != null && 
				txnEntityList.stream().filter(t -> t.getTransactionId() > 0).toList().size() == txnRecords.size()) {
			List<ExpenseDetailsEntity> expenseDetailsEntities = new ArrayList<ExpenseDetailsEntity>();
			for (int i = 0; i < txnRecords.size(); i++) {
				TransactionRecord txnRecord = txnRecords.get(i);
				if (txnRecord instanceof ExpenseDetails && !((ExpenseDetails) txnRecord).getCategory().isBlank()) {
					ExpenseDetailsEntity expenseEntity = new ExpenseDetailsEntity();
					expenseEntity.setTransactionId(txnEntityList.get(i).getTransactionId());
					expenseEntity.setCategory(((ExpenseDetails) txnRecord).getCategory());
					expenseDetailsEntities.add(expenseEntity);
				}
			}
			transactionsDao.saveExpenseDetailEntities(expenseDetailsEntities);
		}	else {
			return "SAVING_TXN_FAILED";
		}
		return "SUCCESS";
	}	
	
}
