package com.etk2000.clsl.exception.type;

public class ClslNotAFunctionException extends ClslTypeException {
	public ClslNotAFunctionException() {
		super("called object is not a function or function pointer");
	}
}