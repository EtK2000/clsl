package com.etk2000.clsl.exception.type;

import com.etk2000.clsl.exception.ClslCompilerException;

public class ClslInvalidArraySizeException extends ClslCompilerException {
	public ClslInvalidArraySizeException() {
		super("invalid size");
	}
}