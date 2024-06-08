package com.etk2000.clsl.chunk.variable.set;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.VariableAccess;
import com.etk2000.clsl.chunk.op.OpModulus;
import com.etk2000.clsl.chunk.variable.definition.DefineVar;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;

public class SetVarModulus extends SetVarAbstract {
	public SetVarModulus(VariableAccess variableAccess, ValueChunk val) {
		super(variableAccess, val);
	}

	public SetVarModulus(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return variableAccess.get(env).mod(val.get(env), true);
	}

	@Override
	public ExecutableChunk inline(DefineVar defineVar) {
		return defineVar.withVal(new OpModulus(defineVar.val, val));
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass)
			return new SetVarModulus(variableAccess.optimize(env.forValue()), (ValueChunk) val.optimize(env.forValue()));
		return optimizeSecondPass(env);
	}

	@Override
	public String toString() {
		return variableAccess + " %= " + val;
	}
}