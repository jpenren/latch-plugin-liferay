package com.elevenpaths.latch.api.response;

/**
 * Data available on pair response
 * 
 * @author jpenren
 */
public class PairResponse extends LatchResponse {
	private static final long serialVersionUID = 1L;
	private String accountId;
	private boolean alreadyPaired;

	PairResponse() {
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public boolean isAlreadyPaired() {
		return alreadyPaired;
	}

	public void setAlreadyPaired(boolean alreadyPaired) {
		this.alreadyPaired = alreadyPaired;
	}

}
