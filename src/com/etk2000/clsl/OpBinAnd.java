package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;

class OpBinAnd extends OpBinary {
	OpBinAnd(ValueChunk op1, ValueChunk op2) {
		super(op1, op2);
	}

	OpBinAnd(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		return op1.get(env).band(op2.get(env), false);
	}

	@Override
	public String toString() {
		return "(" + op1 + " & " + op2 + ')';
	}

	// TODO: optimize for when op1 or op2 are only trues (0xfffffffff...)
	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			if (op1 instanceof ConstValueChunk) {
				// 0 & X = 0
				if (!op1.get(null).toBoolean())
					return new ConstIntChunk(0);
				
				if (op2 instanceof ConstValueChunk)
					return CLSL.toChunk(get(null));
			}

			// X & 0 = 0
			else if (op2 instanceof ConstValueChunk && !op2.get(null).toBoolean())
				return new ConstIntChunk(0);

			CLSLChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpBinAnd((ValueChunk) ep1, (ValueChunk) ep2).optimize(env);
		}
		return this;
	}
}