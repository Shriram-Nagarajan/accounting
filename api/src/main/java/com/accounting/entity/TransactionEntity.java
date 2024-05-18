package com.accounting.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.accounting.model.TransactionRecord;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long transactionId;
	
	@Column(name = "transaction_ref_num")
	private String txnRefNumber;
	
	@Column(name = "account_id")
	private long accountId;
	
	@Column(name = "transaction_date")
	private Date date;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "credit_txn")
	private boolean creditTxn;
	
	@Column(name = "amount")
	private BigDecimal amount;
	
	@Column(name = "reversal_txn")
	private boolean reversalTxn;

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getTxnRefNumber() {
		return txnRefNumber;
	}

	public void setTxnRefNumber(String txnRefNumber) {
		this.txnRefNumber = txnRefNumber;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCreditTxn() {
		return creditTxn;
	}

	public void setCreditTxn(boolean creditTxn) {
		this.creditTxn = creditTxn;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public boolean isReversalTxn() {
		return reversalTxn;
	}

	public void setReversalTxn(boolean reversalTxn) {
		this.reversalTxn = reversalTxn;
	}
	
	public static TransactionEntity getEntity(long accountId, TransactionRecord txnRecord) {
		
		TransactionEntity entity = new TransactionEntity();
		entity.setTxnRefNumber(txnRecord.getTxnRefNumber());
		entity.setAccountId(accountId);
		entity.setDate(txnRecord.getDate());
		entity.setDescription(txnRecord.getDescription());
		entity.setCreditTxn(txnRecord.isCreditTxn());
		entity.setAmount(txnRecord.getAmount());
		entity.setReversalTxn(txnRecord.isReversalTxn());
		return entity;
		
	}
	
}
