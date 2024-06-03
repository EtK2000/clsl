package com.etk2000.clsl;

import com.etk2000.clsl.exception.ClslException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class WhileChunk extends BlockChunk {
	private final ValueChunk cause;
	private final ExecutableChunk[] effect;

	WhileChunk(ValueChunk cause, ExecutableChunk[] effect) {
		this.cause = cause;
		this.effect = effect;
	}

	WhileChunk(InputStream i) throws IOException {
		cause = CLSL.readValueChunk(i);
		effect = CLSL.readExecutableChunks(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		CLSL.writeChunk(o, cause);
		CLSL.writeChunks(o, effect);
	}

	@Override
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		while (CLSL.evalBoolean(cause, env)) {
			try {
				env.pushStack(false);
				for (ExecutableChunk c : effect) {
					if (c instanceof ReturnChunk)
						return (ReturnChunk) c;
					ReturnChunk res = c.execute(env);
					if (res != null)
						return res;
				}
			}
			finally {
				env.popStack(false);
			}
		}
		return null;
	}

	@Override
	public String toString() {
		try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
			sb.append("while (").append(cause).append(") ");
			CLSL.append(sb, effect);
			return sb.toString();
		}
	}

	@Override
	public WhileChunk optimize(OptimizationEnvironment env) {
		if (cause == null)// maybe look for breaks?
			throw new ClslException("theoretical infinite loop: " + this);
		if (effect.length == 0)
			return null;// empty while

		ValueChunk query = (ValueChunk) cause.optimize(env.forValue());
		if (query instanceof ConstValueChunk) {
			if (query.get(null).toBoolean())// maybe look for breaks?
				throw new ClslException("theoretical infinite loop: " + this);
			return null;
		}

		ExecutableChunk[] newEffect = optimize(effect, env);
		return effect != newEffect ? new WhileChunk(query, newEffect) : this;
	}
}