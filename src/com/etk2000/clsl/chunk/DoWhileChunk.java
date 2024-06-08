package com.etk2000.clsl.chunk;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StringBuilderPoolable;
import com.etk2000.clsl.chunk.value.ConstValueChunk;
import com.etk2000.clsl.exception.ClslOptimizationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DoWhileChunk extends BlockChunk {
	private final ValueChunk cause;
	private final ExecutableChunk[] effect;

	public DoWhileChunk(ExecutableChunk[] effect, ValueChunk cause) {
		this.effect = effect;
		this.cause = cause;
	}

	DoWhileChunk(InputStream i) throws IOException {
		effect = Clsl.readExecutableChunks(i);
		cause = Clsl.readChunk(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunks(o, effect);
		Clsl.writeChunk(o, cause);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		do {
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
		} while (Clsl.evalBoolean(cause, env));
		return null;
	}

	@Override
	public String toString() {
		try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
			sb.append("do ");
			Clsl.append(sb, effect);
			return sb.append(" while (").append(cause).append(')').toString();
		}
	}

	@Override
	public BlockChunk optimize(OptimizationEnvironment env) {
		if (cause == null)// maybe look for breaks?
			throw new ClslOptimizationException("theoretical infinite loop: " + this);
		if (effect.length == 0)
			return null;// empty do-while

		ValueChunk query = (ValueChunk) cause.optimize(env.forValue());
		if (query instanceof ConstValueChunk) {
			if (query.get(null).toBoolean())// maybe look for breaks?
				throw new ClslOptimizationException("theoretical infinite loop: " + this);

			// do-while(0) just means run once
			return new CodeBlockChunk(optimize(effect, env));
		}

		ExecutableChunk[] newEffect = optimize(effect, env);
		return effect != newEffect ? new DoWhileChunk(newEffect, query) : this;
	}
}