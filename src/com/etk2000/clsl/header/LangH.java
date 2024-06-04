package com.etk2000.clsl.header;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.chunk.FunctionalChunk;

// this library is supposed to be safe so we don't create unsafe functions
// TODO: add a safety toggle so all functions exist?
public class LangH extends HeaderBase {
	private static final FunctionalChunk yield = Clsl.createFunctionalChunk((env, args) -> Thread.yield());

	@Override
	public FunctionalChunk lookup(String functionName) {
		switch (functionName) {
			case "yield":
				return yield;
		}
		return null;
	}
}