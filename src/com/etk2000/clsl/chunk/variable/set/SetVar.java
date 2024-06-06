package com.etk2000.clsl.chunk.variable.set;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.variable.GetVar;
import com.etk2000.clsl.chunk.variable.definition.DefineVar;
import com.etk2000.clsl.exception.variable.ClslVariableCannotBeResolvedException;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;

public class SetVar extends SetVarAbstract {
	public SetVar(String name, ValueChunk val) {
		super(name, val);
	}

	public SetVar(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		try {
			return env.getVar(name).set(val.get(env));
		}
		catch (NullPointerException e) {
			throw new ClslVariableCannotBeResolvedException(name);
		}
	}

	@Override
	public ExecutableChunk inline(DefineVar defineVar) {
		return defineVar.withVal(val);
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			ValueChunk newVal = (ValueChunk) val.optimize(env.forValue());
			if (newVal instanceof GetVar && ((GetVar) newVal).name.equals(name))
				return null;
			return new SetVar(name, newVal);
		}
		return env.unusedVars.contains(name) ? val.getExecutablePart(env) : this;
	}

	@Override
	public String toString() {
		return name + " = " + val;
	}
}