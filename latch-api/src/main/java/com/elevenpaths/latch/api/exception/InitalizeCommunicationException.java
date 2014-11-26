package com.elevenpaths.latch.api.exception;

public class InitalizeCommunicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InitalizeCommunicationException() {
	}
	
	public InitalizeCommunicationException(String message) {
		super(message);
	}
	
	public InitalizeCommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

}
