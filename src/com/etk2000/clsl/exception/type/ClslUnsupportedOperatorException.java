package com.etk2000.clsl.exception.type;

public class ClslUnsupportedOperatorException extends ClslTypeException {
	public ClslUnsupportedOperatorException(String op, String typeName) {
		super("The operator " + op + " is undefined for the argument type(s) " + typeName);
	}

	public ClslUnsupportedOperatorException(String op, String typeName, String otherTypeName) {
		super("The operator " + op + " is undefined for the argument type(s) " + typeName + ", " + otherTypeName);
	}
}
