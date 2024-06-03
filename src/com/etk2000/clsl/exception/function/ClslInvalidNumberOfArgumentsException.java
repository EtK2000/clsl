package com.etk2000.clsl.exception.function;

public class ClslInvalidNumberOfArgumentsException extends ClslFunctionCallException {
	public ClslInvalidNumberOfArgumentsException(int got, int expected) {
		super("invalid number of arguments, got " + got + " expected " + expected);
	}
}