package com.accounting.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UserAccountId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "user_id")
	private long userId;
	
	@Column(name = "account_id")
	private long accountId;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

}
