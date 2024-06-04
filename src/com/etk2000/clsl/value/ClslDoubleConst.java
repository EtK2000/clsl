package com.etk2000.clsl.value;

import com.etk2000.clsl.exception.type.ClslConstAssignmentException;
import com.etk2000.clsl.exception.type.ClslConstDecrementException;
import com.etk2000.clsl.exception.type.ClslConstIncrementException;

public class ClslDoubleConst extends ClslDouble implements ClslConst {
	public ClslDoubleConst(double val) {
		super(val);
	}

	@Override
	public ClslDouble set(ClslValue other) {
		throw new ClslConstAssignmentException();
	}

	@Override
	public ClslDouble dec(boolean post) {
		throw new ClslConstDecrementException();
	}

	@Override
	public ClslDouble inc(boolean post) {
		throw new ClslConstIncrementException();
	}

	@Override
	public ClslDouble add(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.add(other, set);
	}

	@Override
	public ClslDouble div(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.div(other, set);
	}

	@Override
	public ClslDouble mod(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.mod(other, set);
	}

	@Override
	public ClslDouble mul(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.mul(other, set);
	}

	@Override
	public ClslDouble sub(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.sub(other, set);
	}

	@Override
	public ClslDoubleConst copy() {
		return this;
	}
}