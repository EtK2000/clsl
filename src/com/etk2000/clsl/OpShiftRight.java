package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class OpShiftRight extends OpBinary {
	OpShiftRight(ValueChunk op1, ValueChunk op2) {
		super(op1, op2);
	}

	OpShiftRight(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		return op1.get(env).sr(op2.get(env), false);
	}

	@Override
	public String toString() {
		return "(" + op1 + " >> " + op2 + ')';
	}

	// TODO: if op2 is big enough,
	// we can return 0 and execute the part of them that's needed
	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			if (op2 instanceof ConstValueChunk) {
				if (!op2.get(null).toBoolean())// X >> 0 = X
					return (ValueChunk) op1.optimize(env);
				if (op1 instanceof ConstValueChunk)
					return CLSL.toChunk(get(null));
			}

			CLSLChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpShiftRight((ValueChunk) ep1, (ValueChunk) ep2).optimize(env);
		}
		return this;
	}
}