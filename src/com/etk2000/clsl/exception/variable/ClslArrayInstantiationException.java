package com.etk2000.clsl.exception.variable;

import com.etk2000.clsl.exception.ClslCompilerException;

public class ClslArrayInstantiationException extends ClslCompilerException {
	public ClslArrayInstantiationException() {
		super("array cannot be instantiated with non-array");
	}
}
