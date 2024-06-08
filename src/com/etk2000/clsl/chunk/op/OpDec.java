package com.etk2000.clsl.chunk.op;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.chunk.ExecutableValueChunk;
import com.etk2000.clsl.chunk.ReturnChunk;
import com.etk2000.clsl.chunk.VariableAccess;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OpDec implements ExecutableValueChunk {
	private final boolean post;
	private final VariableAccess op;

	public OpDec(VariableAccess op, boolean post) {
		this.op = op;
		this.post = post;
	}

	public OpDec(InputStream i) throws IOException {
		this.op = Clsl.readChunk(i);
		this.post = StreamUtils.readByte(i) != 0;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;

		final OpDec that = (OpDec) other;
		return op.equals(that.op) && post == that.post;
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		get(env);
		return null;
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return op.get(env).dec(post);
	}

	@Override
	public ExecutableValueChunk getExecutablePart(OptimizationEnvironment env) {
		return optimize(env);
	}

	@Override
	public String toString() {
		return post ? op + "--" : ("--" + op);
	}

	@Override
	public ExecutableValueChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			if (env.forValue) {
				int index = env.unusedVars.indexOf(op.getVariableName());
				if (index != -1)
					env.unusedVars.remove(index);
			}
			return this;
		}
		return !env.unusedVars.contains(op.getVariableName()) ? env.forValue || !post ? this : new OpDec(op, false) : null;
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, op);
		o.write(post ? 1 : 0);
	}
}