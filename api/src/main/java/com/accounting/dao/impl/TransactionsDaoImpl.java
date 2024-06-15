package com.accounting.dao.impl;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.dao.TransactionsDao;
import com.accounting.entity.Category;
import com.accounting.entity.ExpenseDetailsEntity;
import com.accounting.entity.TransactionEntity;
import com.accounting.model.CategoryWiseExpense;
import com.accounting.model.ExpenseDetails;
import com.accounting.model.TransactionRecord;
import com.accounting.util.DateUtil;

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
					LocalDate date = txnRecord.getDate();
					ps.setDate(colIdx++, java.sql.Date.valueOf(date));
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
	public int deleteTransactions(long accountId, LocalDate fromDate, LocalDate toDate) {
		String query = env.getProperty("delete_transactions");
		if(query != null && !query.isBlank() && accountId > 0) {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("accountId", accountId);
			paramMap.put("fromDate", fromDate);
			paramMap.put("toDate", toDate);
			return accountsNamedTemplate.update(query, paramMap); 
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
		int count = 0;
		int batchSize = Integer.parseInt(env.getProperty("entity.transaction.batch-size"));
		// save entities batch-wise
		for(TransactionEntity txnEntity : transactionEntityList) {
			entityManager.persist(txnEntity);
			count++;
			if(count % batchSize == 0) {
				entityManager.flush();
				entityManager.clear();
			}
		}
		return transactionEntityList;
	}

	@SuppressWarnings("serial")
	@Override
	public List<CategoryWiseExpense> getCategoryWiseExpenses(List<Long> accountIds, String fromDate, String toDate) {
		String query = env.getProperty("get_category_wise_expenses");
		if(accountIds.size() > 0 && query != null && !query.isBlank()) {
			var resultList = accountsNamedTemplate.queryForList(query, new HashMap<> () {
				{put("accountId", accountIds);};
				{put("fromDate", fromDate);};
				{put("toDate", toDate);};
			});
			return resultList.stream()
					.map((each) -> {
						CategoryWiseExpense expense = new CategoryWiseExpense();
						expense.setCategoryId(Integer.parseInt(String.valueOf(each.get("category_id"))));
						expense.setCategoryName(String.valueOf(each.get("category_name")));
						expense.setNumOfExpenses(Integer.parseInt(String.valueOf(each.get("num_expenses"))));
						expense.setTotalExpenditure(Double.parseDouble(String.valueOf(each.get("total_expenditure"))));
						return expense;
					}).toList();
		}
		return null;
	}

	@Override
	public List<ExpenseDetails> getExpenses(List<Long> accountIds, int categoryId, String fromDate, String toDate) {
		String query = env.getProperty("get_expenses");
		if(query != null && !query.isBlank() && fromDate != null && toDate != null) {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("accountId", accountIds);
			paramMap.put("categoryId", categoryId);
			paramMap.put("fromDate", fromDate);
			paramMap.put("toDate", toDate);
			var resultList = accountsNamedTemplate.queryForList(query, paramMap);
			return resultList.stream()
					.map((each) -> {
						ExpenseDetails expenseDetails = new ExpenseDetails();
						expenseDetails.setTransactionId(Long.parseLong(String.valueOf(each.get("transaction_id"))));
						try {
							expenseDetails.setDate(
									DateUtil.getDate(
											String.valueOf(each.get("transaction_date")).substring(0, 10),
											env.getProperty("mysql.date.format")));
						} catch (DateTimeParseException e) {
							log.error("Exception occurred while parsing transaction date: ", e);
						}
						expenseDetails.setDescription(each.get("description") != null && !String.valueOf(each.get("description")).isBlank() ?
									String.valueOf(each.get("description")) : null);
						expenseDetails.setAmount(BigDecimal.valueOf(Double.parseDouble(String.valueOf(each.get("amount")))));
						expenseDetails.setTxnRefNumber(each.get("transaction_ref_num") != null && !String.valueOf(each.get("transaction_ref_num")).isBlank() ?
								String.valueOf(each.get("transaction_ref_num")): null);
						expenseDetails.setCategory(String.valueOf(each.get("category_name")));
						return expenseDetails;
					}).toList();
		}
		return List.of();
	}

	@Override
	public List<TransactionRecord> getIncomeDetails(List<Long> accountIds, String fromDate, String toDate) {
		
		String query = env.getProperty("get_income_details");
		
		if(query != null && !query.isBlank()) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("accountId", accountIds);
			paramMap.put("fromDate", fromDate);
			paramMap.put("toDate", toDate);
			List<Map<String, Object>> resultList = accountsNamedTemplate.queryForList(query, paramMap);
			return resultList.stream()
					.map((each) -> {
						TransactionRecord incomeDetails = new TransactionRecord();
						incomeDetails.setTransactionId(Long.parseLong(String.valueOf(each.get("transaction_id"))));
						try {
							incomeDetails.setDate(
									DateUtil.getDate(
											String.valueOf(each.get("transaction_date")).substring(0, 10),
											env.getProperty("mysql.date.format")));
						} catch (DateTimeParseException e) {
							log.error("Exception occurred while parsing transaction date: ", e);
						}
						incomeDetails.setDescription(each.get("description") != null && !String.valueOf(each.get("description")).isBlank() ?
									String.valueOf(each.get("description")) : null);
						incomeDetails.setAmount(BigDecimal.valueOf(Double.parseDouble(String.valueOf(each.get("amount")))));
						incomeDetails.setTxnRefNumber(each.get("transaction_ref_num") != null && !String.valueOf(each.get("transaction_ref_num")).isBlank() ?
								String.valueOf(each.get("transaction_ref_num")): null);
						return incomeDetails;
					})
					.toList();
		}
		
		return List.of();
	}
	
	public List<Category> getCategoriesForAccounts(List<Long> accountIds) {
		String query = env.getProperty("get_user_categories");
		if(accountIds != null && accountIds.size() > 0 && query != null) {
			Map<String, List<Long>> paramMap = new HashMap<String, List<Long>> () ;
			paramMap.put("accountId", accountIds);
			return accountsNamedTemplate.queryForList(query, paramMap)
						.stream()
						.map((map) -> {
							Category category = new Category();
							category.setId(Integer.parseInt(String.valueOf(map.get("category_id"))));
							category.setCategoryName(String.valueOf(map.get("category_name")));
							category.setDefaultCategory(false);
							return category;
						}).toList();
		}
		return List.of();
	}
	
	private static final Logger log = LogManager.getLogger(TransactionsDaoImpl.class);
	
}
