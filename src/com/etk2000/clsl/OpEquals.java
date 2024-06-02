package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class OpEquals extends OpBinary {
	private final boolean not;

	OpEquals(ValueChunk op1, ValueChunk op2, boolean not) {
		super(op1, op2);
		this.not = not;
	}

	OpEquals(InputStream i) throws IOException {
		super(i);
		not = StreamUtils.readByte(i) != 0;
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		super.transmit(o);
		o.write(not ? 1 : 0);
	}

	@Override
	public CLSLInt get(CLSLRuntimeEnv env) {
		return new CLSLInt(op1.get(env).equals(op2.get(env)) != not ? 1 : 0);
	}

	@Override
	public String toString() {
		return "(" + op1 + (not ? " != " : " == ") + op2 + ')';
	}

	// TODO: if op1 == op2 (i.e. they are the same code),
	// we can return !not and execute the part of them that's needed
	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			if (op1 instanceof ConstValueChunk && op2 instanceof ConstValueChunk)
				return CLSL.toChunk(get(null));

			CLSLChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2)
				return new OpEquals((ValueChunk) ep1, (ValueChunk) ep2, not).optimize(env);
		}
		return this;
	}
}