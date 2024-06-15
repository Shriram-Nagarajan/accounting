package com.accounting.handler.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.accounting.entity.UserAccountMapping;
import com.accounting.handler.UserAccountsHandler;
import com.accounting.repository.UserAccountRepository;

@Service("userAccountsHandler")
public class UserAccountsHandlerImpl implements UserAccountsHandler {

	private UserAccountRepository userAccountRepository;
	
	public UserAccountsHandlerImpl(UserAccountRepository userAccountRepository) {
		this.userAccountRepository = userAccountRepository;
	}
	
	@Override
	public List<UserAccountMapping> findAccountsForUser(long userId) {
//		return userAccountRepository.findByUserId(userId);
		return userAccountRepository.findAll()
				.stream()
				.filter(account -> account.getUserAccountId().getUserId() == userId)
				.toList();
	}

}
