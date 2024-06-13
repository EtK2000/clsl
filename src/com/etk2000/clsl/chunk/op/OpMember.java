package com.etk2000.clsl.chunk.op;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.VariableAccess;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OpMember implements VariableAccess {
	private final VariableAccess op;
	private final String member;

	public OpMember(VariableAccess op, String member) {
		this.op = op;
		this.member = member;
	}

	public OpMember(InputStream i) throws IOException {
		op = Clsl.readChunk(i);
		member = StreamUtils.readString(i);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;

		final OpMember that = (OpMember) other;
		return member.equals(that.member) && op.equals(that.op);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return op.get(env).dot(member);
	}

	@Override
	public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return op.getExecutablePart(env);
	}

	@Override
	public String getVariableName() {
		return op.getVariableName();
	}

	@Override
	public VariableAccess optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			VariableAccess ep = op.optimize(env.forValue());
			if (ep != op)
				return new OpMember(ep, member).optimize(env);
		}
		return this;
	}

	@Override
	public String toString() {
		return "(" + op + '.' + member + ')';
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, op);
		StreamUtils.write(o, member);
	}

	public OpMember withOp(VariableAccess op) {
		return new OpMember(op, member);
	}
}