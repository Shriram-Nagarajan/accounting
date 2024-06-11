package com.accounting.model;

import java.util.List;

public class SaveIncomeDetails {
	
	private boolean deleteExisting;
	private List<TransactionRecord> incomeDetails;
	
	public boolean isDeleteExisting() {
		return deleteExisting;
	}
	public void setDeleteExisting(boolean deleteExisting) {
		this.deleteExisting = deleteExisting;
	}
	public List<TransactionRecord> getIncomeDetails() {
		return incomeDetails;
	}
	public void setIncomeDetails(List<TransactionRecord> incomeDetails) {
		this.incomeDetails = incomeDetails;
	}
	
}
