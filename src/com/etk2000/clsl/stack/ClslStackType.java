package com.etk2000.clsl.stack;

import java.util.function.Supplier;

public enum ClslStackType {
	ARRAY_DEQUE(DequeStack::new);

	public final Supplier<ClslRuntimeStack> createStack;

	ClslStackType(Supplier<ClslRuntimeStack> createStack) {
		this.createStack = createStack;
	}
}