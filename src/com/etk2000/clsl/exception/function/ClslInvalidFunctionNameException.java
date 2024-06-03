package com.etk2000.clsl.exception.function;

import com.etk2000.clsl.exception.ClslCompilerException;

public class ClslInvalidFunctionNameException extends ClslCompilerException {
	public ClslInvalidFunctionNameException(String functionName) {
		super("invalid function name: " + functionName);
	}
}