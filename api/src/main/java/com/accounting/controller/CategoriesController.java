package com.accounting.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.entity.Category;
import com.accounting.handler.TransactionsHandler;
import com.accounting.model.AccountingUser;
import com.accounting.repository.CategoryRepository;
import com.common.auth.UserThreadLocal;

@RestController
public class CategoriesController {

	private CategoryRepository categoryRepository;
	private TransactionsHandler transactionsHandler;
	
	public CategoriesController(CategoryRepository repository, TransactionsHandler transactionsHandler) {
		this.categoryRepository = repository;
		this.transactionsHandler = transactionsHandler;
	}
	
	@GetMapping("/category/default")
	public ResponseEntity<List<Category>> getDefaultCategories() {
		List<Category> categoryList = categoryRepository.findByIsDefaultCategory(true);
		return ResponseEntity.ok(categoryList);
	}
	
	@GetMapping("/category/user")
	public ResponseEntity<List<Category>> getUserCategories() {
		UserThreadLocal<AccountingUser> threadLocal = new UserThreadLocal<AccountingUser>();
		List<Category> categoryList = transactionsHandler.getCategoriesForAccounts(threadLocal.get().getUserAccounts());
		return ResponseEntity.ok(categoryList);
	}
	
}
