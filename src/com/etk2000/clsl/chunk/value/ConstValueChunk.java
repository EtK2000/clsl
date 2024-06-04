package com.etk2000.clsl.chunk.value;

import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;

// TODO: look into merging sub-classes into this
public abstract class ConstValueChunk implements ValueChunk {
	public final ValueType type;

	protected ConstValueChunk(ValueType type) {
		this.type = type;
	}

	@Override
	final public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return null;
	}

	@Override
	final public ConstValueChunk optimize(OptimizationEnvironment env) {
		return this;
	}
}