package com.etk2000.clsl.exception.variable;

import com.etk2000.clsl.exception.ClslCompilerException;

// TODO: should extend ClslVariableException or something
public class ClslInvalidVariableNameException extends ClslCompilerException {
	public ClslInvalidVariableNameException(String variableName) {
		super("invalid var name: " + variableName);
	}
}