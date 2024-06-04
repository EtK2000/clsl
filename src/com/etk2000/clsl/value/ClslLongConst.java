package com.etk2000.clsl.value;

import com.etk2000.clsl.exception.type.ClslConstAssignmentException;
import com.etk2000.clsl.exception.type.ClslConstDecrementException;
import com.etk2000.clsl.exception.type.ClslConstIncrementException;

public class ClslLongConst extends ClslLong implements ClslConst {
	public ClslLongConst(long val) {
		super(val);
	}

	@Override
	public ClslLong set(ClslValue other) {
		throw new ClslConstAssignmentException();
	}

	@Override
	public ClslLong dec(boolean post) {
		throw new ClslConstDecrementException();
	}

	@Override
	public ClslLong inc(boolean post) {
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
	public ClslLong bor(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.bor(other, set);
	}

	@Override
	public ClslLong sl(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.sl(other, set);
	}

	@Override
	public ClslLong sr(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.sr(other, set);
	}

	@Override
	public ClslLong xor(ClslValue other, boolean set) {
		if (set)
			throw new ClslConstAssignmentException();
		return super.xor(other, set);
	}

	@Override
	public ClslLongConst copy() {
		return this;
	}
}