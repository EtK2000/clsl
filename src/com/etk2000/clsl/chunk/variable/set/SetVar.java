package com.etk2000.clsl.chunk.variable.set;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.VariableAccess;
import com.etk2000.clsl.chunk.variable.GetVar;
import com.etk2000.clsl.chunk.variable.definition.DefineVar;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;

public class SetVar extends SetVarAbstract {
	public SetVar(VariableAccess variableAccess, ValueChunk val) {
		super(variableAccess, val);
	}

	public SetVar(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return variableAccess.get(env).set(val.get(env));
	}

	@Override
	public ExecutableChunk inline(DefineVar defineVar) {
		return defineVar.withVal(val);
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			final ValueChunk newVal = (ValueChunk) val.optimize(env.forValue());
			final VariableAccess newVariableAccess = variableAccess.optimize(env.forValue());

			// remove `X = X`
			if (newVal instanceof GetVar && newVal.equals(newVariableAccess))
				return null;

			return new SetVar(newVariableAccess, newVal);
		}
		return optimizeSecondPass(env);
	}

	@Override
	public String toString() {
		return variableAccess + " = " + val;
	}
}