package com.etk2000.clsl.chunk;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// FIXME: support loading this from code as stray curly brackets ({})
class CodeBlockChunk extends BlockChunk {
	private final ExecutableChunk[] effect;

	CodeBlockChunk(ExecutableChunk[] effect) {
		this.effect = effect;
	}

	CodeBlockChunk(InputStream i) throws IOException {
		effect = Clsl.readExecutableChunks(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunks(o, effect);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
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
		return null;
	}

	@Override
	public CodeBlockChunk optimize(OptimizationEnvironment env) {
		ExecutableChunk[] newEffect = optimize(effect, env);
		return effect != newEffect ? new CodeBlockChunk(newEffect) : this;
	}
}