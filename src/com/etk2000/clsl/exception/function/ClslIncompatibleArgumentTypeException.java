package com.etk2000.clsl.exception.function;

public class ClslIncompatibleArgumentTypeException extends ClslFunctionCallException {
	public ClslIncompatibleArgumentTypeException(int argumentIndex, String functionName) {
		super("incompatible type for argument " + argumentIndex + " of `" + functionName + '`');
	}
}