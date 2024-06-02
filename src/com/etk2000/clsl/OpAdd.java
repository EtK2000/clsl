package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class OpAdd extends OpBinary {
	OpAdd(ValueChunk op1, ValueChunk op2) {
		super(op1, op2);
	}

	OpAdd(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		return op1.get(env).add(op2.get(env), false);
	}

	@Override
	public String toString() {
		return "(" + op1 + " + " + op2 + ')';
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			if (op1 instanceof ConstValueChunk) {
				// 0 + X = X
				if (!op1.get(null).toBoolean())
					return (ValueChunk) op2.optimize(env.forValue());
				if (op2 instanceof ConstValueChunk)
					return CLSL.toChunk(get(null));
			}

			// X + 0 = X
			else if (op2 instanceof ConstValueChunk && !op2.get(null).toBoolean())
				return (ValueChunk) op1.optimize(env.forValue());

			CLSLChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpAdd((ValueChunk) ep1, (ValueChunk) ep2).optimize(env);
		}
		return this;
	}
}