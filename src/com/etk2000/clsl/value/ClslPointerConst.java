package com.etk2000.clsl.value;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.exception.type.ClslConstAssignmentException;
import com.etk2000.clsl.exception.type.ClslConstDecrementException;
import com.etk2000.clsl.exception.type.ClslConstIncrementException;
import com.etk2000.clsl.exception.type.ClslInvalidArrayComponentTypeException;
import com.etk2000.clsl.exception.type.ClslInvalidArraySizeException;

public class ClslPointerConst extends ClslPointer implements ClslConst {
	public ClslPointerConst(ValueType component, short length) {
		this(component, length, (short) 0);
	}

	public ClslPointerConst(ValueType component, short length, short index) {
		super(component);
		if (length < 1)
			throw new ClslInvalidArraySizeException();

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
		this.index = index;
	}

	public ClslPointerConst(ClslValue[] val) {
		this(val, (short) 0);
	}

	public ClslPointerConst(ClslValue[] val, short index) {
		super(val, index);
	}

	// const char*
	public ClslPointerConst(String str) {
		super(ValueType.CHAR);
		val = new ClslCharConst[str.length() + 1];
		for (int i = 0; i < str.length(); ++i)
			val[i] = new ClslCharConst(str.charAt(i));
		val[val.length - 1] = new ClslCharConst(0);// NULL termination
	}

	@Override
	public ClslPointer set(ClslValue other) {
		throw new ClslConstAssignmentException();
	}

	// TODO: test in c
	@Override
	public ClslPointer dec(boolean post) {
		throw new ClslConstDecrementException();
	}

	// TODO: test in c
	@Override
	public ClslPointer inc(boolean post) {
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
}