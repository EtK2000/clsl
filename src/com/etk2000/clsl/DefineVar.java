package com.etk2000.clsl;

import com.etk2000.clsl.exception.variable.ClslInvalidVariableNameException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

abstract class DefineVar implements ExecutableChunk {
	final ValueType type;
	final String name;
	final ValueChunk val;

	protected DefineVar(ValueType type, String name, ValueChunk val) {
		this.type = type;
		if (!CLSL.isValidId(this.name = name))
			throw new ClslInvalidVariableNameException(name);
		this.val = val;
	}

	protected DefineVar(ValueType type, InputStream i) throws IOException {
		this.type = type;
		if (!CLSL.isValidId(name = StreamUtils.readString(i)))
			throw new ClslInvalidVariableNameException(name);
		val = CLSL.readValueChunk(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, name);
		CLSL.writeChunk(o, val);
	}

	abstract ExecutableChunk withVal(ValueChunk newVal);

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			env.unusedVars.add(name);
			return withVal((ValueChunk) val.optimize(env.forValue()));
		}
		return env.unusedVars.contains(name) ? val.getExecutablePart(env) : this;
	}
}