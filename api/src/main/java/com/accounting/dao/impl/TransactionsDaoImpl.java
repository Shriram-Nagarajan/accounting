package com.accounting.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.dao.TransactionsDao;
import com.accounting.entity.ExpenseDetailsEntity;
import com.accounting.entity.TransactionEntity;
import com.accounting.model.CategoryWiseExpense;
import com.accounting.model.TransactionRecord;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Transactional
@Service("transactionsDao")
public class TransactionsDaoImpl implements TransactionsDao {
	
	private final Environment env;
	private final JdbcTemplate accountsTemplate;
	private final NamedParameterJdbcTemplate accountsNamedTemplate;
	
    @PersistenceContext
    private EntityManager entityManager;
    
	public TransactionsDaoImpl(DataSource accountsDataSource,  Environment env) {
		accountsTemplate = new JdbcTemplate(accountsDataSource);
		this.env = env;
		this.accountsNamedTemplate = new NamedParameterJdbcTemplate(accountsDataSource);
	}

	@Override
	@Transactional
	public int saveTransactions(long accountId, List<TransactionRecord> transactionRecordList) {
		int rowsUpdated = 0;
		String query = env.getProperty("save_transactions");
		if(accountId > 0 && transactionRecordList != null && !transactionRecordList.isEmpty() && query != null && !query.isBlank()) {
			int[] updateCount = accountsTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					
					TransactionRecord txnRecord = transactionRecordList.get(i);
					
					int colIdx = 1;
					ps.setString(colIdx++, txnRecord.getTxnRefNumber());
					ps.setLong(colIdx++, accountId);
					Date date = txnRecord.getDate();
					ps.setDate(colIdx++, new java.sql.Date(date.getTime()));
					ps.setString(colIdx++, txnRecord.getDescription());
					ps.setBoolean(colIdx++, txnRecord.isCreditTxn());
					ps.setBigDecimal(colIdx++, txnRecord.getAmount());
					ps.setBoolean(colIdx, txnRecord.isReversalTxn());
					
				}
				
				@Override
				public int getBatchSize() {
					return transactionRecordList.size();
				}
			});
			rowsUpdated = IntStream.of(updateCount).sum();
		}
		return rowsUpdated;
	}

	@Override
	public int deleteTransactions(long accountId) {
		String query = env.getProperty("delete_transactions");
		if(query != null && !query.isBlank() && accountId > 0) {
			return accountsTemplate.update(query, accountId);
		}
		return 0;
	}

	@Override
	public void saveExpenseDetailEntities(List<ExpenseDetailsEntity> expenseEntities) {
		
		expenseEntities.forEach((expenseEntity) -> entityManager.persist(expenseEntity));
		
	}
	
	@Override
	@Transactional
	public List<TransactionEntity> saveTransactionEntities(List<TransactionEntity> transactionEntityList) {
		transactionEntityList.forEach((txnEntity) -> entityManager.persist(txnEntity));
		return transactionEntityList;
	}

	@SuppressWarnings("serial")
	@Override
	public List<CategoryWiseExpense> getCategoryWiseExpenses(long accountId) {
		String query = env.getProperty("get_category_wise_expenses");
		if(accountId > 0 && query != null && !query.isBlank()) {
			var resultList = accountsNamedTemplate.queryForList(query, new HashMap<String, Long> () {
				{put("accountId", accountId);}
			});
			return resultList.stream()
					.map((each) -> {
						CategoryWiseExpense expense = new CategoryWiseExpense();
						expense.setCategoryName(String.valueOf(each.get("category_name")));
						expense.setNumOfExpenses(Integer.parseInt(String.valueOf(each.get("num_expenses"))));
						expense.setTotalExpenditure(Double.parseDouble(String.valueOf(each.get("total_expenditure"))));
						return expense;
					}).toList();
		}
		return null;
	}
	
}
