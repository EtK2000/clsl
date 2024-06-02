package com.etk2000.clsl;

import java.util.Arrays;

// TODO: allow arr[i] = x;
// TODO: don't allow calling free() if pointing to not malloc()
// TODO: if first index is null notify user about dangling pointer
public class CLSLPointer extends CLSLArray {
	static final CLSLValue[] NULL = {};
	public short index;

	protected CLSLPointer(ValueType component) {
		super(ValueType.POINTER, component);
	}

	public CLSLPointer(ValueType component, short length) {
		super(ValueType.POINTER, component, length);
	}

	public CLSLPointer(CLSLValue[] val, short index) {
		super(ValueType.POINTER, val);
		this.index = index;
	}

	public void free() {
		for (short i = 0; i < val.length; ++i)
			val[i] = null;// fix dangling pointer
		val = CLSLPointer.NULL;
	}

	@Override
	protected void finalize() throws Throwable {
		if (val != NULL && val[0] != null && CLSL.doWarn)
			System.err.println("pseudo memory leak: " + hashCode());
	}

	@Override
	public CLSLPointer set(CLSLValue other) {
		if (other instanceof CLSLPointer)
			val = ((CLSLPointer) other).val;
		else
			super.set(other);
		return this;
	}

	@Override
	public CLSLPointer dec(boolean post) {
		if (post)
			return new CLSLPointer(val, index--);
		--index;
		return this;
	}

	@Override
	public CLSLValue inc(boolean post) {
		if (post)
			return new CLSLPointer(val, index++);
		++index;
		return this;
	}

	// TODO: test the following operators in c

	@Override
	public CLSLValue add(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator += is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator + is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public CLSLValue div(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator /= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator / is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public CLSLValue mod(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator %= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator % is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public CLSLValue mul(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator *= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator * is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public CLSLValue sub(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator -= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator - is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public CLSLValue band(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator &= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator & is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public CLSLValue bor(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator |= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator | is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public CLSLValue sl(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator <<= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator << is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public CLSLValue sr(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator >>= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator >> is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public CLSLValue xor(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException(
					"The operator ^= is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
		throw new UnsupportedOperationException(
				"The operator ^ is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public CLSLValue index(CLSLValue index) {
		switch (index.type) {
		case ARRAY:
		case DOUBLE:
		case FLOAT:
		case POINTER:
		case STRUCT:
		case VOID:
			break;
		case CHAR:
		case INT:
		case LONG:
			return val[index.toChar()];// max arr len is same as char
		}
		return super.index(index);
	}

	@Override
	public CLSLArrayConst copy() {
		return new CLSLArrayConst(val);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CLSLValue)
			throw new UnsupportedOperationException("The operator == is undefined for the argument type(s) "
					+ typeName() + ", " + ((CLSLValue) obj).typeName());
		return false;
	}

	@Override
	public CLSLIntConst sizeof() {
		return new CLSLIntConst(4);
	}

	@Override
	public String typeName() {
		return component.name() + " *";
	}

	@Override
	public CLSLConst cast(ValueType to) {
		switch (type) {
		case ARRAY:// FIXME: deal with component types
			// FIXME: start from index
			return new CLSLArrayConst(val);
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case VOID:
			break;
		case POINTER:// FIXME: deal with component types
			if (CLSL.doWarn)
				System.out.println("redundant cast from pointer to pointer");
			return new CLSLPointerConst(val, index);
		}
		return super.cast(to);
	}

	// TODO: show toString as { ... } and not [ ... ]?
	@Override
	public String toString() {
		if (component == ValueType.CHAR) {
			try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
				for (short i = 0; i < val.length; ++i)
					sb.append(val[i].toChar());
				return sb.toString();
			}
		}
		return Arrays.toString(val);
	}

	// TODO: find a way to not require creating new arrays every time
	@Override
	public Object toJava() {
		switch (component) {
		case CHAR:// TODO: optimize this, i.e. don't do 2 fors
			try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
				char c = 255;
				for (short i = index; i < val.length; ++i) {
					c = val[i].toChar();
					if (c == 0)
						break;// hit NULL termination
					sb.append(c);
				}
				if (c != 0) {// no NULL termination
					char[] res = new char[val.length - index];
					for (short i = 0; i < res.length; ++i)
						res[i] = val[i + index].toChar();
					return res;
				}
				return sb.toString();
			}
		case DOUBLE: {
			double[] res = new double[val.length - index];
			for (short i = 0; i < res.length; ++i)
				res[i] = val[i + index].toDouble();
			return res;
		}
		case FLOAT: {
			float[] res = new float[val.length - index];
			for (short i = 0; i < res.length; ++i)
				res[i] = val[i + index].toFloat();
			return res;
		}
		case INT: {
			int[] res = new int[val.length - index];
			for (short i = 0; i < res.length; ++i)
				res[i] = val[i + index].toInt();
			return res;
		}
		case LONG: {
			long[] res = new long[val.length - index];
			for (short i = 0; i < res.length; ++i)
				res[i] = val[i + index].toLong();
			return res;
		}
		}
		throw new UnsupportedOperationException("CLSLPointer(" + CLSL.typeName(component) + ") not supported yet");
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
}