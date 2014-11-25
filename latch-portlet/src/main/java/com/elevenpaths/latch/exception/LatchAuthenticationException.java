package com.elevenpaths.latch.exception;

public class LatchAuthenticationException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public LatchAuthenticationException() {
	}
	
	public LatchAuthenticationException(String message) {
		super(message);
	}
	
	public LatchAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

}
