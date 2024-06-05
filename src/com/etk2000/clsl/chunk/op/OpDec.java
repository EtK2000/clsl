package com.etk2000.clsl.chunk.op;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.chunk.ExecutableValueChunk;
import com.etk2000.clsl.chunk.ReturnChunk;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OpDec implements ExecutableValueChunk {
	private final String name;
	private final boolean post;

	public OpDec(String name, boolean post) {
		this.name = name;
		this.post = post;
	}

	public OpDec(InputStream i) throws IOException {
		name = StreamUtils.readString(i);
		post = StreamUtils.readByte(i) != 0;
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, name);
		o.write(post ? 1 : 0);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		get(env);
		return null;
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return env.getVar(name).dec(post);
	}

	@Override
	public String toString() {
		return post ? name + "--" : ("--" + name);
	}

	@Override
	public ExecutableValueChunk getExecutablePart(OptimizationEnvironment env) {
		return optimize(env);
	}

	@Override
	public ExecutableValueChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
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