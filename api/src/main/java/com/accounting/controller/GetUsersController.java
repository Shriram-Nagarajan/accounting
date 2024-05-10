package com.accounting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.handler.UsersHandler;
import com.accounting.model.User;

@RestController
public class GetUsersController {
	
	@Autowired
	private UsersHandler usersHandler;

	@GetMapping("/all-users")
	public List<User> getAllUsers() {
		return usersHandler.getAllUsers();
	}
	
}