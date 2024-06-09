package com.etk2000.clsl.chunk.op;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ClslChunk;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.VariableAccess;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OpIndex implements VariableAccess {
	private final ValueChunk index;
	private final VariableAccess op;

	public OpIndex(VariableAccess op, ValueChunk index) {
		this.op = op;
		this.index = index;
	}

	public OpIndex(InputStream i) throws IOException {
		op = Clsl.readChunk(i);
		index = Clsl.readChunk(i);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;

		final OpIndex that = (OpIndex) other;
		return index.equals(that.index) && op.equals(that.op);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return op.get(env).index(index.get(env));
	}

	@Override
	public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return op.getExecutablePart(env);
	}

	@Override
	public String getVariableName() {
		return op.getVariableName();
	}

	@Override
	public VariableAccess optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			final VariableAccess ep = op.optimize(env.forValue());
			final ClslChunk ei = index.optimize(env.forValue());
			if (ep != op || ei != index)
				return new OpIndex(ep, (ValueChunk) ei).optimize(env);
		}
		return this;
	}

	@Override
	public String toString() {
		return "(" + op + '[' + index + "])";
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, op);
		Clsl.writeChunk(o, index);
	}
}