package com.etk2000.clsl.chunk.variable.set;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.op.OpShiftRight;
import com.etk2000.clsl.chunk.variable.definition.DefineVar;
import com.etk2000.clsl.exception.variable.ClslVariableCannotBeResolvedException;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;

public class SetVarShiftRight extends SetVarAbstract {
	public SetVarShiftRight(String name, ValueChunk val) {
		super(name, val);
	}

	public SetVarShiftRight(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		try {
			return env.getVar(name).sr(val.get(env), true);
		}
		catch (NullPointerException e) {
			throw new ClslVariableCannotBeResolvedException(name);
		}
	}

	@Override
	public ExecutableChunk inline(DefineVar defineVar) {
		return defineVar.withVal(new OpShiftRight(defineVar.val, val));
	}

	@Override
	public String toString() {
		return name + " >>= " + val;
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass)
			return new SetVarShiftRight(name, (ValueChunk) val.optimize(env.forValue()));
		return env.unusedVars.contains(name) ? val.getExecutablePart(env) : this;
	}
}