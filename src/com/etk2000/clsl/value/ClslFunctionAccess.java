package com.etk2000.clsl.value;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.chunk.FunctionalChunk;

public class ClslFunctionAccess extends ClslValue implements ClslConst {
	private final FunctionalChunk function;

	public ClslFunctionAccess(FunctionalChunk function) {
		super(null);
		this.function = function;
	}

	@Override
	public ClslValue call(ClslRuntimeEnv env, ClslValue... args) {
		return function.call(env, args);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ClslFunctionAccess copy() {
		return this;
	}

	@Override
	public ClslIntConst sizeof() {
		return ClslIntConst.of(8);
	}

	@Override
	public Object toJava() {
		return null;
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public String typeName() {
		return null;// FIXME: should be `<return> (*<name>)(<parameters>)`
	}
}