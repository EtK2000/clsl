package com.etk2000.clsl.exception.function;

public class ClslFunctionNotApplicableForArgumentsException extends ClslFunctionCallException {
	public ClslFunctionNotApplicableForArgumentsException() {
		super("function not applicable for the arguments");
	}
}