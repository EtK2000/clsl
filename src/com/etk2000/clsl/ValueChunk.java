package com.etk2000.clsl;

interface ValueChunk extends CLSLChunk {
	ExecutableChunk getExecutablePart(OptimizationEnvironment env);

	CLSLValue get(CLSLRuntimeEnv env);
	
	// FIXME: optimize to valuechunk?
}