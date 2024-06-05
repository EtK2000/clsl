package com.etk2000.clsl.chunk;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.exception.ClslRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReturnChunk implements ExecutableChunk {
	public final ValueChunk val;

	public ReturnChunk(ValueChunk val) {
		this.val = val;
	}

	ReturnChunk(InputStream i) throws IOException {
		val = Clsl.readValueChunk(i);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		throw new ClslRuntimeException("one doesn't simply execute a ReturnChunk");
	}

	@Override
	public ReturnChunk optimize(OptimizationEnvironment env) {
		return env.isFirstPass && val != null ? new ReturnChunk((ValueChunk) val.optimize(env.forValue())) : this;
	}

	@Override
	public String toString() {
		return val != null ? "return " + val : "return";
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, val);
	}
}