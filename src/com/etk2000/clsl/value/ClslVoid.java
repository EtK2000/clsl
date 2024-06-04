package com.etk2000.clsl.value;

import com.etk2000.clsl.ValueType;

public class ClslVoid extends ClslValue implements ClslConst {
	public static final ClslVoid instance = new ClslVoid();

	private ClslVoid() {
		super(ValueType.VOID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ClslVoid copy() {
		return this;
	}

	@Override
	public ClslIntConst sizeof() {
		return new ClslIntConst(0);
	}

	@Override
	public String typeName() {
		return "void";
	}

	@Override
	public String toString() {
		return "(void)";
	}

	@Override
	public Void toJava() {
		return null;
	}
}