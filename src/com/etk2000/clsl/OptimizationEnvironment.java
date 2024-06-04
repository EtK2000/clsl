package com.etk2000.clsl;

import java.util.List;

public class OptimizationEnvironment {
	private final OptimizationEnvironment _forValue;
	public final List<String> unusedVars;
	public final boolean isFirstPass;
	public final boolean forValue;

	public OptimizationEnvironment(List<String> unusedVars) {
		this(unusedVars, true, false);
	}

	private OptimizationEnvironment(List<String> unusedVars, boolean isFirstPass, boolean forValue) {
		this.forValue = forValue;
		this.isFirstPass = isFirstPass;
		this.unusedVars = unusedVars;

		_forValue = forValue ? this : new OptimizationEnvironment(unusedVars, isFirstPass, true);
	}

	public OptimizationEnvironment forValue() {
		return _forValue;
	}

	public OptimizationEnvironment secondPass() {
		return new OptimizationEnvironment(unusedVars, false, false);
	}
}