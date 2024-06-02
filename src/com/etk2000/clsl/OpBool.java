package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class OpBool implements ValueChunk {
	private final ValueChunk var;

	OpBool(ValueChunk var) {
		this.var = var;
	}

	OpBool(InputStream i) throws IOException {
		var = CLSL.readValueChunk(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		CLSL.writeChunk(o, var);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		return new CLSLInt(CLSL.evalBoolean(var, env) ? 1 : 0);
	}

	@Override
	public String toString() {
		return var.toString();
	}

	@Override
	public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return var.getExecutablePart(env);
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			if (var instanceof ConstValueChunk)
				return new ConstIntChunk(var.get(null).toBoolean() ? 1 : 0);
			
			CLSLChunk op = var.optimize(env.forValue());
			if (op != var)
				return new OpBool((ValueChunk) op).optimize(env);
		}
		return this;
	}
}