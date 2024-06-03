package com.etk2000.clsl;

import com.etk2000.clsl.exception.ClslRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class ReturnChunk implements ExecutableChunk {
	final ValueChunk val;

	ReturnChunk(ValueChunk val) {
		this.val = val;
	}

	ReturnChunk(InputStream i) throws IOException {
		val = CLSL.readValueChunk(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		CLSL.writeChunk(o, val);
	}

	@Override
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		throw new ClslRuntimeException("one doesn't simply execute a ReturnChunk");
	}

	@Override
	public String toString() {
		return val != null ? "return " + val : "return";
	}

	@Override
	public ReturnChunk optimize(OptimizationEnvironment env) {
		return env.firstPass && val != null ? new ReturnChunk((ValueChunk) val.optimize(env.forValue())) : this;
	}
}