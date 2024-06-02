package com.etk2000.clsl;

public class CLSLLongConst extends CLSLLong implements CLSLConst {
	public CLSLLongConst(long val) {
		super(val);
	}

	@Override
	public CLSLLong set(CLSLValue other) {
		throw constAssignment();
	}

	@Override
	public CLSLLong dec(boolean post) {
		throw constDec();
	}

	@Override
	public CLSLLong inc(boolean post) {
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
	public CLSLValue band(CLSLValue other, boolean set) {
		if (set)
			throw constAssignment();
		return super.band(other, set);
	}

	@Override
	public CLSLLong bor(CLSLValue other, boolean set) {
		if (set)
			throw constAssignment();
		return super.bor(other, set);
	}

	@Override
	public CLSLLong sl(CLSLValue other, boolean set) {
		if (set)
			throw constAssignment();
		return super.sl(other, set);
	}

	@Override
	public CLSLLong sr(CLSLValue other, boolean set) {
		if (set)
			throw constAssignment();
		return super.sr(other, set);
	}

	@Override
	public CLSLLong xor(CLSLValue other, boolean set) {
		if (set)
			throw constAssignment();
		return super.xor(other, set);
	}

	@Override
	public CLSLLongConst copy() {
		return this;
	}
}