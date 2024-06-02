package com.etk2000.clsl;

public class CLSLStructConstWrapped<T> extends CLSLStructWrapped<T> implements CLSLConst {
	public CLSLStructConstWrapped(T o) {
		this(o, DEFAULT_WRAP);
	}

	public CLSLStructConstWrapped(T o, byte wraptype) {
		super(o, wraptype, true);
	}
	
	@Override
	public CLSLStructConstWrapped<T> copy() {
		return this;
	}
}