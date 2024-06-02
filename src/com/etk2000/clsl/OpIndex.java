package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class OpIndex implements ValueChunk {
	private final ValueChunk op, index;

	OpIndex(ValueChunk op, ValueChunk index) {
		this.op = op;
		this.index = index;
	}

	OpIndex(InputStream i) throws IOException {
		op = CLSL.readValueChunk(i);
		index = CLSL.readValueChunk(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		CLSL.writeChunk(o, op);
		CLSL.writeChunk(o, index);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		return op.get(env).index(index.get(env));
	}

	@Override
	public String toString() {
		return "(" + op + '[' + index + "])";
	}

	@Override
	public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return op.getExecutablePart(env);
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			CLSLChunk ep = op.optimize(env.forValue());
			CLSLChunk ei = index.optimize(env.forValue());
			if (ep != op || ei != index)
				return new OpIndex((ValueChunk) ep, (ValueChunk) ei).optimize(env);
		}
		return this;
	}
}