package com.etk2000.clsl.exception;

import java.io.IOException;

public class ClslHeaderImportFailureException extends ClslCompilerException {
	public ClslHeaderImportFailureException(String header, IOException cause) {
		super("could import header \"" + header + '"', cause);
	}
}