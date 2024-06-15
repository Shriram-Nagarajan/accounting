package com.accounting.model;

import java.util.List;

import com.common.model.User;

public class AccountingUser extends User {

	private static final long serialVersionUID = 1L;
	
	public AccountingUser(User user) {
		setUserDetails(user.getUserDetails());
		setSessionId(user.getSessionId());
	}

	private List<Long> userAccounts;

	public List<Long> getUserAccounts() {
		return userAccounts;
	}

	public void setUserAccounts(List<Long> userAccounts) {
		this.userAccounts = userAccounts;
	} 
	
}
