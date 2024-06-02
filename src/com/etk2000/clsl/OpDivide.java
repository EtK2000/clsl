package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class OpDivide extends OpBinary {
	OpDivide(ValueChunk op1, ValueChunk op2) {
		super(op1, op2);
	}

	OpDivide(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		return op1.get(env).div(op2.get(env), false);
	}

	@Override
	public String toString() {
		return "(" + op1 + " / " + op2 + ')';
	}

	// TODO: if op1 == op2 (i.e. they are the same code),
	// we can return 1 and execute the part of them that's needed
	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			if (op1 instanceof ConstValueChunk) {
				// 0 / X = 0
				if (!op1.get(null).toBoolean())
					return new ConstIntChunk(0);
				if (op2 instanceof ConstValueChunk)
					return CLSL.toChunk(get(null));
			}

			else if (op2 instanceof ConstValueChunk) {
				CLSLValue v = op2.get(null);

				// X / 0 = UNDEFINED
				if (!v.toBoolean())
					throw new CLSL_Exception("cannot divide by 0");

				// X / 1 = X
				if (CLSL.isOne(v))
					return (ValueChunk) op1.optimize(env.forValue());
			}

			CLSLChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpDivide((ValueChunk) ep1, (ValueChunk) ep2).optimize(env);
		}
		return this;
	}
}