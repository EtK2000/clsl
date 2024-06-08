package com.etk2000.clsl.chunk;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StringBuilderPoolable;
import com.etk2000.clsl.chunk.value.ConstValueChunk;
import com.etk2000.clsl.exception.ClslException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WhileChunk extends BlockChunk {
	private final ValueChunk cause;
	private final ExecutableChunk[] effect;

	public WhileChunk(ValueChunk cause, ExecutableChunk[] effect) {
		this.cause = cause;
		this.effect = effect;
	}

	WhileChunk(InputStream i) throws IOException {
		cause = Clsl.readChunk(i);
		effect = Clsl.readExecutableChunks(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, cause);
		Clsl.writeChunks(o, effect);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		while (Clsl.evalBoolean(cause, env)) {
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
			Clsl.append(sb, effect);
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