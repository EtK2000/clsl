package com.etk2000.clsl;

@SuppressWarnings("serial")
public class CLSL_Exception extends RuntimeException {
	CLSL_Exception(String msg) {
		super(msg);
	}

	@Override
	public synchronized CLSL_Exception fillInStackTrace() {
		return this;
	}
}