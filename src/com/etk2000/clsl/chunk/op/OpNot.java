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
	private final ValueChunk var;

	public OpNot(ValueChunk var) {
		this.var = var;
	}

	public OpNot(InputStream i) throws IOException {
		var = Clsl.readValueChunk(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, var);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return new ClslInt(var.get(env).toBoolean() ? 0 : 1);
	}

	@Override
	public String toString() {
		return '!' + var.toString();
	}

	@Override
	public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return var.getExecutablePart(env);
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			if (var instanceof ConstValueChunk)
				return new ConstIntChunk(var.get(null).toBoolean() ? 0 : 1);

			ClslChunk op = var.optimize(env.forValue());
			if (op != var)
				return new OpBool((ValueChunk) op).optimize(env.forValue());
		}
		return this;
	}
}