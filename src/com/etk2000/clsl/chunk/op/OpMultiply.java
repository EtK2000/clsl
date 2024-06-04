package com.etk2000.clsl.chunk.op;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ClslChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.chunk.value.ConstValueChunk;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;

public class OpMultiply extends OpBinary {
	public OpMultiply(ValueChunk op1, ValueChunk op2) {
		super(op1, op2);
	}

	OpMultiply(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return op1.get(env).mul(op2.get(env), false);
	}

	@Override
	public String toString() {
		return "(" + op1 + " * " + op2 + ')';
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			if (op1 instanceof ConstValueChunk) {
				ClslValue v = op1.get(null);

				// 0 * X = 0
				if (!v.toBoolean())
					return new ConstIntChunk(0);

				// 1 * X = X
				if (Clsl.isOne(v))
					return (ValueChunk) op2.optimize(env.forValue());

				if (op2 instanceof ConstValueChunk)
					return Clsl.toChunk(get(null));
			}

			else if (op2 instanceof ConstValueChunk) {
				ClslValue v = op2.get(null);

				// X * 0 = 0
				if (!v.toBoolean())
					return new ConstIntChunk(0);

				// X * 1 = X
				if (Clsl.isOne(v))
					return (ValueChunk) op1.optimize(env.forValue());
			}

			ClslChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpMultiply((ValueChunk) ep1, (ValueChunk) ep2).optimize(env);
		}
		return this;
	}
}