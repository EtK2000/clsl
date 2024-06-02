package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class OpOr extends OpBinary {
	OpOr(ValueChunk op1, ValueChunk op2) {
		super(op1, op2);
	}

	OpOr(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		return new CLSLIntConst(op1.get(env).toBoolean() || op2.get(env).toBoolean() ? 1 : 0);
	}

	@Override
	public String toString() {
		return "(" + op1 + " || " + op2 + ')';
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			if (op1 instanceof ConstValueChunk) {
				// true || X = true
				if (op1.get(null).toBoolean())
					return new ConstIntChunk(1);
				if (op2 instanceof ConstValueChunk)
					return new ConstIntChunk(op2.get(null).toBoolean() ? 1 : 0);
				return new OpBool(op2).optimize(env.forValue());
			}

			CLSLChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpOr((ValueChunk) ep1, (ValueChunk) ep2).optimize(env);
		}
		return this;
	}
}