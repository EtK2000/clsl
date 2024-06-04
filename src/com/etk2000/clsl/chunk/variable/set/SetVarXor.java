package com.etk2000.clsl.chunk.variable.set;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.op.OpXor;
import com.etk2000.clsl.chunk.variable.definition.DefineVar;
import com.etk2000.clsl.exception.variable.ClslVariableCannotBeResolvedException;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;

public class SetVarXor extends SetVarAbstract {
	public SetVarXor(String name, ValueChunk val) {
		super(name, val);
	}

	SetVarXor(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		try {
			return env.getVar(name).xor(val.get(env), true);
		}
		catch (NullPointerException e) {
			throw new ClslVariableCannotBeResolvedException(name);
		}
	}

	@Override
	public ExecutableChunk inline(DefineVar defineVar) {
		return defineVar.withVal(new OpXor(defineVar.val, val));
	}

	@Override
	public String toString() {
		return name + " ^= " + val;
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass)
			return new SetVarXor(name, (ValueChunk) val.optimize(env.forValue()));
		return env.unusedVars.contains(name) ? val.getExecutablePart(env) : this;
	}
}