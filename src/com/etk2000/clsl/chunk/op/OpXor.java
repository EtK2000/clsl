package com.etk2000.clsl.chunk.op;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ClslChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.value.ConstValueChunk;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;

public class OpXor extends OpBinary {
	public OpXor(ValueChunk op1, ValueChunk op2) {
		super(op1, op2);
	}

	public OpXor(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return op1.get(env).xor(op2.get(env), false);
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			if (op1 instanceof ConstValueChunk) {
				if (op2 instanceof ConstValueChunk)
					return Clsl.toChunk(get(null));

				// 0 ^ X = X
				if (!op1.get(null).toBoolean())
					return (ValueChunk) op2.optimize(env.forValue());
			}

			// X ^ 0 = X
			else if (op2 instanceof ConstValueChunk && !op2.get(null).toBoolean())
				return (ValueChunk) op1.optimize(env.forValue());

			ClslChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpXor((ValueChunk) ep1, (ValueChunk) ep2).optimize(env);
		}
		return this;
	}

	@Override
	public String toString() {
		return "(" + op1 + " ^ " + op2 + ')';
	}

	@Override
	public OpXor withFirstOp(ValueChunk op1) {
		return new OpXor(op1, op2);
	}
}