package com.etk2000.clsl.chunk.op;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.ClslChunk;
import com.etk2000.clsl.chunk.DuoExecutableChunk;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.TriExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.value.ConstValueChunk;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OpTernary implements ValueChunk {
	private final ValueChunk op1, op2, op3;

	public OpTernary(ValueChunk op1, ValueChunk op2, ValueChunk op3) {
		this.op1 = op1;
		this.op2 = op2;
		this.op3 = op3;
	}

	OpTernary(InputStream i) throws IOException {
		op1 = Clsl.readValueChunk(i);
		op2 = Clsl.readValueChunk(i);
		op3 = Clsl.readValueChunk(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, op1);
		Clsl.writeChunk(o, op2);
		Clsl.writeChunk(o, op3);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
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
		if (env.isFirstPass) {
			if (op1 instanceof ConstValueChunk)
				return (ValueChunk) (op1.get(null).toBoolean() ? op2 : op3).optimize(env.forValue());

			ClslChunk ep1 = op1.optimize(env.forValue()), ep2 = op2.optimize(env.forValue()), ep3 = op3.optimize(env.forValue());
			if (ep1 != op1 || ep2 != op2 || ep3 != op3)
				return new OpTernary((ValueChunk) ep1, (ValueChunk) ep2, (ValueChunk) ep3).optimize(env);
		}
		return this;
	}
}