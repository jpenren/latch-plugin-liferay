package com.elevenpaths.latch.domain;

import java.io.Serializable;
import java.util.Date;

public class UserLatch implements Serializable {

	private static final long serialVersionUID = 1L;
	private long userId;
	private String accountId;
	private Date createDate;
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
