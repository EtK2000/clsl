package com.etk2000.clsl.chunk.variable.definition;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.chunk.ReturnChunk;
import com.etk2000.clsl.chunk.ValueChunk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DefineStruct extends DefineVar {
	private final String structName;

	public DefineStruct(String structName, String name) {
		super(ValueType.STRUCT, name, null);
		this.structName = structName;
	}

	public DefineStruct(InputStream i) throws IOException {
		super(ValueType.STRUCT, StreamUtils.readString(i), null);
		this.structName = StreamUtils.readString(i);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		env.defineVar(name, env.structInterop.newInstance(structName));
		return null;
	}

	@Override
	public String toString() {
		return "struct " + structName + ' ' + name;
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, name);
		StreamUtils.write(o, structName);
	}

	@Override
	public DefineStruct withVal(ValueChunk newVal) {
		throw new IllegalStateException("not implemented");
	}
}