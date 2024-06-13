package com.etk2000.clsl.chunk.op;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ClslChunk;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.chunk.value.ConstValueChunk;
import com.etk2000.clsl.value.ClslInt;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OpNot implements ValueChunk {
	private final ValueChunk val;

	public OpNot(ValueChunk val) {
		this.val = val;
	}

	public OpNot(InputStream i) throws IOException {
		val = Clsl.readChunk(i);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;

		return val.equals(((OpNot) other).val);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return new ClslInt(val.get(env).toBoolean() ? 0 : 1);
	}

	@Override
	public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return val.getExecutablePart(env);
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			if (val instanceof ConstValueChunk)
				return new ConstIntChunk(val.get(null).toBoolean() ? 0 : 1);

			final ClslChunk op = val.optimize(env.forValue());
			if (op != val)
				return new OpBool((ValueChunk) op).optimize(env.forValue());
		}
		return this;
	}

	@Override
	public String toString() {
		return '!' + val.toString();
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, val);
	}
}