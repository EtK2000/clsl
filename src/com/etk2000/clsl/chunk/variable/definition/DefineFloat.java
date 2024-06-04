package com.etk2000.clsl.chunk.variable.definition;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.chunk.ReturnChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.value.ClslFloat;

import java.io.IOException;
import java.io.InputStream;

public class DefineFloat extends DefineVar {
	public DefineFloat(String name, ValueChunk val) {
		super(ValueType.FLOAT, name, val);
	}

	DefineFloat(InputStream i) throws IOException {
		super(ValueType.FLOAT, i);
	}

	@Override
	public DefineFloat withVal(ValueChunk newVal) {
		return new DefineFloat(name, newVal);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		env.defineVar(name, new ClslFloat(val.get(env).toFloat()));
		return null;
	}

	@Override
	public String toString() {
		return "float " + name + " = " + val;
	}
}