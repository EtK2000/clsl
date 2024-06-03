package com.etk2000.clsl.exception.type;

public class ClslConstIncrementException extends ClslTypeException {
	public ClslConstIncrementException() {
		super("lvalue required as increment operand");
	}
}