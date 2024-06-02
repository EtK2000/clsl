package com.etk2000.clsl;

// TODO: look into merging sub-classes into this
abstract class ConstValueChunk implements ValueChunk {
	final ValueType type;
	
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