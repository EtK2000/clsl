package com.etk2000.clsl.exception.type;

import com.etk2000.clsl.exception.ClslCompilerException;

public class ClslInvalidArrayComponentTypeException extends ClslCompilerException {
	public ClslInvalidArrayComponentTypeException() {
		super("invalid component type");
	}

	public ClslInvalidArrayComponentTypeException(String typeName, String componentTypeName) {
		super("cannot create " + typeName + " of type " + componentTypeName);
	}
}