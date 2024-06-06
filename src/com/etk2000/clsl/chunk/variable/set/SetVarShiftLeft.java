package com.etk2000.clsl.chunk.variable.set;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.op.OpCast;
import com.etk2000.clsl.chunk.op.OpShiftLeft;
import com.etk2000.clsl.chunk.variable.definition.DefineVar;
import com.etk2000.clsl.exception.variable.ClslVariableCannotBeResolvedException;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;

public class SetVarShiftLeft extends SetVarAbstract {
	public SetVarShiftLeft(String name, ValueChunk val) {
		super(name, val);
	}

	public SetVarShiftLeft(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		try {
			return env.getVar(name).sl(val.get(env), true);
		}
		catch (NullPointerException e) {
			throw new ClslVariableCannotBeResolvedException(name);
		}
	}

	@Override
	public ExecutableChunk inline(DefineVar defineVar) {
		return defineVar.withVal(new OpShiftLeft(new OpCast(defineVar.val, defineVar.type), val));
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass)
			return new SetVarShiftLeft(name, (ValueChunk) val.optimize(env.forValue()));
		return env.unusedVars.contains(name) ? val.getExecutablePart(env) : this;
	}

	@Override
	public String toString() {
		return name + " <<= " + val;
	}
}