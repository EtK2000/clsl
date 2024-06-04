package com.etk2000.clsl.value;

public class ClslLStructConstWrapped<T> extends ClslLStructWrapped<T> implements ClslConst {
	public ClslLStructConstWrapped(T o) {
		this(o, DEFAULT_WRAP);
	}

	public ClslLStructConstWrapped(T o, byte wraptype) {
		super(o, wraptype, true);
	}

	@Override
	public ClslLStructConstWrapped<T> copy() {
		return this;
	}
}