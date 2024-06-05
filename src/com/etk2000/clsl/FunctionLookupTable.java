package com.etk2000.clsl;

import com.etk2000.clsl.chunk.FunctionalChunk;

@FunctionalInterface
public interface FunctionLookupTable {
	FunctionalChunk lookup(String functionName);
}