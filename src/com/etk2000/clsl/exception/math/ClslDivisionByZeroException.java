package com.etk2000.clsl.exception.math;

public class ClslDivisionByZeroException extends ClslMathException {
	public ClslDivisionByZeroException(boolean isModulus) {
		super(isModulus ? "cannot modulus by 0" : "cannot divide by 0");
	}
}