package com.etk2000.clsl;

import com.etk2000.clsl.ClslRuntimeStack.ClslStackType;
import com.etk2000.clsl.header.ClslExternHeaderFinder;
import com.etk2000.clsl.value.ClslValue;

public class ClslRuntimeEnv {
	public final ClslStructInterop structInterop = new ClslStructInterop();
	public final ClslExternHeaderFinder headerFinder;
	private final ClslRuntimeStack stack;

	public ClslRuntimeEnv(ClslExternHeaderFinder externHeaderFinder) {
		this(externHeaderFinder, ClslRuntimeStack.create(ClslStackType.array_deque));
	}

	public ClslRuntimeEnv(ClslExternHeaderFinder externHeaderFinder, ClslRuntimeStack variableScopes) {
		headerFinder = externHeaderFinder;
		stack = variableScopes;

		pushStack(true);// add global scope
	}

	public void defineVar(String name, ClslValue value) {
		stack.defineVar(name, value);
	}

	// FIXME: don't double lookup if stack.size() == 1
	// FIXME: allow optimization to dictate whether to look in stackFrame or
	// stack.get(0) (global)
	public ClslValue getVar(String name) {
		return stack.getVar(name);
	}

	public void popStack(boolean full) {
		stack.pop(full);
	}

	public void pushStack(boolean full) {
		stack.push(full);
	}

	public void resetProgramScope() {
		stack.wipe();
	}

	public ClslRuntimeStack stack() {
		return stack;
	}
}