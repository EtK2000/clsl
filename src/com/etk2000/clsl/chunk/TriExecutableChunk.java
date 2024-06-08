package com.etk2000.clsl.chunk;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Deprecated
public class TriExecutableChunk implements ExecutableChunk {
	private final ExecutableChunk ep1, ep2, ep3;

	public TriExecutableChunk(ExecutableChunk ep1, ExecutableChunk ep2, ExecutableChunk ep3) {
		this.ep1 = ep1;
		this.ep2 = ep2;
		this.ep3 = ep3;
	}

	TriExecutableChunk(InputStream i) throws IOException {
		ep1 = Clsl.readChunk(i);
		ep2 = Clsl.readChunk(i);
		ep3 = Clsl.readChunk(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, ep1);
		Clsl.writeChunk(o, ep2);
		Clsl.writeChunk(o, ep3);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		ReturnChunk res = ep1.execute(env);
		if (res != null)
			return res;
		return (res = ep2.execute(env)) != null ? res : ep3.execute(env);
	}

	@Override
	public String toString() {
		return ep1.toString() + '\n' + ep2 + '\n' + ep3;
	}

	// FIXME: ensure env.forValue = false
	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		ExecutableChunk op1 = ep1.optimize(env), op2 = ep2.optimize(env), op3 = ep3.optimize(env);
		if (op1 == null)
			return op2 == null ? op3 : new DuoExecutableChunk(op2, op3);
		if (op2 == null)
			return op3 == null ? op1 : new DuoExecutableChunk(op1, op3);
		return new TriExecutableChunk(op1, op2, op3);
	}
}