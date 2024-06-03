package com.etk2000.clsl;

import com.etk2000.clsl.exception.variable.ClslVariableCannotBeResolvedException;

import java.io.IOException;
import java.io.InputStream;

class SetVarMul extends SetVarAbstract {
	SetVarMul(String name, ValueChunk val) {
		super(name, val);
	}

	SetVarMul(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		try {
			return env.getVar(name).mul(val.get(env), true);
		}
		catch (NullPointerException e) {
			throw new ClslVariableCannotBeResolvedException(name);
		}
	}

	@Override
	ExecutableChunk inline(DefineVar defineVar) {
		return defineVar.withVal(new OpMultiply(new OpCast(defineVar.val, defineVar.type), val));
	}

	@Override
	public String toString() {
		return name + " *= " + val;
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass)
			return new SetVarMul(name, (ValueChunk) val.optimize(env.forValue()));
		return env.unusedVars.contains(name) ? val.getExecutablePart(env) : this;
	}
}