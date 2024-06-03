package com.etk2000.clsl;

import com.etk2000.clsl.exception.type.ClslNotAStructureOrUnionException;
import com.etk2000.clsl.exception.type.ClslTypeCastException;
import com.etk2000.clsl.exception.type.ClslUnsupportedOperatorException;

abstract public class CLSLValue {
	public final ValueType type;

	protected CLSLValue(ValueType type) {
		this.type = type;
	}

	// solo setters

	public CLSLValue set(CLSLValue other) {
		throw new ClslUnsupportedOperatorException("=", typeName(), other.typeName());
	}

	public CLSLValue dec(boolean post) {
		throw new ClslUnsupportedOperatorException(post ? "()--" : "--()", typeName());
	}

	public CLSLValue inc(boolean post) {
		throw new ClslUnsupportedOperatorException(post ? "()++" : "++()", typeName());
	}

	// math [setters]

	public CLSLValue add(CLSLValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "+=" : "+", typeName(), other.typeName());
	}

	public CLSLValue div(CLSLValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "/=" : "/", typeName(), other.typeName());
	}

	public CLSLValue mod(CLSLValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "%=" : "%", typeName(), other.typeName());
	}

	public CLSLValue mul(CLSLValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "*=" : "*", typeName(), other.typeName());
	}

	public CLSLValue sub(CLSLValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "-=" : "-", typeName(), other.typeName());
	}

	// binary ops (don't work on floats) [setters]

	public CLSLValue band(CLSLValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "&=" : "&", typeName(), other.typeName());
	}

	public CLSLValue bor(CLSLValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "|=" : "|", typeName(), other.typeName());
	}

	public CLSLValue sl(CLSLValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "<<=" : "<<", typeName(), other.typeName());
	}

	public CLSLValue sr(CLSLValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? ">>=" : ">>", typeName(), other.typeName());
	}

	public CLSLValue xor(CLSLValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "^=" : "^", typeName(), other.typeName());
	}

	// other ops

	public CLSLValue dot(String member) {
		throw new ClslNotAStructureOrUnionException(member);
	}

	public CLSLValue index(CLSLValue index) {
		throw new ClslUnsupportedOperatorException("[]", typeName(), index.typeName());
	}

	// duplicate this

	abstract public <T extends CLSLValue & CLSLConst> T copy();

	// compare
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CLSLValue)
			throw new ClslUnsupportedOperatorException("==", typeName(), ((CLSLValue) obj).typeName());
		return false;
	}

	public boolean lt(CLSLValue other) {
		throw new ClslUnsupportedOperatorException("<", typeName(), other.typeName());
	}

	public boolean lte(CLSLValue other) {
		throw new ClslUnsupportedOperatorException("<=", typeName(), other.typeName());
	}

	// other useful operations
	abstract public CLSLIntConst sizeof();

	abstract public String typeName();

	// TODO: look into using template like what copy() does for its return type
	public CLSLConst cast(ValueType to) {
		throw new ClslTypeCastException(typeName(), CLSL.typeName(to));
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