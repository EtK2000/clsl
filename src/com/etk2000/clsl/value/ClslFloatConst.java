package com.etk2000.clsl.value;

import com.etk2000.clsl.exception.type.ClslConstAssignmentException;
import com.etk2000.clsl.exception.type.ClslConstDecrementException;
import com.etk2000.clsl.exception.type.ClslConstIncrementException;

public class ClslFloatConst extends ClslFloat implements ClslConst {
	public ClslFloatConst(float val) {
		super(val);
	}

	@Override
	public ClslFloat set(ClslValue other) {
		throw new ClslConstAssignmentException();
	}

	@Override
	public ClslFloat dec(boolean post) {
		throw new ClslConstDecrementException();
	}

	@Override
	public ClslFloat inc(boolean post) {
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
	public ClslFloatConst copy() {
		return this;
	}
}