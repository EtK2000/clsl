package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class DefineChar extends DefineVar {
	DefineChar(String name, ValueChunk val) {
		super(ValueType.CHAR, name, val);
	}

	DefineChar(InputStream i) throws IOException {
		super(ValueType.CHAR, i);
	}

	@Override
	DefineChar withVal(ValueChunk newVal) {
		return new DefineChar(name, newVal);
	}

	@Override
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		env.defineVar(name, new CLSLChar(val.get(env).toChar()));
		return null;
	}

	@Override
	public String toString() {
		return "char " + name + " = " + val;
	}
}