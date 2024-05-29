package com.accounting.model;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionRecord {
	
	private long transactionId;
	private String txnRefNumber;
	private Date date;
	private String description;
	private boolean creditTxn;
	private BigDecimal amount;
	private boolean reversalTxn;
	
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long l) {
		this.transactionId = l;
	}
	public String getTxnRefNumber() {
		return txnRefNumber;
	}
	public void setTxnRefNumber(String txnRefNumber) {
		this.txnRefNumber = txnRefNumber;
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
	public String toString() {
		return txnRefNumber + " " + date.toString() + " " + description + " " + amount + " " + (creditTxn ? "Cr" : "Dr");
	}
}
