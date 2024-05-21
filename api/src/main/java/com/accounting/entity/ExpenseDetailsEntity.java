package com.accounting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "expense_details")
public class ExpenseDetailsEntity {

	@Id
	@Column(name="transaction_id")
	private long id;
	
	@Column(name = "category_id")
	private int categoryId;

	public long getId() {
		return id;
	}

	public void setId(long transactionId) {
		this.id = transactionId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
}
