package com.etk2000.clsl.chunk;

import com.etk2000.clsl.OptimizationEnvironment;

public interface VariableAccess extends ValueChunk {
	String getVariableName();

	@Override
	VariableAccess optimize(OptimizationEnvironment env);
}