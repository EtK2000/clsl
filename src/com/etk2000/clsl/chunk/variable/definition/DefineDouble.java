package com.etk2000.clsl.chunk.variable.definition;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.chunk.ReturnChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.value.ClslDouble;

import java.io.IOException;
import java.io.InputStream;

public class DefineDouble extends DefineVar {
	public DefineDouble(String name, ValueChunk val) {
		super(ValueType.DOUBLE, name, val);
	}

	public DefineDouble(InputStream i) throws IOException {
		super(ValueType.DOUBLE, i);
	}

	@Override
	public DefineDouble withVal(ValueChunk newVal) {
		return new DefineDouble(name, newVal);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		env.defineVar(name, new ClslDouble(val.get(env).toDouble()));
		return null;
	}

	@Override
	public String toString() {
		return "double " + name + " = " + val;
	}
}