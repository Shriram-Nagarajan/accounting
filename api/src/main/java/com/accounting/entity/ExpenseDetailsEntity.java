package com.accounting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "expense_details")
public class ExpenseDetailsEntity {

	@Id
	@Column(name="transaction_id")
	private long id;
	
	@Column(name = "category")
	private String category;

	public long getId() {
		return id;
	}

	public void setId(long transactionId) {
		this.id = transactionId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
}
