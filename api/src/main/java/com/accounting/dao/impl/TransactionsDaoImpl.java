package com.accounting.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.dao.TransactionsDao;
import com.accounting.model.TransactionRecord;

@Service("transactionsDao")
public class TransactionsDaoImpl implements TransactionsDao {
	
	private final Environment env;
	private final JdbcTemplate accountsTemplate;
	
	public TransactionsDaoImpl(DataSource accountsDataSource, Environment env) {
		accountsTemplate = new JdbcTemplate(accountsDataSource);
		this.env = env;
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
	public List<TransactionRecord> getTransaction(long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("serial")
	@Override
	@Transactional
	public int deleteTransactions(long accountId) {
		String query = env.getProperty("delete_transactions");
		if(query != null && !query.isBlank() && accountId > 0) {
			return accountsTemplate.update(query, accountId);
		}
		return 0;
	}

}
