package com.etk2000.clsl.exception.include;

import com.etk2000.clsl.exception.ClslCompilerException;

public class ClslInvalidHeaderException extends ClslCompilerException {
	public ClslInvalidHeaderException(String header) {
		super("invalid header: " + header);
	}
}