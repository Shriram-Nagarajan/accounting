package com.accounting.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.entity.Category;
import com.accounting.model.AccountingUser;
import com.accounting.repository.CategoryRepository;
import com.common.auth.UserThreadLocal;

@RestController
public class CategoriesController {

	private CategoryRepository categoryRepository;
	
	public CategoriesController(CategoryRepository repository) {
		this.categoryRepository = repository;
	}
	
	@GetMapping("/category/default")
	public ResponseEntity<List<Category>> getDefaultCategories() {
		List<Category> categoryList = categoryRepository.findByIsDefaultCategory(true);
		return ResponseEntity.ok(categoryList);
	}
	
	@GetMapping("/category/user")
	public ResponseEntity<List<Category>> getUserCategories() {
		UserThreadLocal<AccountingUser> threadLocal = new UserThreadLocal<AccountingUser>();
		//TODO: Complete this API
		List<Long> userAccountIds = threadLocal.get().getUserAccounts();
		List<Category> categoryList = categoryRepository.findByIsDefaultCategory(true);
		return ResponseEntity.ok(categoryList);
	}
	
}
