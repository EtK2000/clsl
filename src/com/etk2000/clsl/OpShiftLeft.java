package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class OpShiftLeft extends OpBinary {
	OpShiftLeft(ValueChunk op1, ValueChunk op2) {
		super(op1, op2);
	}

	OpShiftLeft(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		return op1.get(env).sl(op2.get(env), false);
	}

	@Override
	public String toString() {
		return "(" + op1 + " << " + op2 + ')';
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			if (op2 instanceof ConstValueChunk) {
				if (!op2.get(null).toBoolean())// X << 0 = X
					return (ValueChunk) op1.optimize(env);
				if (op1 instanceof ConstValueChunk)
					return CLSL.toChunk(get(null));
			}

			CLSLChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpShiftLeft((ValueChunk) ep1, (ValueChunk) ep2).optimize(env);
		}
		return this;
	}
}