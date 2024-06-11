package com.accounting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private int id;

	@Column(name = "category_name")
	private String categoryName;
	
	@Column(name = "is_default")
	private boolean isDefaultCategory;

	public Category() {
	}

	public Category(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public boolean isDefaultCategory() {
		return isDefaultCategory;
	}
	
	public void setDefaultCategory(boolean isDefaultCategory) {
		this.isDefaultCategory = isDefaultCategory;
	}
	
}
