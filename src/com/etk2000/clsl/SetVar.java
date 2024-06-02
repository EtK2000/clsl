package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class SetVar extends SetVarAbstract {
	SetVar(String name, ValueChunk val) {
		super(name, val);
	}

	SetVar(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		try {
			return env.getVar(name).set(val.get(env));
		}
		catch (NullPointerException e) {
			throw new CLSL_RuntimeException(name + " cannot be resolved to a variable");
		}
	}

	@Override
	ExecutableChunk inline(DefineVar defineVar) {
		return defineVar.withVal(val);
	}

	@Override
	public String toString() {
		return name + " = " + val;
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			ValueChunk newVal = (ValueChunk) val.optimize(env.forValue());
			if (newVal instanceof GetVar && ((GetVar)newVal).name.equals(name))
				return null;
			return new SetVar(name, newVal);
		}
		return env.unusedVars.contains(name) ? val.getExecutablePart(env) : this;
	}
}