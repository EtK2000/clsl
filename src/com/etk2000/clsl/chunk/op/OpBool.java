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
import java.util.Objects;

public class OpBool implements ValueChunk {
	private final ValueChunk val;

	public OpBool(ValueChunk val) {
		this.val = val;
	}

	public OpBool(InputStream i) throws IOException {
		this.val = Clsl.readChunk(i);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;

		return Objects.equals(val, ((OpBool) other).val);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return new ClslInt(Clsl.evalBoolean(val, env) ? 1 : 0);
	}

	@Override
	public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return val.getExecutablePart(env);
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			if (val instanceof ConstValueChunk)
				return new ConstIntChunk(val.get(null).toBoolean() ? 1 : 0);

			ClslChunk op = val.optimize(env.forValue());
			if (op != val)
				return new OpBool((ValueChunk) op).optimize(env);
		}
		return this;
	}

	@Override
	public String toString() {
		return val.toString();
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, val);
	}
}