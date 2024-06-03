package com.etk2000.clsl.exception.type;

public class ClslNotAStructureOrUnionException extends ClslTypeException {
	public ClslNotAStructureOrUnionException(String member) {
		super("request for member `" + member + "` in something not a structure or union");
	}
}