package com.etk2000.clsl;

public class CLSLDoubleConst extends CLSLDouble implements CLSLConst {
	public CLSLDoubleConst(double val) {
		super(val);
	}

	@Override
	public CLSLDouble set(CLSLValue other) {
		throw constAssignment();
	}

	@Override
	public CLSLDouble dec(boolean post) {
		throw constDec();
	}

	@Override
	public CLSLDouble inc(boolean post) {
		throw constInc();
	}

	@Override
	public CLSLDouble add(CLSLValue other, boolean set) {
		if (set)
			throw constAssignment();
		return super.add(other, set);
	}

	@Override
	public CLSLDouble div(CLSLValue other, boolean set) {
		if (set)
			throw constAssignment();
		return super.div(other, set);
	}

	@Override
	public CLSLDouble mod(CLSLValue other, boolean set) {
		if (set)
			throw constAssignment();
		return super.mod(other, set);
	}

	@Override
	public CLSLDouble mul(CLSLValue other, boolean set) {
		if (set)
			throw constAssignment();
		return super.mul(other, set);
	}

	@Override
	public CLSLDouble sub(CLSLValue other, boolean set) {
		if (set)
			throw constAssignment();
		return super.sub(other, set);
	}

	@Override
	public CLSLDoubleConst copy() {
		return this;
	}
}