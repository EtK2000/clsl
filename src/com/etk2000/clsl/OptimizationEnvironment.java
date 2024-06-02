package com.etk2000.clsl;

import java.util.List;

class OptimizationEnvironment {
	private final OptimizationEnvironment _forValue;
	final List<String> unusedVars;
	final boolean firstPass, forValue;
	
	OptimizationEnvironment(List<String> unusedVars) {
		this(unusedVars, true, false);
	}
	
	private OptimizationEnvironment(List<String> unusedVars, boolean firstPass, boolean forValue) {
		this.unusedVars = unusedVars;
		this.firstPass = firstPass;
		this.forValue = forValue;
		
		_forValue = forValue ? this : new OptimizationEnvironment(unusedVars, firstPass, true);
	}
	
	OptimizationEnvironment forValue() {
		return _forValue;
	}
	
	OptimizationEnvironment secondPass() {
		return new OptimizationEnvironment(unusedVars, false, false);
	}
}