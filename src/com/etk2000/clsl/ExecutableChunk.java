package com.etk2000.clsl;

interface ExecutableChunk extends CLSLChunk {
	ReturnChunk execute(CLSLRuntimeEnv env);

	@Override
	ExecutableChunk optimize(OptimizationEnvironment env);
}