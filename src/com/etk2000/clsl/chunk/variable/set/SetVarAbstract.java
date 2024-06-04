package com.etk2000.clsl.chunk.variable.set;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.ClslUtil;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ExecutableValueChunk;
import com.etk2000.clsl.chunk.ReturnChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.variable.definition.DefineVar;
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
	public final String name;
	protected final ValueChunk val;

	protected SetVarAbstract(String name, ValueChunk val) {
		if (!ClslUtil.isValidId(this.name = name))
			throw new ClslInvalidVariableNameException(name);
		this.val = val;
	}

	protected SetVarAbstract(InputStream i) throws IOException {
		if (!ClslUtil.isValidId(name = StreamUtils.readString(i)))
			throw new ClslInvalidVariableNameException(name);
		val = Clsl.readValueChunk(i);
	}

	@Override
	final public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, name);
		Clsl.writeChunk(o, val);
	}

	@Override
	public final ReturnChunk execute(ClslRuntimeEnv env) {
		get(env);
		return null;
	}

	public abstract ExecutableChunk inline(DefineVar defineVar);

	@Override
	public final ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return optimize(env);
	}
}