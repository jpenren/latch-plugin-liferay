package com.elevenpaths.latch.api.exception;

/**
 * Communication error with Latch Service
 * 
 * @author jpenren
 */
public class CommunicationException extends Exception {
	private static final long serialVersionUID = 1L;

	public CommunicationException() {
	}

	public CommunicationException(String message) {
		super(message);
	}

	public CommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

}
