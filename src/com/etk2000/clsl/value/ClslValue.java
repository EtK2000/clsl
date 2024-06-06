package com.etk2000.clsl.value;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.exception.type.ClslNotAStructureOrUnionException;
import com.etk2000.clsl.exception.type.ClslTypeCastException;
import com.etk2000.clsl.exception.type.ClslUnsupportedOperatorException;

abstract public class ClslValue {
	public final ValueType type;

	protected ClslValue(ValueType type) {
		this.type = type;
	}

	// solo setters

	public ClslValue set(ClslValue other) {
		throw new ClslUnsupportedOperatorException("=", typeName(), other.typeName());
	}

	public ClslValue dec(boolean post) {
		throw new ClslUnsupportedOperatorException(post ? "()--" : "--()", typeName());
	}

	public ClslValue inc(boolean post) {
		throw new ClslUnsupportedOperatorException(post ? "()++" : "++()", typeName());
	}

	// math [setters]

	public ClslValue add(ClslValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "+=" : "+", typeName(), other.typeName());
	}

	public ClslValue div(ClslValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "/=" : "/", typeName(), other.typeName());
	}

	public ClslValue mod(ClslValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "%=" : "%", typeName(), other.typeName());
	}

	public ClslValue mul(ClslValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "*=" : "*", typeName(), other.typeName());
	}

	public ClslValue sub(ClslValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "-=" : "-", typeName(), other.typeName());
	}

	// binary ops (don't work on floats) [setters]

	public ClslValue band(ClslValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "&=" : "&", typeName(), other.typeName());
	}

	public ClslValue bor(ClslValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "|=" : "|", typeName(), other.typeName());
	}

	public ClslValue sl(ClslValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "<<=" : "<<", typeName(), other.typeName());
	}

	public ClslValue sr(ClslValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? ">>=" : ">>", typeName(), other.typeName());
	}

	public ClslValue xor(ClslValue other, boolean set) {
		throw new ClslUnsupportedOperatorException(set ? "^=" : "^", typeName(), other.typeName());
	}

	// other ops

	public ClslValue dot(String member) {
		throw new ClslNotAStructureOrUnionException(member);
	}

	public ClslValue index(ClslValue index) {
		throw new ClslUnsupportedOperatorException("[]", typeName(), index.typeName());
	}

	// duplicate this

	abstract public <T extends ClslValue & ClslConst> T copy();

	// compare

	public boolean eq(ClslValue other) {
		throw new ClslUnsupportedOperatorException("==", typeName(), other.typeName());
	}

	public boolean lt(ClslValue other) {
		throw new ClslUnsupportedOperatorException("<", typeName(), other.typeName());
	}

	public boolean lte(ClslValue other) {
		throw new ClslUnsupportedOperatorException("<=", typeName(), other.typeName());
	}

	// other useful operations
	abstract public ClslIntConst sizeof();

	abstract public String typeName();

	// TODO: look into using template like what copy() does for its return type
	// LOW: try to change return type to something like: <T extends ClslValue & ClslConst>
	public ClslConst cast(ValueType to) {
		throw new ClslTypeCastException(typeName(), Clsl.typeName(to));
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