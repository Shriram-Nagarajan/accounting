package com.accounting.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.entity.Category;
import com.accounting.repository.CategoryRepository;

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
	
}
