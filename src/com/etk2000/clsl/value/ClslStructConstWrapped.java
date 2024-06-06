package com.etk2000.clsl.value;

public class ClslStructConstWrapped<T> extends ClslStructWrapped<T> implements ClslConst {
	public ClslStructConstWrapped(T o) {
		this(o, DEFAULT_WRAP);
	}

	public ClslStructConstWrapped(T o, byte wraptype) {
		super(o, wraptype, true);
	}

	@Override
	public ClslStructConstWrapped<T> copy() {
		return this;
	}
}