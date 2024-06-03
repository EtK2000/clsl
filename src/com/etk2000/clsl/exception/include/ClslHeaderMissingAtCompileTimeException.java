package com.etk2000.clsl.exception.include;

import com.etk2000.clsl.exception.ClslCompilerException;

public class ClslHeaderMissingAtCompileTimeException extends ClslCompilerException {
	public ClslHeaderMissingAtCompileTimeException(String header) {
		super("could find header \"" + header + '"');
	}
}