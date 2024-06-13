package com.etk2000.clsl.chunk.op;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.chunk.ClslChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.value.ConstValueChunk;
import com.etk2000.clsl.value.ClslInt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OpLessThan extends OpBinary {
	private final boolean lte;

	public OpLessThan(ValueChunk op1, ValueChunk op2, boolean lte) {
		super(op1, op2);
		this.lte = lte;
	}

	public OpLessThan(InputStream i) throws IOException {
		super(i);
		lte = StreamUtils.readByte(i) != 0;
	}

	@Override
	public ClslInt get(ClslRuntimeEnv env) {
		return new ClslInt(
				(lte ?
						op1.get(env).lte(op2.get(env)) :
						op1.get(env).lt(op2.get(env))
				) ? 1 : 0);
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && lte == ((OpLessThan) other).lte;
	}

	// TODO: if op1 == op2 (i.e. they are the same code),
	// we can return lte and execute the part of them that's needed
	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			if (op1 instanceof ConstValueChunk && op2 instanceof ConstValueChunk)
				return Clsl.toChunk(get(null));

			ClslChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpLessThan((ValueChunk) ep1, (ValueChunk) ep2, lte).optimize(env);
		}
		return this;
	}

	@Override
	public String toString() {
		return "(" + op1 + (lte ? " <= " : " < ") + op2 + ')';
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		super.transmit(o);
		o.write(lte ? 1 : 0);
	}

	@Override
	public OpLessThan withFirstOp(ValueChunk op1) {
		return new OpLessThan(op1, op2, lte);
	}
}