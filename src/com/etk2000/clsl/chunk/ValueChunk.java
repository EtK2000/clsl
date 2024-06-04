package com.etk2000.clsl.chunk;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.value.ClslValue;

public interface ValueChunk extends ClslChunk {
	ExecutableChunk getExecutablePart(OptimizationEnvironment env);

	ClslValue get(ClslRuntimeEnv env);

	// FIXME: optimize to ValueChunk?
}