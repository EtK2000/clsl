package com.etk2000.clsl.exception.include;

import com.etk2000.clsl.exception.ClslRuntimeException;

public class ClslHeaderNotFoundException extends ClslRuntimeException {
	public ClslHeaderNotFoundException(String header) {
		super(header + ": Could not find header");
	}
}