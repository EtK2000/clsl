package com.etk2000.clsl.header;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.chunk.FunctionalChunk;
import com.etk2000.clsl.chunk.ReturnChunk;

// this library is supposed to be safe so we don't create unsafe functions
// TODO: add a safety toggle so all functions exist?
public class LangH extends HeaderBase {
	public static final LangH INSTANCE = new LangH();

	private static final FunctionalChunk yield = Clsl.createFunctionalChunk((env, args) -> Thread.yield());

	private LangH() {
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		env.defineVar("yield", yield.access());
		return null;
	}
}