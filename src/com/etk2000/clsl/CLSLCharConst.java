package com.etk2000.clsl;

import com.etk2000.clsl.exception.type.ClslConstAssignmentException;
import com.etk2000.clsl.exception.type.ClslConstDecrementException;
import com.etk2000.clsl.exception.type.ClslConstIncrementException;

public class CLSLCharConst extends CLSLChar implements CLSLConst {
	public CLSLCharConst(int val) {
		super(val);
	}

	public CLSLCharConst(char val) {
		super(val);
	}

	@Override
	public CLSLChar set(CLSLValue other) {
		throw new ClslConstAssignmentException();
	}

	@Override
	public CLSLChar dec(boolean post) {
		throw new ClslConstDecrementException();
	}

	@Override
	public CLSLChar inc(boolean post) {
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
	public CLSLCharConst copy() {
		return this;
	}
}