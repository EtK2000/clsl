package com.etk2000.clsl.exception.variable;

public class ClslVariableCannotBeResolvedException extends ClslVariableException {
	public ClslVariableCannotBeResolvedException(String variableName) {
		super(variableName + " cannot be resolved to a variable");
	}
}