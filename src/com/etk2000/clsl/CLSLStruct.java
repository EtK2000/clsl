package com.etk2000.clsl;

import com.etk2000.clsl.exception.type.ClslTypeCastException;

public abstract class CLSLStruct extends CLSLValue {
	public CLSLStruct() {
		super(ValueType.STRUCT);
	}

	@Override
	public CLSLStruct set(CLSLValue other) {
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
	public CLSLConst cast(ValueType to) {
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
		throw new ClslTypeCastException(typeName(), CLSL.typeName(to));
	}

	@Override
	public boolean toBoolean() {
		throw new UnsupportedOperationException("CLSLStruct has no boolean value");
	}

	@Override
	public char toChar() {
		throw new UnsupportedOperationException("CLSLStruct has no char value");
	}

	@Override
	public double toDouble() {
		throw new UnsupportedOperationException("CLSLStruct has no double value");
	}

	@Override
	public float toFloat() {
		throw new UnsupportedOperationException("CLSLStruct has no float value");
	}

	@Override
	public int toInt() {
		throw new UnsupportedOperationException("CLSLStruct has no int value");
	}

	@Override
	public long toLong() {
		throw new UnsupportedOperationException("CLSLStruct has no long value");
	}
}