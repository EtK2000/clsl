package com.etk2000.clsl.exception;

public class ClslException extends RuntimeException {
	public ClslException(String msg) {
		super(msg);
	}

	ClslException(String msg, Exception cause) {
		super(msg, cause);
	}

	@Override
	public synchronized ClslException fillInStackTrace() {
		return this;
	}
}