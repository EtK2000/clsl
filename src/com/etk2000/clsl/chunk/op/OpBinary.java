package com.etk2000.clsl.chunk.op;


import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.DuoExecutableChunk;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;

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
		op1 = Clsl.readValueChunk(i);
		op2 = Clsl.readValueChunk(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, op1);
		Clsl.writeChunk(o, op2);
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
