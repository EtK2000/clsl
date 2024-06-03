package com.etk2000.clsl.exception.function;

import com.etk2000.clsl.exception.ClslRuntimeException;

public class ClslBrokenJavaFunctionException extends ClslRuntimeException {
	public ClslBrokenJavaFunctionException(String reason) {
		super("broken java function: " + reason);
	}
}