package com.accounting.model;

import java.util.List;

public class ExpenseDetails extends TransactionRecord {
	
	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public static List<TransactionRecord> getTransactionRecords(List<ExpenseDetails> expList) {
		return expList.stream()
				.map(each -> (TransactionRecord)each)
				.toList();
	}

}
