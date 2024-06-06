package com.etk2000.clsl;

import com.etk2000.clsl.chunk.FunctionalChunk;

import java.util.HashMap;
import java.util.Map;

public class MainScopeLookupTable implements FunctionLookupTable {
	final Map<String, FunctionalChunk> funcs = new HashMap<>();

	public void clear() {
		funcs.clear();
	}

	@Override
	public FunctionalChunk lookup(String functionName) {
		return funcs.get(functionName);
	}

	public void put(String functionName, FunctionalChunk body) {
		funcs.put(functionName, body);
	}
}