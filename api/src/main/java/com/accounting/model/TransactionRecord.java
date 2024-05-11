package com.accounting.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionRecord {
	
	private String txnRefNumber;
	private LocalDate date;
	private String description;
	private boolean creditTxn;
	private BigDecimal amount;
	private boolean reversalTxn;
	
	public String getTxnRefNumber() {
		return txnRefNumber;
	}
	public void setTxnRefNumber(String txnRefNumber) {
		this.txnRefNumber = txnRefNumber;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
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
	
}
