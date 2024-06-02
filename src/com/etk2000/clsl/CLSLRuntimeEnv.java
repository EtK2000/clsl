package com.etk2000.clsl;

import com.etk2000.clsl.CLSLRuntimeStack.CLSLStackType;

public class CLSLRuntimeEnv {
	private final MainScopeLookupContainer headers;
	private final CLSLRuntimeStack stack;
	final CLSLExternHeaderFinder headerFinder;

	public CLSLRuntimeEnv(CLSLExternHeaderFinder externHeaderFinder, MainScopeLookupContainer headerScopes) {
		this(externHeaderFinder, headerScopes, CLSLRuntimeStack.create(CLSLStackType.array_deque));
	}

	public CLSLRuntimeEnv(CLSLExternHeaderFinder externHeaderFinder, MainScopeLookupContainer headerScopes, CLSLRuntimeStack variableScopes) {
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

	public void defineVar(String name, CLSLValue value) {
		stack.defineVar(name, value);
	}

	// FIXME: don't double lookup if stack.size() == 1
	// FIXME: allow optimization to dictate whether to look in stackFrame or
	// stack.get(0) (global)
	public CLSLValue getVar(String name) {
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
	
	public CLSLRuntimeStack stack() {
		return stack;
	}
}