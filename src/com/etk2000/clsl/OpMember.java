package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class OpMember implements ValueChunk {
	private final ValueChunk op;
	private final String member;

	OpMember(ValueChunk op, String member) {
		this.op = op;
		this.member = member;
	}

	OpMember(InputStream i) throws IOException {
		op = CLSL.readValueChunk(i);
		member = StreamUtils.readString(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		CLSL.writeChunk(o, op);
		StreamUtils.write(o, member);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		return op.get(env).dot(member);
	}

	@Override
	public String toString() {
		return "(" + op + '.' + member + ')';
	}
	
	@Override
	public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return op.getExecutablePart(env);
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			CLSLChunk ep = op.optimize(env.forValue());
			if (ep != op)
				return new OpMember((ValueChunk) ep, member).optimize(env);
		}
		return this;
	}
}