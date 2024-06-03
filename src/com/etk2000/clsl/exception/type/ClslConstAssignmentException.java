package com.etk2000.clsl.exception.type;

public class ClslConstAssignmentException extends ClslTypeException {
	public ClslConstAssignmentException() {
		super("lvalue required as left operand of assignment");
	}
}