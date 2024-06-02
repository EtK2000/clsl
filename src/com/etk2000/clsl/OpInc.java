package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class OpInc implements ExecutableValueChunk {
	private final String name;
	private final boolean post;

	OpInc(String name, boolean post) {
		this.name = name;
		this.post = post;
	}

	OpInc(InputStream i) throws IOException {
		name = StreamUtils.readString(i);
		post = StreamUtils.readByte(i) != 0;
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, name);
		o.write(post ? 1 : 0);
	}

	@Override
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		get(env);
		return null;
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		return env.getVar(name).inc(post);
	}

	@Override
	public String toString() {
		return post ? name + "++" : ("++" + name);
	}

	@Override
	public ExecutableValueChunk getExecutablePart(OptimizationEnvironment env) {
		return optimize(env);
	}

	@Override
	public ExecutableValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			if (env.forValue) {
				int index = env.unusedVars.indexOf(name);
				if (index != -1)
					env.unusedVars.remove(index);
			}
			return this;
		}
		return !env.unusedVars.contains(name) ? env.forValue || !post ? this : new OpDec(name, false) : null;
	}
}