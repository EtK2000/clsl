package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class DefineFloat extends DefineVar {
	DefineFloat(String name, ValueChunk val) {
		super(ValueType.FLOAT, name, val);
	}

	DefineFloat(InputStream i) throws IOException {
		super(ValueType.FLOAT, i);
	}

	@Override
	DefineFloat withVal(ValueChunk newVal) {
		return new DefineFloat(name, newVal);
	}

	@Override
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		env.defineVar(name, new CLSLFloat(val.get(env).toFloat()));
		return null;
	}

	@Override
	public String toString() {
		return "float " + name + " = " + val;
	}
}