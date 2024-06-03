package com.etk2000.clsl;

import com.etk2000.clsl.exception.variable.ClslInvalidVariableNameException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// HIGH: in child optimize remove pointless sets, for example:
// X *= 1, X /= 1
// X -= 0, X += 0
// X = X
// etc, can look into Op chunks for examples
public abstract class SetVarAbstract implements ExecutableValueChunk {
	protected final String name;
	protected final ValueChunk val;

	protected SetVarAbstract(String name, ValueChunk val) {
		if (!CLSL.isValidId(this.name = name))
			throw new ClslInvalidVariableNameException(name);
		this.val = val;
	}

	protected SetVarAbstract(InputStream i) throws IOException {
		if (!CLSL.isValidId(name = StreamUtils.readString(i)))
			throw new ClslInvalidVariableNameException(name);
		val = CLSL.readValueChunk(i);
	}

	@Override
	final public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, name);
		CLSL.writeChunk(o, val);
	}

	@Override
	final public ReturnChunk execute(CLSLRuntimeEnv env) {
		get(env);
		return null;
	}

	abstract ExecutableChunk inline(DefineVar defineVar);

	@Override
	final public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return optimize(env);
	}
}