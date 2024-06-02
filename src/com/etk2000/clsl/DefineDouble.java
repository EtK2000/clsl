package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class DefineDouble extends DefineVar {
	DefineDouble(String name, ValueChunk val) {
		super(ValueType.DOUBLE, name, val);
	}

	DefineDouble(InputStream i) throws IOException {
		super(ValueType.DOUBLE, i);
	}

	@Override
	DefineDouble withVal(ValueChunk newVal) {
		return new DefineDouble(name, newVal);
	}

	@Override
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		env.defineVar(name, new CLSLDouble(val.get(env).toDouble()));
		return null;
	}

	@Override
	public String toString() {
		return "double " + name + " = " + val;
	}
}