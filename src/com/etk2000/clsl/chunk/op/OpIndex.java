package com.etk2000.clsl.chunk.op;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ClslChunk;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OpIndex implements ValueChunk {
	private final ValueChunk op, index;

	OpIndex(ValueChunk op, ValueChunk index) {
		this.op = op;
		this.index = index;
	}

	OpIndex(InputStream i) throws IOException {
		op = Clsl.readValueChunk(i);
		index = Clsl.readValueChunk(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, op);
		Clsl.writeChunk(o, index);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
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
		if (env.isFirstPass) {
			ClslChunk ep = op.optimize(env.forValue());
			ClslChunk ei = index.optimize(env.forValue());
			if (ep != op || ei != index)
				return new OpIndex((ValueChunk) ep, (ValueChunk) ei).optimize(env);
		}
		return this;
	}
}