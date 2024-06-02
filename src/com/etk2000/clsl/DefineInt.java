package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class DefineInt extends DefineVar {
	DefineInt(String name, ValueChunk val) {
		super(ValueType.INT, name, val);
	}

	DefineInt(InputStream i) throws IOException {
		super(ValueType.INT, i);
	}

	@Override
	DefineInt withVal(ValueChunk newVal) {
		return new DefineInt(name, newVal);
	}

	@Override
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		env.defineVar(name, new CLSLInt(val.get(env).toInt()));
		return null;
	}

	@Override
	public String toString() {
		return "int " + name + " = " + val;
	}
}