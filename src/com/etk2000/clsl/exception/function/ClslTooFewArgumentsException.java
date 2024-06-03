package com.etk2000.clsl.exception.function;

public class ClslTooFewArgumentsException extends ClslFunctionCallException {
	public ClslTooFewArgumentsException(String functionName) {
		super("too few arguments to function `" + functionName + '`');
	}
}