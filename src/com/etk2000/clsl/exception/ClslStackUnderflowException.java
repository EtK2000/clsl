package com.etk2000.clsl.exception;

public class ClslStackUnderflowException extends ClslRuntimeException {
	public ClslStackUnderflowException(boolean isSemi) {
		super(isSemi ? "stack semi-underflow" : "stack underflow");
	}
}