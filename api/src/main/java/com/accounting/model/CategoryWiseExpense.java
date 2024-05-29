package com.accounting.model;

public class CategoryWiseExpense {
	
	private int categoryId;
	private String categoryName;
	private int numOfExpenses;
	private double totalExpenditure;
	
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public int getNumOfExpenses() {
		return numOfExpenses;
	}
	public void setNumOfExpenses(int numOfExpenses) {
		this.numOfExpenses = numOfExpenses;
	}
	public double getTotalExpenditure() {
		return totalExpenditure;
	}
	public void setTotalExpenditure(double totalExpenditure) {
		this.totalExpenditure = totalExpenditure;
	}

}
