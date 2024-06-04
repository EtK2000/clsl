package com.etk2000.clsl.chunk.variable;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.ClslUtil;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.exception.variable.ClslInvalidVariableNameException;
import com.etk2000.clsl.exception.variable.ClslVariableCannotBeResolvedException;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GetVar implements ValueChunk {
	public final String name;

	public GetVar(String name) {
		if (!ClslUtil.isValidId(this.name = name))
			throw new ClslInvalidVariableNameException("invalid var name: " + name);
	}

	GetVar(InputStream i) throws IOException {
		if (!ClslUtil.isValidId(name = StreamUtils.readString(i)))
			throw new ClslInvalidVariableNameException("invalid var name: " + name);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, name);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		ClslValue res = env.getVar(name);
		if (res == null)
			throw new ClslVariableCannotBeResolvedException(name);
		return res;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return null;
	}

	// TODO: just because we access its value doesn't mean we use what accessed;
	// basically, to actually know if the var is used we need more passes AFAIK
	@Override
	public GetVar optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			int index = env.unusedVars.indexOf(name);
			if (index != -1)
				env.unusedVars.remove(index);
			return this;
		}
		return env.unusedVars.contains(name) ? null : this;
	}
}