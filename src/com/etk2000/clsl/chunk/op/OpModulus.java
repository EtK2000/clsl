package com.etk2000.clsl.chunk.op;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ClslChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.chunk.value.ConstValueChunk;
import com.etk2000.clsl.exception.math.ClslDivisionByZeroException;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;

public class OpModulus extends OpBinary {
	public OpModulus(ValueChunk op1, ValueChunk op2) {
		super(op1, op2);
	}

	public OpModulus(InputStream i) throws IOException {
		super(i);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return op1.get(env).mod(op2.get(env), false);
	}

	@Override
	public String toString() {
		return "(" + op1 + " % " + op2 + ')';
	}

	// TODO: if op1 == op2 (i.e. they are the same code),
	// we can return 0 and execute the part of them that's needed
	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			if (op1 instanceof ConstValueChunk) {
				// 0 % X = 0
				if (!op1.get(null).toBoolean())
					return new ConstIntChunk(0);
				if (op2 instanceof ConstValueChunk)
					return Clsl.toChunk(get(null));
			}

			else if (op2 instanceof ConstValueChunk) {
				ClslValue v = op2.get(null);

				// X % 0 = UNDEFINED
				if (!v.toBoolean())
					throw new ClslDivisionByZeroException(true);

				// X % 1 = 0
				if (Clsl.isOne(v))
					return new ConstIntChunk(0);
			}

			ClslChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpModulus((ValueChunk) ep1, (ValueChunk) ep2).optimize(env);
		}
		return this;
	}
}