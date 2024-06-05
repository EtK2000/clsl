package com.etk2000.clsl.chunk.op;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ClslChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.chunk.value.ConstValueChunk;
import com.etk2000.clsl.value.ClslInt;

import java.io.IOException;
import java.io.InputStream;

public class OpAnd extends OpBinary {
	public OpAnd(ValueChunk op1, ValueChunk op2) {
		super(op1, op2);
	}

	public OpAnd(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public ClslInt get(ClslRuntimeEnv env) {
		return new ClslInt(Clsl.evalBoolean(op1, env) && Clsl.evalBoolean(op2, env) ? 1 : 0);
	}

	@Override
	public String toString() {
		return "(" + op1 + " && " + op2 + ')';
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			if (op1 instanceof ConstValueChunk) {
				if (op2 instanceof ConstValueChunk)
					return Clsl.toChunk(get(null));

				// 1 && X = X
				if (op1.get(null).toBoolean())
					return (ValueChunk) op2.optimize(env.forValue());

				// 0 && X = 0
				return new ConstIntChunk(0);
			}

			else if (op2 instanceof ConstValueChunk) {
				// X && 1 = X
				if (op2.get(null).toBoolean())
					return (ValueChunk) op1.optimize(env.forValue());

				// X && 0 = 0
				return new ConstIntChunk(0);
			}

			ClslChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpAnd((ValueChunk) ep1, (ValueChunk) ep2).optimize(env);
		}
		return this;
	}
}