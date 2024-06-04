package com.etk2000.clsl;

import com.etk2000.clsl.ClslRuntimeStack.ClslStackType;
import com.etk2000.clsl.chunk.FunctionalChunk;
import com.etk2000.clsl.header.ClslExternHeaderFinder;
import com.etk2000.clsl.value.ClslValue;

public class ClslRuntimeEnv {
	private final MainScopeLookupContainer headers;
	private final ClslRuntimeStack stack;
	public final ClslExternHeaderFinder headerFinder;

	public ClslRuntimeEnv(ClslExternHeaderFinder externHeaderFinder, MainScopeLookupContainer headerScopes) {
		this(externHeaderFinder, headerScopes, ClslRuntimeStack.create(ClslStackType.array_deque));
	}

	public ClslRuntimeEnv(ClslExternHeaderFinder externHeaderFinder, MainScopeLookupContainer headerScopes, ClslRuntimeStack variableScopes) {
		headerFinder = externHeaderFinder;
		headers = headerScopes;
		stack = variableScopes;

		pushStack(true);// add global scope
	}

	public void addFunction(String name, FunctionalChunk func) {
		headers.put(name, func);
	}

	public void addHeader(FunctionLookupTable header) {
		headers.add(header);
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

	public FunctionalChunk lookupFunction(String function) {
		return headers.lookup(function);
	}

	public void popStack(boolean full) {
		stack.pop(full);
	}

	public void pushStack(boolean full) {
		stack.push(full);
	}

	public void resetProgramScope(boolean wipeHeadersAndStack) {
		headers.empty(wipeHeadersAndStack);

		if (wipeHeadersAndStack)
			stack.wipe();
	}

	public ClslRuntimeStack stack() {
		return stack;
	}
}