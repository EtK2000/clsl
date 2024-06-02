package com.etk2000.clsl;

public class CLSLIntConst extends CLSLInt implements CLSLConst {
	public CLSLIntConst(int val) {
		super(val);
	}

	@Override
	public CLSLInt set(CLSLValue other) {
		throw new UnsupportedOperationException("lvalue required as left operand of assignment");
	}

	@Override
	public CLSLInt dec(boolean post) {
		throw new UnsupportedOperationException("lvalue required as decrement operand");
	}

	@Override
	public CLSLInt inc(boolean post) {
		throw new UnsupportedOperationException("lvalue required as increment operand");
	}

	@Override
	public CLSLValue add(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.add(other, set);
	}

	@Override
	public CLSLValue div(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.div(other, set);
	}

	@Override
	public CLSLValue mod(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.mod(other, set);
	}

	@Override
	public CLSLValue mul(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.mul(other, set);
	}

	@Override
	public CLSLValue sub(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.sub(other, set);
	}

	@Override
	public CLSLValue band(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.band(other, set);
	}

	@Override
	public CLSLValue bor(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.bor(other, set);
	}

	@Override
	public CLSLInt sl(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.sl(other, set);
	}

	@Override
	public CLSLInt sr(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.sr(other, set);
	}

	@Override
	public CLSLValue xor(CLSLValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.xor(other, set);
	}

	@Override
	public CLSLIntConst copy() {
		return this;
	}
}