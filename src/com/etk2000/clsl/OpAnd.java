package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class OpAnd extends OpBinary {
	OpAnd(ValueChunk op1, ValueChunk op2) {
		super(op1, op2);
	}

	OpAnd(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public CLSLInt get(CLSLRuntimeEnv env) {
		return new CLSLInt(CLSL.evalBoolean(op1, env) && CLSL.evalBoolean(op2, env) ? 1 : 0);
	}

	@Override
	public String toString() {
		return "(" + op1 + " && " + op2 + ')';
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			if (op1 instanceof ConstValueChunk) {
				if (op2 instanceof ConstValueChunk)
					return CLSL.toChunk(get(null));

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

			CLSLChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpAnd((ValueChunk) ep1, (ValueChunk) ep2).optimize(env);
		}
		return this;
	}
}