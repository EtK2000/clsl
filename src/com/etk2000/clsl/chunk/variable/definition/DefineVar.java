package com.etk2000.clsl.chunk.variable.definition;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslUtil;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.exception.variable.ClslInvalidVariableNameException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class DefineVar implements ExecutableChunk {
	public final ValueType type;
	public final String name;
	public final ValueChunk val;

	protected DefineVar(ValueType type, String name, ValueChunk val) {
		this.type = type;
		if (!ClslUtil.isValidId(this.name = name))
			throw new ClslInvalidVariableNameException(name);
		this.val = val;
	}

	protected DefineVar(ValueType type, InputStream i) throws IOException {
		this.type = type;
		if (!ClslUtil.isValidId(name = StreamUtils.readString(i)))
			throw new ClslInvalidVariableNameException(name);
		val = Clsl.readValueChunk(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, name);
		Clsl.writeChunk(o, val);
	}

	public abstract ExecutableChunk withVal(ValueChunk newVal);

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			env.unusedVars.add(name);
			return withVal((ValueChunk) val.optimize(env.forValue()));
		}
		return env.unusedVars.contains(name) ? val.getExecutablePart(env) : this;
	}
}