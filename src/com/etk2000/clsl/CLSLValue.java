package com.etk2000.clsl;

abstract public class CLSLValue {
	private static CLSL_RuntimeException unsupportedOperator(String op, String typeName) {
		return new CLSL_RuntimeException("The operator " + op + " is undefined for the argument type(s) " + typeName);
	}

	private static CLSL_RuntimeException unsupportedOperator(String op, String typeName, String otherTypeName) {
		return new CLSL_RuntimeException("The operator " + op + " is undefined for the argument type(s) " + typeName + ", " + otherTypeName);
	}

	protected static CLSL_RuntimeException constAssignment() {
		return new CLSL_RuntimeException("lvalue required as left operand of assignment");
	}

	protected static CLSL_RuntimeException constDec() {
		return new CLSL_RuntimeException("lvalue required as decrement operand");
	}

	protected static CLSL_RuntimeException constInc() {
		return new CLSL_RuntimeException("lvalue required as increment operand");
	}

	public final ValueType type;

	protected CLSLValue(ValueType type) {
		this.type = type;
	}

	// solo setters

	public CLSLValue set(CLSLValue other) {
		throw unsupportedOperator("=", typeName(), other.typeName());
	}

	public CLSLValue dec(boolean post) {
		throw unsupportedOperator(post ? "()--" : "--()", typeName());
	}

	public CLSLValue inc(boolean post) {
		throw unsupportedOperator(post ? "()++" : "++()", typeName());
	}

	// math [setters]

	public CLSLValue add(CLSLValue other, boolean set) {
		throw unsupportedOperator(set ? "+=" : "+", typeName(), other.typeName());
	}

	public CLSLValue div(CLSLValue other, boolean set) {
		throw unsupportedOperator(set ? "/=" : "/", typeName(), other.typeName());
	}

	public CLSLValue mod(CLSLValue other, boolean set) {
		throw unsupportedOperator(set ? "%=" : "%", typeName(), other.typeName());
	}

	public CLSLValue mul(CLSLValue other, boolean set) {
		throw unsupportedOperator(set ? "*=" : "*", typeName(), other.typeName());
	}

	public CLSLValue sub(CLSLValue other, boolean set) {
		throw unsupportedOperator(set ? "-=" : "-", typeName(), other.typeName());
	}

	// binary ops (don't work on floats) [setters]

	public CLSLValue band(CLSLValue other, boolean set) {
		throw unsupportedOperator(set ? "&=" : "&", typeName(), other.typeName());
	}

	public CLSLValue bor(CLSLValue other, boolean set) {
		throw unsupportedOperator(set ? "|=" : "|", typeName(), other.typeName());
	}

	public CLSLValue sl(CLSLValue other, boolean set) {
		throw unsupportedOperator(set ? "<<=" : "<<", typeName(), other.typeName());
	}

	public CLSLValue sr(CLSLValue other, boolean set) {
		throw unsupportedOperator(set ? ">>=" : ">>", typeName(), other.typeName());
	}

	public CLSLValue xor(CLSLValue other, boolean set) {
		throw unsupportedOperator(set ? "^=" : "^", typeName(), other.typeName());
	}

	// other ops

	public CLSLValue dot(String member) {
		throw new CLSL_RuntimeException("request for member '" + member + "' in something not a structure or union");
	}

	public CLSLValue index(CLSLValue index) {
		throw unsupportedOperator("[]", typeName(), index.typeName());
	}

	// duplicate this

	abstract public <T extends CLSLValue & CLSLConst> T copy();

	// compare
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CLSLValue)
			throw unsupportedOperator("==", typeName(), ((CLSLValue) obj).typeName());
		return false;
	}

	public boolean lt(CLSLValue other) {
		throw unsupportedOperator("<", typeName(), other.typeName());
	}

	public boolean lte(CLSLValue other) {
		throw unsupportedOperator("<=", typeName(), other.typeName());
	}

	// other useful operations
	abstract public CLSLIntConst sizeof();

	abstract public String typeName();

	// TODO: look into using template like what copy() does for its return type
	public CLSLConst cast(ValueType to) {
		throw new CLSL_RuntimeException("cannot cast from type " + typeName() + " to " + CLSL.typeName(to));
	}

	@Override
	public abstract String toString();

	abstract public Object toJava();

	public boolean toBoolean() {
		throw new UnsupportedOperationException(typeName() + " has no boolean value");
	}

	public char toChar() {
		throw new UnsupportedOperationException(typeName() + " has no char value");
	}

	public double toDouble() {
		throw new UnsupportedOperationException(typeName() + " has no double value");
	}

	public float toFloat() {
		throw new UnsupportedOperationException(typeName() + " has no float value");
	}

	public int toInt() {
		throw new UnsupportedOperationException(typeName() + " has no int value");
	}

	public long toLong() {
		throw new UnsupportedOperationException(typeName() + " has no long value");
	}
}