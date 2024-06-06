package com.etk2000.clsl.value;

import com.etk2000.clsl.exception.type.ClslConstAssignmentException;
import com.etk2000.clsl.exception.type.ClslConstDecrementException;
import com.etk2000.clsl.exception.type.ClslConstIncrementException;

public class ClslCharConst extends ClslChar implements ClslConst {
	public ClslCharConst(int val) {
		super(val);
	}

	public ClslCharConst(char val) {
		super(val);
	}

	@Override
	public ClslValue add(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.add(other, set);
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
	public ClslCharConst copy() {
		return this;
	}

	@Override
	public ClslChar dec(boolean post) {
		throw new ClslConstDecrementException();
	}

	@Override
	public ClslValue div(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.div(other, set);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;

		return val == ((ClslCharConst) other).val;
	}

	@Override
	public ClslChar inc(boolean post) {
		throw new ClslConstIncrementException();
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
	public ClslChar set(ClslValue other) {
		throw new ClslConstAssignmentException();
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
	public ClslValue sub(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.sub(other, set);
	}

	@Override
	public ClslValue xor(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.xor(other, set);
	}
}