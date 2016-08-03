package com.yingks.pay.exception;

public class PayException extends RuntimeException {

	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;

	public PayException() {
		super();
	}

	public PayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PayException(String message, Throwable cause) {
		super(message, cause);
	}

	public PayException(String message) {
		super(message);
	}

	public PayException(Throwable cause) {
		super(cause);
	}
	
}
