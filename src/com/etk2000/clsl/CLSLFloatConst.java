package com.etk2000.clsl;

import com.etk2000.clsl.exception.type.ClslConstAssignmentException;
import com.etk2000.clsl.exception.type.ClslConstDecrementException;
import com.etk2000.clsl.exception.type.ClslConstIncrementException;

public class CLSLFloatConst extends CLSLFloat implements CLSLConst {
	public CLSLFloatConst(float val) {
		super(val);
	}

	@Override
	public CLSLFloat set(CLSLValue other) {
		throw new ClslConstAssignmentException();
	}

	@Override
	public CLSLFloat dec(boolean post) {
		throw new ClslConstDecrementException();
	}

	@Override
	public CLSLFloat inc(boolean post) {
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
	public CLSLFloatConst copy() {
		return this;
	}
}