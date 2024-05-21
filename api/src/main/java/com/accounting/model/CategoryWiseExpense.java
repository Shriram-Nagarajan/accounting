package com.accounting.model;

public class CategoryWiseExpense {
	
	private String categoryName;
	private int numOfExpenses;
	private double totalExpenditure;
	
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
