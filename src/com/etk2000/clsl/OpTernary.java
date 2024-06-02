package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class OpTernary implements ValueChunk {
	private final ValueChunk op1, op2, op3;

	OpTernary(ValueChunk op1, ValueChunk op2, ValueChunk op3) {
		this.op1 = op1;
		this.op2 = op2;
		this.op3 = op3;
	}

	OpTernary(InputStream i) throws IOException {
		op1 = CLSL.readValueChunk(i);
		op2 = CLSL.readValueChunk(i);
		op3 = CLSL.readValueChunk(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		CLSL.writeChunk(o, op1);
		CLSL.writeChunk(o, op2);
		CLSL.writeChunk(o, op3);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		return op1.get(env).toBoolean() ? op2.get(env) : op3.get(env);
	}

	@Override
	public String toString() {
		return "(" + op1 + " ? " + op2 + " : " + op3 + ')';
	}

	@Override
	public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		ExecutableChunk ep1 = op1.getExecutablePart(env), ep2 = op2.getExecutablePart(env), ep3 = op3.getExecutablePart(env);
		if (ep1 == null)
			return ep2 == null ? ep3 : new DuoExecutableChunk(ep2, ep3);
		if (ep2 == null)
			return ep3 == null ? ep1 : new DuoExecutableChunk(ep1, ep3);
		return new TriExecutableChunk(ep1, ep2, ep3);
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			if (op1 instanceof ConstValueChunk)
				return (ValueChunk) (op1.get(null).toBoolean() ? op2 : op3).optimize(env.forValue());

			CLSLChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue()), ep3 = op3.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2 || ep3 != op3)
				return new OpTernary((ValueChunk) ep1, (ValueChunk) ep2, (ValueChunk) ep3).optimize(env);
		}
		return this;
	}
}