package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class SetVarAdd extends SetVarAbstract {
	SetVarAdd(String name, ValueChunk val) {
		super(name, val);
	}

	SetVarAdd(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		try {
			return env.getVar(name).add(val.get(env), true);
		}
		catch (NullPointerException e) {
			throw new CLSL_RuntimeException(name + " cannot be resolved to a variable");
		}
	}

	// TODO: do we need to cast to prevent overflow?
	@Override
	ExecutableChunk inline(DefineVar defineVar) {
		return defineVar.withVal(new OpAnd(defineVar.val, val));
	}

	@Override
	public String toString() {
		return name + " += " + val;
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass)
			return new SetVarAdd(name, (ValueChunk) val.optimize(env.forValue()));
		return env.unusedVars.contains(name) ? val.getExecutablePart(env) : this;
	}
}