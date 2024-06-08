package com.etk2000.clsl.chunk.variable.set;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.VariableAccess;
import com.etk2000.clsl.chunk.op.OpBinAnd;
import com.etk2000.clsl.chunk.variable.definition.DefineVar;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;

public class SetVarBinAnd extends SetVarAbstract {
	public SetVarBinAnd(VariableAccess variableAccess, ValueChunk val) {
		super(variableAccess, val);
	}

	public SetVarBinAnd(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return variableAccess.get(env).band(val.get(env), true);
	}

	@Override
	public ExecutableChunk inline(DefineVar defineVar) {
		return defineVar.withVal(new OpBinAnd(defineVar.val, val));
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass)
			return new SetVarBinAnd(variableAccess.optimize(env.forValue()), (ValueChunk) val.optimize(env.forValue()));
		return optimizeSecondPass(env);
	}

	@Override
	public String toString() {
		return variableAccess + " &= " + val;
	}
}