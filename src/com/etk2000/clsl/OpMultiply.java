package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class OpMultiply extends OpBinary {
	OpMultiply(ValueChunk op1, ValueChunk op2) {
		super(op1, op2);
	}

	OpMultiply(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		return op1.get(env).mul(op2.get(env), false);
	}

	@Override
	public String toString() {
		return "(" + op1 + " * " + op2 + ')';
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			if (op1 instanceof ConstValueChunk) {
				CLSLValue v = op1.get(null);
				
				// 0 * X = 0
				if (!v.toBoolean())
					return new ConstIntChunk(0);

				// 1 * X = X
				if (CLSL.isOne(v))
					return (ValueChunk) op2.optimize(env.forValue());

				if (op2 instanceof ConstValueChunk)
					return CLSL.toChunk(get(null));
			}

			else if (op2 instanceof ConstValueChunk) {
				CLSLValue v = op2.get(null);

				// X * 0 = 0
				if (!v.toBoolean())
					return new ConstIntChunk(0);

				// X * 1 = X
				if (CLSL.isOne(v))
					return (ValueChunk) op1.optimize(env.forValue());
			}

			CLSLChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpMultiply((ValueChunk) ep1, (ValueChunk) ep2).optimize(env);
		}
		return this;
	}
}