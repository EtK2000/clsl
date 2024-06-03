package com.etk2000.clsl;

import com.etk2000.clsl.exception.type.ClslConstAssignmentException;
import com.etk2000.clsl.exception.type.ClslConstDecrementException;
import com.etk2000.clsl.exception.type.ClslConstIncrementException;
import com.etk2000.clsl.exception.type.ClslInvalidArrayComponentTypeException;
import com.etk2000.clsl.exception.type.ClslInvalidArraySizeException;

public class CLSLPointerConst extends CLSLPointer implements CLSLConst {
	public CLSLPointerConst(ValueType component, short length) {
		this(component, length, (short) 0);
	}

	public CLSLPointerConst(ValueType component, short length, short index) {
		super(component);
		if (length < 1)
			throw new ClslInvalidArraySizeException();

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
		this.index = index;
	}

	public CLSLPointerConst(CLSLValue[] val) {
		this(val, (short) 0);
	}

	public CLSLPointerConst(CLSLValue[] val, short index) {
		super(val, index);
	}

	// const char*
	public CLSLPointerConst(String str) {
		super(ValueType.CHAR);
		val = new CLSLCharConst[str.length() + 1];
		for (int i = 0; i < str.length(); ++i)
			val[i] = new CLSLCharConst(str.charAt(i));
		val[val.length - 1] = new CLSLCharConst(0);// NULL termination
	}

	@Override
	public CLSLPointer set(CLSLValue other) {
		throw new ClslConstAssignmentException();
	}

	// TODO: test in c
	@Override
	public CLSLPointer dec(boolean post) {
		throw new ClslConstDecrementException();
	}

	// TODO: test in c
	@Override
	public CLSLPointer inc(boolean post) {
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
}