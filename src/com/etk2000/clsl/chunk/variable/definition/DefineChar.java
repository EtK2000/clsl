package com.etk2000.clsl.chunk.variable.definition;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.chunk.ReturnChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.value.ClslChar;

import java.io.IOException;
import java.io.InputStream;

public class DefineChar extends DefineVar {
	public DefineChar(String name, ValueChunk val) {
		super(ValueType.CHAR, name, val);
	}

	public DefineChar(InputStream i) throws IOException {
		super(ValueType.CHAR, i);
	}

	@Override
	public DefineChar withVal(ValueChunk newVal) {
		return new DefineChar(name, newVal);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		env.defineVar(name, new ClslChar(val.get(env).toChar()));
		return null;
	}

	@Override
	public String toString() {
		return "char " + name + " = " + val;
	}
}