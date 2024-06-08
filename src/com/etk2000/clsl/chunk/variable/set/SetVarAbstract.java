package com.etk2000.clsl.chunk.variable.set;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ExecutableValueChunk;
import com.etk2000.clsl.chunk.ReturnChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.VariableAccess;
import com.etk2000.clsl.chunk.variable.GetVar;
import com.etk2000.clsl.chunk.variable.definition.DefineVar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// HIGH: in child optimize remove pointless sets, for example:
// X *= 1, X /= 1
// X -= 0, X += 0
// etc, can look into Op chunks for examples
public abstract class SetVarAbstract implements ExecutableValueChunk {
	protected final ValueChunk val;
	public final VariableAccess variableAccess;

	protected SetVarAbstract(VariableAccess variableAccess, ValueChunk val) {
		this.val = val;
		this.variableAccess = variableAccess;
	}

	protected SetVarAbstract(InputStream i) throws IOException {
		this.variableAccess = Clsl.readChunk(i);
		this.val = Clsl.readChunk(i);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;

		final SetVarAbstract that = (SetVarAbstract) other;
		return val.equals(that.val) && variableAccess.equals(that.variableAccess);
	}

	@Override
	public final ReturnChunk execute(ClslRuntimeEnv env) {
		get(env);
		return null;
	}

	@Override
	public final ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return optimize(env);
	}

	public abstract ExecutableChunk inline(DefineVar defineVar);

	protected final ExecutableChunk optimizeSecondPass(OptimizationEnvironment env) {
		if (variableAccess instanceof GetVar && env.unusedVars.contains(variableAccess.getVariableName()))
			return val.getExecutablePart(env);

		return this;
	}

	@Override
	public final void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, variableAccess);
		Clsl.writeChunk(o, val);
	}
}