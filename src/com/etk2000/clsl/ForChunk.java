package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class ForChunk extends BlockChunk {
	private static void exec(ExecutableChunk c, CLSLRuntimeEnv env) {
		if (c != null)
			c.execute(env);
	}

	private final ExecutableChunk pre, post;
	private final ValueChunk cause;
	private final ExecutableChunk[] effect;

	ForChunk(ExecutableChunk pre, ValueChunk cause, ExecutableChunk post, ExecutableChunk[] effect) {
		this.pre = pre;
		this.cause = cause;
		this.post = post;
		this.effect = effect;
	}

	ForChunk(InputStream i) throws IOException {
		pre = CLSL.readExecutableChunk(i);
		cause = CLSL.readValueChunk(i);
		post = CLSL.readExecutableChunk(i);
		effect = CLSL.readExecutableChunks(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		CLSL.writeChunk(o, pre);
		CLSL.writeChunk(o, cause);
		CLSL.writeChunk(o, post);
		CLSL.writeChunks(o, effect);
	}

	@Override
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		env.pushStack(false);// for pre
		try {
			for (exec(pre, env); CLSL.evalBoolean(cause, env); exec(post, env)) {
				if (effect.length > 0) {
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
			}
		}
		finally {
			env.popStack(false);
		}
		return null;
	}

	@Override
	public String toString() {
		try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
			sb.append("for (");
			
			if (pre != null)
				sb.append(pre);
			sb.append("; ");
			
			if (cause != null)
				sb.append(cause);
			sb.append("; ");
			
			if (post != null)
				sb.append(post);
			sb.append(") ");
			
			CLSL.append(sb, effect);
			return sb.toString();
		}
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (cause == null)// maybe look for breaks?
			throw new CLSL_Exception("theoretical infinite loop: " + this);

		ExecutableChunk before, after;
		ValueChunk query;

		// needs to be before cause optimization
		before = pre != null ? pre.optimize(env) : null;

		if (env.firstPass) {
			// only a Sith deals in absolutes
			if ((query = (ValueChunk) cause.optimize(env.forValue())) instanceof ConstValueChunk) {
				if (query.get(null).toBoolean())// maybe look for breaks?
					throw new CLSL_Exception("theoretical infinite loop: " + this);

				// remove unused variable definition if present
				ExecutableChunk res = pre.optimize(env);
				return res instanceof ExecutableValueChunk ? ((ExecutableValueChunk) res).getExecutablePart(env) : res;
				// FIXME: add single run of cause into executable parts
			}
		}
		else
			query = cause;

		after = post != null ? post.optimize(env) : null;// FIXME: if using vars from pre, remove from env.unusedVars

		// for(;x;) => while(x)
		if (before == null && after == null)
			return new WhileChunk(query, optimize(effect, env));

		return new ForChunk(before, query, after, optimize(effect, env));
	}
}