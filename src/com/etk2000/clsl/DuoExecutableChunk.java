package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Deprecated
class DuoExecutableChunk implements ExecutableChunk {
	private final ExecutableChunk ep1, ep2;

	DuoExecutableChunk(ExecutableChunk ep1, ExecutableChunk ep2) {
		this.ep1 = ep1;
		this.ep2 = ep2;
	}

	DuoExecutableChunk(InputStream i) throws IOException {
		ep1 = CLSL.readExecutableChunk(i);
		ep2 = CLSL.readExecutableChunk(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		CLSL.writeChunk(o, ep1);
		CLSL.writeChunk(o, ep2);
	}

	@Override
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		ReturnChunk res = ep1.execute(env);
		if (res != null)
			return res;
		return ep2.execute(env);
	}

	@Override
	public String toString() {
		return ep1.toString() + '\n' + ep2;
	}

	// FIXME: ensure env.forValue = false
	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		ExecutableChunk op1 = ep1.optimize(env), op2 = ep2.optimize(env);
		if (op1 == null)
			return op2;
		if (op2 == null)
			return op1;
		return new DuoExecutableChunk(op1, op2);
	}
}