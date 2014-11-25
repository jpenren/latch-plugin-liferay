package com.elevenpaths.latch.api.exception;

/**
 * Error on response data received
 * 
 * @author jpenren
 * 
 */
public class MethodException extends Exception {
	private static final long serialVersionUID = 1L;

	public MethodException() {
	}

	public MethodException(String message) {
		super(message);
	}

	public MethodException(String message, Throwable cause) {
		super(message, cause);
	}

}
