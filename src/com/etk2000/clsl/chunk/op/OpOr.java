package com.etk2000.clsl.chunk.op;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ClslChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.chunk.value.ConstValueChunk;
import com.etk2000.clsl.value.ClslIntConst;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;

public class OpOr extends OpBinary {
	public OpOr(ValueChunk op1, ValueChunk op2) {
		super(op1, op2);
	}

	public OpOr(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return new ClslIntConst(op1.get(env).toBoolean() || op2.get(env).toBoolean() ? 1 : 0);
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			if (op1 instanceof ConstValueChunk) {
				// true || X = true
				if (op1.get(null).toBoolean())
					return new ConstIntChunk(1);
				if (op2 instanceof ConstValueChunk)
					return new ConstIntChunk(op2.get(null).toBoolean() ? 1 : 0);
				return new OpBool(op2).optimize(env.forValue());
			}

			ClslChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpOr((ValueChunk) ep1, (ValueChunk) ep2).optimize(env);
		}
		return this;
	}

	@Override
	public String toString() {
		return "(" + op1 + " || " + op2 + ')';
	}

	@Override
	public OpOr withFirstOp(ValueChunk op1) {
		return new OpOr(op1, op2);
	}
}