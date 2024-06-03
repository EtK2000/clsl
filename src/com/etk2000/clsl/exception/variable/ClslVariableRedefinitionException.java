package com.etk2000.clsl.exception.variable;

public class ClslVariableRedefinitionException extends ClslVariableException {
	public ClslVariableRedefinitionException(String variableName) {
		super("redefinition of '" + variableName + '\'');
	}
}