package com.um.accounting.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_account_mapping")
public class UserAccountMapping {

	@EmbeddedId
	private UserAccountId userAccountId;
	
	public UserAccountMapping() {}
	
	public UserAccountMapping(UserAccountId userAccountId) {
		this.userAccountId = userAccountId;
	}

	public UserAccountId getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(UserAccountId userAccountId) {
		this.userAccountId = userAccountId;
	}

}
