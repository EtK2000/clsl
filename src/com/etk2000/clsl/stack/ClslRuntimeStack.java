package com.etk2000.clsl.stack;

import com.etk2000.clsl.value.ClslValue;

import java.util.Collection;

public abstract class ClslRuntimeStack {
	public abstract void defineVar(String name, ClslValue value);

	public abstract Collection<? extends StackFrame> frames();

	public abstract ClslValue getVar(String name);

	public abstract void pop(boolean full);

	public abstract void push(boolean full);

	public abstract void wipe();
}