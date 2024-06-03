package com.etk2000.clsl;

import com.etk2000.clsl.exception.type.ClslConstAssignmentException;
import com.etk2000.clsl.exception.type.ClslConstDecrementException;
import com.etk2000.clsl.exception.type.ClslConstIncrementException;
import com.etk2000.clsl.exception.type.ClslInvalidArrayComponentTypeException;

public class CLSLArrayConst extends CLSLArray implements CLSLConst {
	public CLSLArrayConst(ValueType component, short length) {
		super(component);
		switch (component) {
			case ARRAY:// TODO: allow multidimensional arrays
			case POINTER:// TODO: allow multidimensional pointers
			case VOID:
				throw new ClslInvalidArrayComponentTypeException(CLSL.typeName(type), CLSL.typeName(component));
			case CHAR:
				val = new CLSLCharConst[length];
				break;
			case DOUBLE:
				val = new CLSLDoubleConst[length];
				break;
			case FLOAT:
				val = new CLSLFloatConst[length];
				break;
			case INT:
				val = new CLSLIntConst[length];
				break;
			case LONG:
				val = new CLSLLongConst[length];
				break;
		}
	}

	public CLSLArrayConst(CLSLValue[] val) {
		super(val);
	}

	private CLSLArrayConst(char[] arr) {
		super(ValueType.CHAR);
		val = new CLSLChar[arr.length];
		for (int i = 0; i < arr.length; ++i)
			val[i] = new CLSLChar(arr[i]);
	}

	private CLSLArrayConst(double[] arr) {
		super(ValueType.DOUBLE);
		val = new CLSLDouble[arr.length];
		for (int i = 0; i < arr.length; ++i)
			val[i] = new CLSLDouble(arr[i]);
	}

	private CLSLArrayConst(float[] arr) {
		super(ValueType.FLOAT);
		val = new CLSLFloat[arr.length];
		for (int i = 0; i < arr.length; ++i)
			val[i] = new CLSLFloat(arr[i]);
	}

	private CLSLArrayConst(int[] arr) {
		super(ValueType.INT);
		val = new CLSLInt[arr.length];
		for (int i = 0; i < arr.length; ++i)
			val[i] = new CLSLInt(arr[i]);
	}

	private CLSLArrayConst(long[] arr) {
		super(ValueType.LONG);
		val = new CLSLLong[arr.length];
		for (int i = 0; i < arr.length; ++i)
			val[i] = new CLSLLong(arr[i]);
	}

	// const char*
	// TODO: deal with NULL in the middle of String
	public CLSLArrayConst(String str) {
		super(ValueType.CHAR);
		val = new CLSLCharConst[str.length() + 1];
		for (int i = 0; i < str.length(); ++i)
			val[i] = new CLSLCharConst(str.charAt(i));
		val[val.length - 1] = new CLSLCharConst(0);// NULL termination
	}

	@Override
	public CLSLArray set(CLSLValue other) {
		throw new ClslConstAssignmentException();
	}

	@Override
	public CLSLValue dec(boolean post) {
		throw new ClslConstDecrementException();
	}

	@Override
	public CLSLValue inc(boolean post) {
		throw new ClslConstIncrementException();
	}

	@Override
	public CLSLValue add(CLSLValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.add(other, set);
	}

	@Override
	public CLSLValue div(CLSLValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.div(other, set);
	}

	@Override
	public CLSLValue mod(CLSLValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.mod(other, set);
	}

	@Override
	public CLSLValue mul(CLSLValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.mul(other, set);
	}

	@Override
	public CLSLValue sub(CLSLValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.sub(other, set);
	}

	@Override
	public CLSLValue band(CLSLValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.band(other, set);
	}

	@Override
	public CLSLValue bor(CLSLValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.bor(other, set);
	}

	@Override
	public CLSLValue sl(CLSLValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.sl(other, set);
	}

	@Override
	public CLSLValue sr(CLSLValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.sr(other, set);
	}

	@Override
	public CLSLValue xor(CLSLValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.xor(other, set);
	}

	@Override
	public CLSLArrayConst copy() {
		return this;
	}
}