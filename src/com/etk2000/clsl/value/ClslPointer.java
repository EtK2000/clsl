package com.etk2000.clsl.value;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ValueType;

// TODO: allow arr[i] = x;
// TODO: don't allow calling free() if pointing to not malloc()
// TODO: if first index is null notify user about dangling pointer
public class ClslPointer extends ClslArray {
	static final ClslValue[] NULL = {};
	public short index;

	protected ClslPointer(ValueType component) {
		super(ValueType.POINTER, component);
	}

	public ClslPointer(ValueType component, short length) {
		super(ValueType.POINTER, component, length);
	}

	public ClslPointer(ClslValue[] val, short index) {
		super(ValueType.POINTER, val);
		this.index = index;
	}

	@Override
	public ClslValue add(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator += is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator + is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public ClslValue band(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator &= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator & is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public ClslValue bor(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator |= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator | is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public ClslPointer dec(boolean post) {
		if (post)
			return new ClslPointer(val, index--);
		--index;
		return this;
	}

	@Override
	public ClslValue div(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator /= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator / is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public boolean eq(ClslValue other) {
		throw new UnsupportedOperationException(
				"The operator == is undefined for the argument type(s) " + typeName() + ", " + other.typeName()
		);
	}

	@Override
	protected void finalize() {
		if (val != NULL && val[0] != null && Clsl.doWarn)
			System.err.println("pseudo memory leak: " + hashCode());
	}

	public void free() {
		for (short i = 0; i < val.length; ++i)
			val[i] = null;// fix dangling pointer
		val = ClslPointer.NULL;
	}

	@Override
	public ClslValue inc(boolean post) {
		if (post)
			return new ClslPointer(val, index++);
		++index;
		return this;
	}

	@Override
	public ClslValue mod(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator %= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator % is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public ClslValue mul(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator *= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator * is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public ClslPointer set(ClslValue other) {
		if (other instanceof ClslPointer)
			val = ((ClslPointer) other).val;
		else
			super.set(other);
		return this;
	}

	@Override
	public ClslIntConst sizeof() {
		return ClslIntConst.of(4);
	}

	@Override
	public ClslValue sl(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator <<= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator << is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public ClslValue sr(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator >>= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator >> is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public ClslValue sub(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator -= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator - is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public boolean toBoolean() {
		return val != NULL;
	}

	@Override
	public char toChar() {
		return val != NULL ? (char) hashCode() : 0;
	}

	@Override
	public float toFloat() {
		return val != NULL ? hashCode() : 0;
	}

	@Override
	public int toInt() {
		return val != NULL ? hashCode() : 0;
	}

	@Override
	public String typeName() {
		return component.name() + " *";
	}

	@Override
	public ClslValue xor(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator ^= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator ^ is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}
}