package com.etk2000.clsl;

import com.etk2000.clsl.exception.type.ClslConstAssignmentException;
import com.etk2000.clsl.exception.type.ClslConstDecrementException;
import com.etk2000.clsl.exception.type.ClslConstIncrementException;

public class CLSLDoubleConst extends CLSLDouble implements CLSLConst {
	public CLSLDoubleConst(double val) {
		super(val);
	}

	@Override
	public CLSLDouble set(CLSLValue other) {
		throw new ClslConstAssignmentException();
	}

	@Override
	public CLSLDouble dec(boolean post) {
		throw new ClslConstDecrementException();
	}

	@Override
	public CLSLDouble inc(boolean post) {
		throw new ClslConstIncrementException();
	}

	@Override
	public CLSLDouble add(CLSLValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.add(other, set);
	}

	@Override
	public CLSLDouble div(CLSLValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.div(other, set);
	}

	@Override
	public CLSLDouble mod(CLSLValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.mod(other, set);
	}

	@Override
	public CLSLDouble mul(CLSLValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.mul(other, set);
	}

	@Override
	public CLSLDouble sub(CLSLValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.sub(other, set);
	}

	@Override
	public CLSLDoubleConst copy() {
		return this;
	}
}