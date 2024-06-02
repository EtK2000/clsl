package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

abstract class OpBinary implements ValueChunk {
	protected final ValueChunk op1, op2;

	OpBinary(ValueChunk op1, ValueChunk op2) {
		this.op1 = op1;
		this.op2 = op2;
	}

	OpBinary(InputStream i) throws IOException {
		op1 = CLSL.readValueChunk(i);
		op2 = CLSL.readValueChunk(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		CLSL.writeChunk(o, op1);
		CLSL.writeChunk(o, op2);
	}

	@Override
	public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		ExecutableChunk ep1 = op1.getExecutablePart(env), ep2 = op2.getExecutablePart(env);
		if (ep1 == null)
			return ep2;
		if (ep2 == null)
			return ep1;
		return new DuoExecutableChunk(ep1, ep2);
	}
}
