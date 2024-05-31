package com.accounting.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

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
	@Column(name="transaction_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	
	@Column(name = "transaction_ref_num")
	public String txnRefNumber;
	
	@Column(name = "account_id")
	public long accountId;
	
	@Column(name = "transaction_date")
	public LocalDate date;
	
	@Column(name = "description")
	public String description;
	
	@Column(name = "credit_txn")
	public boolean creditTxn;
	
	@Column(name = "amount")
	public BigDecimal amount;
	
	@Column(name = "reversal_txn")
	public boolean reversalTxn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate localDate) {
		this.date = localDate;
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
