package com.etk2000.clsl.value;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.exception.type.ClslConstAssignmentException;
import com.etk2000.clsl.exception.type.ClslConstDecrementException;
import com.etk2000.clsl.exception.type.ClslConstIncrementException;
import com.etk2000.clsl.exception.type.ClslInvalidArrayComponentTypeException;

public class ClslArrayConst extends ClslArray implements ClslConst {
	public ClslArrayConst(ValueType component, short length) {
		super(component);
		switch (component) {
			case ARRAY:// TODO: allow multidimensional arrays
			case POINTER:// TODO: allow multidimensional pointers
			case VOID:
				throw new ClslInvalidArrayComponentTypeException(Clsl.typeName(type), Clsl.typeName(component));
			case CHAR:
				val = new ClslCharConst[length];
				break;
			case DOUBLE:
				val = new ClslDoubleConst[length];
				break;
			case FLOAT:
				val = new ClslFloatConst[length];
				break;
			case INT:
				val = new ClslIntConst[length];
				break;
			case LONG:
				val = new ClslLongConst[length];
				break;
		}
	}

	public ClslArrayConst(ClslValue[] val) {
		super(val);
	}

	private ClslArrayConst(char[] arr) {
		super(ValueType.CHAR);
		val = new ClslChar[arr.length];
		for (int i = 0; i < arr.length; ++i)
			val[i] = new ClslChar(arr[i]);
	}

	private ClslArrayConst(double[] arr) {
		super(ValueType.DOUBLE);
		val = new ClslDouble[arr.length];
		for (int i = 0; i < arr.length; ++i)
			val[i] = new ClslDouble(arr[i]);
	}

	private ClslArrayConst(float[] arr) {
		super(ValueType.FLOAT);
		val = new ClslFloat[arr.length];
		for (int i = 0; i < arr.length; ++i)
			val[i] = new ClslFloat(arr[i]);
	}

	private ClslArrayConst(int[] arr) {
		super(ValueType.INT);
		val = new ClslInt[arr.length];
		for (int i = 0; i < arr.length; ++i)
			val[i] = new ClslInt(arr[i]);
	}

	private ClslArrayConst(long[] arr) {
		super(ValueType.LONG);
		val = new ClslLong[arr.length];
		for (int i = 0; i < arr.length; ++i)
			val[i] = new ClslLong(arr[i]);
	}

	// const char*
	// TODO: deal with NULL in the middle of String
	public ClslArrayConst(String str) {
		super(ValueType.CHAR);
		val = new ClslCharConst[str.length() + 1];
		for (int i = 0; i < str.length(); ++i)
			val[i] = new ClslCharConst(str.charAt(i));
		val[val.length - 1] = new ClslCharConst(0);// NULL termination
	}

	@Override
	public ClslArray set(ClslValue other) {
		throw new ClslConstAssignmentException();
	}

	@Override
	public ClslValue dec(boolean post) {
		throw new ClslConstDecrementException();
	}

	@Override
	public ClslValue inc(boolean post) {
		throw new ClslConstIncrementException();
	}

	@Override
	public ClslValue add(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.add(other, set);
	}

	@Override
	public ClslValue div(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.div(other, set);
	}

	@Override
	public ClslValue mod(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.mod(other, set);
	}

	@Override
	public ClslValue mul(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.mul(other, set);
	}

	@Override
	public ClslValue sub(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.sub(other, set);
	}

	@Override
	public ClslValue band(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.band(other, set);
	}

	@Override
	public ClslValue bor(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.bor(other, set);
	}

	@Override
	public ClslValue sl(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.sl(other, set);
	}

	@Override
	public ClslValue sr(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.sr(other, set);
	}

	@Override
	public ClslValue xor(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.xor(other, set);
	}

	@Override
	public ClslArrayConst copy() {
		return this;
	}
}