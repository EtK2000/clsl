package com.etk2000.clsl.exception.type;

public class ClslTypeCastException extends ClslTypeException {
	public ClslTypeCastException(String typeNameFrom, String typeNameTo) {
		super("cannot cast from type " + typeNameFrom + " to " + typeNameTo);
	}
}