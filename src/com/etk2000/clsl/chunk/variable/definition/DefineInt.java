package com.etk2000.clsl.chunk.variable.definition;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.chunk.ReturnChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.value.ClslInt;

import java.io.IOException;
import java.io.InputStream;

public class DefineInt extends DefineVar {
	public DefineInt(String name, ValueChunk val) {
		super(ValueType.INT, name, val);
	}

	DefineInt(InputStream i) throws IOException {
		super(ValueType.INT, i);
	}

	@Override
	public DefineInt withVal(ValueChunk newVal) {
		return new DefineInt(name, newVal);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		env.defineVar(name, new ClslInt(val.get(env).toInt()));
		return null;
	}

	@Override
	public String toString() {
		return "int " + name + " = " + val;
	}
}