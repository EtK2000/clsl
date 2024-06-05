package com.etk2000.clsl.chunk.variable.definition;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.chunk.ReturnChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.value.ClslLong;

import java.io.IOException;
import java.io.InputStream;

public class DefineLong extends DefineVar {
	public DefineLong(String name, ValueChunk val) {
		super(ValueType.LONG, name, val);
	}

	public DefineLong(InputStream i) throws IOException {
		super(ValueType.LONG, i);
	}

	@Override
	public DefineLong withVal(ValueChunk newVal) {
		return new DefineLong(name, newVal);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		env.defineVar(name, new ClslLong(val.get(env).toLong()));
		return null;
	}

	@Override
	public String toString() {
		return "long " + name + " = " + val;
	}
}