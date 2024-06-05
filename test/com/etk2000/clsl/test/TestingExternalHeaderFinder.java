package com.etk2000.clsl.test;

import com.etk2000.clsl.ClslCode;
import com.etk2000.clsl.exception.ClslRuntimeException;
import com.etk2000.clsl.header.ClslExternHeaderFinder;

import java.util.HashMap;
import java.util.Map;

public class TestingExternalHeaderFinder implements ClslExternHeaderFinder {
	private final Map<String, ClslCode> lookup = new HashMap<>();

	public TestingExternalHeaderFinder add(String header, ClslCode code) {
		lookup.put(header, code);
		return this;
	}

	@Override
	public ClslCode find(String header) throws ClslRuntimeException {
		return lookup.get(header);
	}
}