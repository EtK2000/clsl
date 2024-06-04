package com.etk2000.clsl.chunk;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;

public interface ExecutableChunk extends ClslChunk {
	ReturnChunk execute(ClslRuntimeEnv env);

	@Override
	ExecutableChunk optimize(OptimizationEnvironment env);
}