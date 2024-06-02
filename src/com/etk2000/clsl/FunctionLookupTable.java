package com.etk2000.clsl;

@FunctionalInterface
public interface FunctionLookupTable {
	public FunctionalChunk lookup(String functionName);
}