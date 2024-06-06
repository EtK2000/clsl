package com.etk2000.clsl.value;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.exception.type.ClslTypeCastException;

public abstract class ClslStruct extends ClslValue {
	public ClslStruct() {
		super(ValueType.STRUCT);
	}

	@Override
	public ClslStruct set(ClslValue other) {
		switch (other.type) {
			case ARRAY:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case VOID:
				break;
		}
		throw new UnsupportedOperationException(
				"The operator = is undefined for the argument type(s) " + typeName() + ", " + other.typeName());
	}

	@Override
	public ClslConst cast(ValueType to) {
		switch (to) {
			case ARRAY:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case VOID:
				break;
		}
		throw new ClslTypeCastException(typeName(), Clsl.typeName(to));
	}

	@Override
	public boolean toBoolean() {
		throw new UnsupportedOperationException("ClslStruct has no boolean value");
	}

	@Override
	public char toChar() {
		throw new UnsupportedOperationException("ClslStruct has no char value");
	}

	@Override
	public double toDouble() {
		throw new UnsupportedOperationException("ClslStruct has no double value");
	}

	@Override
	public float toFloat() {
		throw new UnsupportedOperationException("ClslStruct has no float value");
	}

	@Override
	public int toInt() {
		throw new UnsupportedOperationException("ClslStruct has no int value");
	}

	@Override
	public long toLong() {
		throw new UnsupportedOperationException("ClslStruct has no long value");
	}
}