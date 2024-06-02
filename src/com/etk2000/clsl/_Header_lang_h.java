package com.etk2000.clsl;

// this library is supposed to be safe so we don't create unsafe functions
// TODO: add a safety toggle so all functions exist?
public class _Header_lang_h extends _HeaderBase {
	private static final FunctionalChunk yield = CLSL.createFunctionalChunk((env, args) -> Thread.yield());

	@Override
	public FunctionalChunk lookup(String functionName) {
		switch (functionName) {
			case "yield":
				return yield;
		}
		return null;
	}
}