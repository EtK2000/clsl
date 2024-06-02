package com.etk2000.clsl;

public class CLSLFloatConst extends CLSLFloat implements CLSLConst {
	public CLSLFloatConst(float val) {
		super(val);
	}

	@Override
	public CLSLFloat set(CLSLValue other) {
		throw constAssignment();
	}

	@Override
	public CLSLFloat dec(boolean post) {
		throw constDec();
	}

	@Override
	public CLSLFloat inc(boolean post) {
		throw constInc();
	}

	@Override
	public CLSLValue add(CLSLValue other, boolean set) {
		if (set)
			throw constAssignment();
		return super.add(other, set);
	}

	@Override
	public CLSLValue div(CLSLValue other, boolean set) {
		if (set)
			throw constAssignment();
		return super.div(other, set);
	}

	@Override
	public CLSLValue mod(CLSLValue other, boolean set) {
		if (set)
			throw constAssignment();
		return super.mod(other, set);
	}

	@Override
	public CLSLValue mul(CLSLValue other, boolean set) {
		if (set)
			throw constAssignment();
		return super.mul(other, set);
	}

	@Override
	public CLSLValue sub(CLSLValue other, boolean set) {
		if (set)
			throw constAssignment();
		return super.sub(other, set);
	}

	@Override
	public CLSLFloatConst copy() {
		return this;
	}
}