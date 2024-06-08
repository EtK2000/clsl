package com.etk2000.clsl.chunk.variable;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.ClslUtil;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.VariableAccess;
import com.etk2000.clsl.exception.variable.ClslInvalidVariableNameException;
import com.etk2000.clsl.exception.variable.ClslVariableCannotBeResolvedException;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GetVar implements VariableAccess {
	private final String name;

	public GetVar(String name) {
		if (!ClslUtil.isValidId(this.name = name))
			throw new ClslInvalidVariableNameException("invalid var name: " + name);
	}

	public GetVar(InputStream i) throws IOException {
		if (!ClslUtil.isValidId(name = StreamUtils.readString(i)))
			throw new ClslInvalidVariableNameException("invalid var name: " + name);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;

		return name.equals(((GetVar) other).name);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		ClslValue res = env.getVar(name);
		if (res == null)
			throw new ClslVariableCannotBeResolvedException(name);
		return res;
	}

	@Override
	public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return null;
	}

	@Override
	public String getVariableName() {
		return name;
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

	@Override
	public String toString() {
		return name;
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, name);
	}
}