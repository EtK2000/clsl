package com.etk2000.clsl.chunk;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.Group;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.StringBuilderPoolable;
import com.etk2000.clsl.chunk.value.ConstValueChunk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IfChunk extends BlockChunk {
	private final List<Group<ValueChunk, ExecutableChunk[]>> causeEffect;

	public IfChunk(ValueChunk cause, ExecutableChunk[] effect) {
		(causeEffect = new ArrayList<>(1)).add(new Group<>(cause, effect));
	}

	// TODO: maybe finalize array?
	public IfChunk(InputStream i) throws IOException {
		short len = StreamUtils.readByteUnsigned(i);
		causeEffect = new ArrayList<>(len);
		for (short j = 0; j < len; ++j)
			causeEffect.add(new Group<>(Clsl.readChunk(i), Clsl.readExecutableChunks(i)));
	}

	public void addElse(Group<ValueChunk, ExecutableChunk[]> causeEffect) {
		this.causeEffect.add(causeEffect);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;

		return causeEffect.equals(((IfChunk) other).causeEffect);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		for (Group<ValueChunk, ExecutableChunk[]> g : causeEffect) {
			if (Clsl.evalBoolean(g.a, env)) {
				try {
					env.pushStack(false);
					for (ExecutableChunk c : g.b) {
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
				break;
			}
		}
		return null;
	}

	// TODO: test this!
	@Override
	public BlockChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			if (causeEffect.size() == 0)
				return null;// oh, look, a pointless chunk!

			Iterator<Group<ValueChunk, ExecutableChunk[]>> it = causeEffect.iterator();
			Group<ValueChunk, ExecutableChunk[]> g = it.next();
			ValueChunk query = (ValueChunk) g.a.optimize(env.forValue());

			if (query instanceof ConstValueChunk) {
				// if the initial if is always true make it a block
				if (query.get(null).toBoolean())
					return new CodeBlockChunk(optimize(g.b, env));

				// if it's always false remove it due to never getting called
				it.remove();
			}

			// if any else-if is always true it's now the final else
			while (it.hasNext()) {
				query = (g = it.next()).a != null ? (ValueChunk) g.a.optimize(env.forValue()) : null;

				// we've reached the final else theoretically
				if (query == null) {
					g.b = optimize(g.b, env);

					// remove any other else-if which shouldn't exist
					while (it.hasNext()) {
						it.next();
						it.remove();
					}
				}

				// only a Sith deals in absolutes
				else if (query instanceof ConstValueChunk) {
					// else-if(1): treat as else
					if (query.get(null).toBoolean()) {
						g.a = null;
						g.b = optimize(g.b, env);

						// remove any other else-if
						while (it.hasNext()) {
							it.next();
							it.remove();
						}
					}
					else// never called, remove
						it.remove();
				}

				// just a boring else-if
				else {
					g.a = query;
					g.b = optimize(g.b, env);
				}
			}

			return this;
		}

		// second pass only optimizes ExecutableChunks
		for (Group<ValueChunk, ExecutableChunk[]> g : causeEffect)
			g.b = optimize(g.b, env);
		return this;
	}

	@Override
	public String toString() {
		try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
			for (int i = 0; i < causeEffect.size(); ++i) {
				if (i != 0)
					sb.append("else ");

				Group<ValueChunk, ExecutableChunk[]> g = causeEffect.get(i);
				if (g.a != null)
					sb.append("if (").append(g.a).append(") ");
				Clsl.append(sb, g.b);
			}

			return sb.toString();
		}
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		if (causeEffect.size() > 255)
			throw new IOException("cannot stream if with more than 255 elses");

		o.write(causeEffect.size());
		for (Group<ValueChunk, ExecutableChunk[]> g : causeEffect) {
			Clsl.writeChunk(o, g.a);
			Clsl.writeChunks(o, g.b);
		}
	}
}