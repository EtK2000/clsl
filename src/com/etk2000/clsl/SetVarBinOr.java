package com.etk2000.clsl;

import com.etk2000.clsl.exception.variable.ClslVariableCannotBeResolvedException;

import java.io.IOException;
import java.io.InputStream;

class SetVarBinOr extends SetVarAbstract {
	SetVarBinOr(String name, ValueChunk val) {
		super(name, val);
	}

	SetVarBinOr(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		try {
			return env.getVar(name).bor(val.get(env), true);
		}
		catch (NullPointerException e) {
			throw new ClslVariableCannotBeResolvedException(name);
		}
	}

	@Override
	ExecutableChunk inline(DefineVar defineVar) {
		return defineVar.withVal(new OpBinOr(defineVar.val, val));
	}

	@Override
	public String toString() {
		return name + " |= " + val;
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass)
			return new SetVarBinOr(name, (ValueChunk) val.optimize(env.forValue()));
		return env.unusedVars.contains(name) ? val.getExecutablePart(env) : this;
	}
}