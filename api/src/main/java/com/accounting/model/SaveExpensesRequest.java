package com.accounting.model;

import java.util.List;

public class SaveExpensesRequest {

	private Boolean deleteExisting;
	private List<ExpenseDetails> expenseList;
	
	public Boolean getDeleteExisting() {
		return deleteExisting;
	}
	public void setDeleteExisting(Boolean deleteExisting) {
		this.deleteExisting = deleteExisting;
	}
	public List<ExpenseDetails> getExpenseList() {
		return expenseList;
	}
	public void setExpenseList(List<ExpenseDetails> expenseList) {
		this.expenseList = expenseList;
	}
	
}
