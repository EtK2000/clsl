package com.etk2000.clsl.exception.type;

public class ClslUnknownTypeException extends ClslTypeException {
	public ClslUnknownTypeException(String typeName) {
		super("error: storage size of `" + typeName + "` isn't known");
	}
}