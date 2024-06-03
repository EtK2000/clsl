package com.etk2000.clsl.exception.type;

public class ClslConstDecrementException extends ClslTypeException {
	public ClslConstDecrementException() {
		super("lvalue required as increment operand");
	}
}