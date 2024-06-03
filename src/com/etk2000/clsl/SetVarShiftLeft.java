package com.etk2000.clsl;

import com.etk2000.clsl.exception.variable.ClslVariableCannotBeResolvedException;

import java.io.IOException;
import java.io.InputStream;

class SetVarShiftLeft extends SetVarAbstract {
	SetVarShiftLeft(String name, ValueChunk val) {
		super(name, val);
	}

	SetVarShiftLeft(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		try {
			return env.getVar(name).sl(val.get(env), true);
		}
		catch (NullPointerException e) {
			throw new ClslVariableCannotBeResolvedException(name);
		}
	}

	@Override
	ExecutableChunk inline(DefineVar defineVar) {
		return defineVar.withVal(new OpShiftLeft(new OpCast(defineVar.val, defineVar.type), val));
	}

	@Override
	public String toString() {
		return name + " <<= " + val;
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass)
			return new SetVarShiftLeft(name, (ValueChunk) val.optimize(env.forValue()));
		return env.unusedVars.contains(name) ? (ExecutableChunk) val.getExecutablePart(env) : this;
	}
}