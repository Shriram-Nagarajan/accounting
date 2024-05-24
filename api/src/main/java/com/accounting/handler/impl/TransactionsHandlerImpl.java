package com.accounting.handler.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.accounting.dao.TransactionsDao;
import com.accounting.entity.Category;
import com.accounting.entity.ExpenseDetailsEntity;
import com.accounting.entity.TransactionEntity;
import com.accounting.handler.TransactionsHandler;
import com.accounting.model.CategoryWiseExpense;
import com.accounting.model.ExpenseDetails;
import com.accounting.model.TransactionRecord;
import com.accounting.repository.CategoryRepository;

@Service("transactionsHandler")
public class TransactionsHandlerImpl implements TransactionsHandler{
	
	private final TransactionsDao transactionsDao;
	private final CategoryRepository categoryRepository;
	
	public TransactionsHandlerImpl(TransactionsDao transactionsDao, CategoryRepository categoryRepository) {
		this.transactionsDao = transactionsDao;
		this.categoryRepository = categoryRepository;
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
				txnEntityList.stream()
				.filter(t -> t.getId() > 0)
				.toList().size() == txnRecords.size()) {
			List<ExpenseDetailsEntity> expenseDetailsEntities = new ArrayList<>();
			for (int i = 0; i < txnRecords.size(); i++) {
				TransactionRecord txnRecord = txnRecords.get(i);
				if (txnRecord instanceof ExpenseDetails expenseDetails && !((ExpenseDetails) txnRecord).getCategory().isBlank()) {
					ExpenseDetailsEntity expenseEntity = new ExpenseDetailsEntity();
					expenseEntity.setId(txnEntityList.get(i).getId());
					expenseEntity.setCategoryId(getCategoryId(expenseDetails.getCategory()));
					expenseDetailsEntities.add(expenseEntity);
				}
			}
			transactionsDao.saveExpenseDetailEntities(expenseDetailsEntities);
		}	else {
			return "SAVING_TXN_FAILED";
		}
		return "SUCCESS";
	}	
	
	private int getCategoryId(String categoryName) {
		if(categoryName != null && !categoryName.isBlank()) {
			List<Category> categoryList = categoryRepository.findByCategoryName(categoryName);
			
			// Populate if category is not present and then return the category id
			return (Optional.ofNullable(categoryList).isPresent() && !categoryList.isEmpty()) ? categoryList.get(0).getId() :
				categoryRepository.save(new Category(categoryName)).getId();
		}
		return 0;
	}

	@Override
	public List<CategoryWiseExpense> getCategoryWiseExpenses(long accountId, String fromDate, String toDate) {
		return transactionsDao.getCategoryWiseExpenses(accountId, fromDate, toDate);
	}
	
}
