package com.etk2000.clsl;

public class CLSLVoid extends CLSLValue implements CLSLConst {
	public static final CLSLVoid instance = new CLSLVoid();

	private CLSLVoid() {
		super(ValueType.VOID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CLSLVoid copy() {
		return this;
	}

	@Override
	public CLSLIntConst sizeof() {
		return new CLSLIntConst(0);
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