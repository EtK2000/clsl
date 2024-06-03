package com.etk2000.clsl.exception.function;

public class ClslFunctionDidntReturnAValueException extends ClslFunctionCallException {
	public ClslFunctionDidntReturnAValueException(String functionName) {
		super("function " + functionName + " didn't return a value");
	}
}