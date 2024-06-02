package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class SetVarXor extends SetVarAbstract {
	SetVarXor(String name, ValueChunk val) {
		super(name, val);
	}

	SetVarXor(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		try {
			return env.getVar(name).xor(val.get(env), true);
		}
		catch (NullPointerException e) {
			throw new CLSL_RuntimeException(name + " cannot be resolved to a variable");
		}
	}

	@Override
	ExecutableChunk inline(DefineVar defineVar) {
		return defineVar.withVal(new OpXor(defineVar.val, val));
	}

	@Override
	public String toString() {
		return name + " ^= " + val;
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass)
			return new SetVarXor(name, (ValueChunk) val.optimize(env.forValue()));
		return env.unusedVars.contains(name) ? val.getExecutablePart(env) : this;
	}
}