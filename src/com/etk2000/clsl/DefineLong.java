package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class DefineLong extends DefineVar {
	DefineLong(String name, ValueChunk val) {
		super(ValueType.LONG, name, val);
	}

	DefineLong(InputStream i) throws IOException {
		super(ValueType.LONG, i);
	}

	@Override
	DefineLong withVal(ValueChunk newVal) {
		return new DefineLong(name, newVal);
	}

	@Override
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		env.defineVar(name, new CLSLLong(val.get(env).toLong()));
		return null;
	}

	@Override
	public String toString() {
		return "long " + name + " = " + val;
	}
}