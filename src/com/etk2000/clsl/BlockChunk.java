package com.etk2000.clsl;

import java.util.ArrayList;
import java.util.List;

// a chunk that is in a new block
abstract class BlockChunk implements ExecutableChunk {

	/* this fixes issues due to floats and binary ops, for example:
	float a = 1;
	a |= 2;
	 * should fail to execute normally, but is optimized into
	float a = 1 | 2;
	 * which is valid, and a will be further optimized into 3
	 */
	protected static ExecutableChunk[] optimize(ExecutableChunk[] effect, OptimizationEnvironment env) {
		// FIXME: ensure env.forValue = false
		
		short i = 0;
		for (short last = -1; i < effect.length; ++i, ++last) {
			effect[i] = effect[i].optimize(env);

			// if the last line was a var definition and we changed that var
			// TODO: allow going back multiple lines if var hasn't been used
			// TODO: maybe also check if var is in unusedVars?
			if (i > 0 && effect[i] != null && effect[i] instanceof SetVarAbstract && effect[last] instanceof DefineVar
					&& ((DefineVar) effect[last]).name.equals(((SetVarAbstract) effect[i]).name)) {
				effect[last] = ((SetVarAbstract) effect[i]).inline((DefineVar) effect[last]).optimize(env);
				effect[i] = null;
				--last;
			}
		}

		// if we removed any lines of code we need to create a new block
		if (Util.contains(effect, null, true)) {
			List<ExecutableChunk> res = new ArrayList<>();
			for (i = 0; i < effect.length; ++i) {
				if (effect[i] != null)
					res.add(effect[i]);
			}
			return res.toArray(Util.CHUNK_EXEC);
		}
		return effect;
	}
}