package com.etk2000.clsl.chunk.op;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.chunk.ClslChunk;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OpMember implements ValueChunk {
	private final ValueChunk op;
	private final String member;

	public OpMember(ValueChunk op, String member) {
		this.op = op;
		this.member = member;
	}

	OpMember(InputStream i) throws IOException {
		op = Clsl.readValueChunk(i);
		member = StreamUtils.readString(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, op);
		StreamUtils.write(o, member);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
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
		if (env.isFirstPass) {
			ClslChunk ep = op.optimize(env.forValue());
			if (ep != op)
				return new OpMember((ValueChunk) ep, member).optimize(env);
		}
		return this;
	}
}