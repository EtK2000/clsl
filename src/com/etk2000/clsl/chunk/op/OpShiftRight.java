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

public class OpShiftRight extends OpBinary {
	public OpShiftRight(ValueChunk op1, ValueChunk op2) {
		super(op1, op2);
	}

	public OpShiftRight(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return op1.get(env).sr(op2.get(env), false);
	}

	// TODO: if op2 is big enough,
	// we can return 0 and execute the part of them that's needed
	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			if (op2 instanceof ConstValueChunk) {
				if (!op2.get(null).toBoolean())// X >> 0 = X
					return (ValueChunk) op1.optimize(env);
				if (op1 instanceof ConstValueChunk)
					return Clsl.toChunk(get(null));
			}

			ClslChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpShiftRight((ValueChunk) ep1, (ValueChunk) ep2).optimize(env);
		}
		return this;
	}

	@Override
	public String toString() {
		return "(" + op1 + " >> " + op2 + ')';
	}

	@Override
	public OpShiftRight withFirstOp(ValueChunk op1) {
		return new OpShiftRight(op1, op2);
	}
}