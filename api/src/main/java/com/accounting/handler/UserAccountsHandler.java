package com.accounting.handler;

import java.util.List;

import com.accounting.entity.UserAccountMapping;

public interface UserAccountsHandler {
	
	public List<UserAccountMapping> findAccountsForUser(long userId);

}
