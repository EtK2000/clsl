package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class OpLessThan extends OpBinary {
	private final boolean lte;

	OpLessThan(ValueChunk op1, ValueChunk op2, boolean lte) {
		super(op1, op2);
		this.lte = lte;
	}

	OpLessThan(InputStream i) throws IOException {
		super(i);
		lte = StreamUtils.readByte(i) != 0;
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		super.transmit(o);
		o.write(lte ? 1 : 0);
	}

	@Override
	public CLSLInt get(CLSLRuntimeEnv env) {
		return new CLSLInt((lte ? op1.get(env).lte(op2.get(env)) : op1.get(env).lt(op2.get(env))) ? 1 : 0);
	}

	@Override
	public String toString() {
		return "(" + op1 + (lte ? " <= " : " < ") + op2 + ')';
	}

	// TODO: if op1 == op2 (i.e. they are the same code),
	// we can return lte and execute the part of them that's needed
	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			if (op1 instanceof ConstValueChunk && op2 instanceof ConstValueChunk)
				return CLSL.toChunk(get(null));

			CLSLChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpLessThan((ValueChunk) ep1, (ValueChunk) ep2, lte).optimize(env);
		}
		return this;
	}
}