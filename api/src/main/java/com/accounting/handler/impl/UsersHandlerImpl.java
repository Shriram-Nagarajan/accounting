package com.accounting.handler.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accounting.dao.UsersDao;
import com.accounting.handler.UsersHandler;
import com.accounting.model.User;

@Service("usersHandler")
public class UsersHandlerImpl implements UsersHandler {
	
	@Autowired
	private UsersDao usersDao;
	
	@Override
	public List<User> getAllUsers() {
		return usersDao.getAllUsers();
	}

}
