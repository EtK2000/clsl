package com.etk2000.clsl;

import com.etk2000.clsl.chunk.FunctionalChunk;

@FunctionalInterface
public interface FunctionLookupTable {
	public FunctionalChunk lookup(String functionName);
}