package com.etk2000.clsl.value;

public class ClslIntConst extends ClslInt implements ClslConst {
	public ClslIntConst(int val) {
		super(val);
	}

	@Override
	public ClslInt set(ClslValue other) {
		throw new UnsupportedOperationException("lvalue required as left operand of assignment");
	}

	@Override
	public ClslInt dec(boolean post) {
		throw new UnsupportedOperationException("lvalue required as decrement operand");
	}

	@Override
	public ClslInt inc(boolean post) {
		throw new UnsupportedOperationException("lvalue required as increment operand");
	}

	@Override
	public ClslValue add(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.add(other, set);
	}

	@Override
	public ClslValue div(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.div(other, set);
	}

	@Override
	public ClslValue mod(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.mod(other, set);
	}

	@Override
	public ClslValue mul(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.mul(other, set);
	}

	@Override
	public ClslValue sub(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.sub(other, set);
	}

	@Override
	public ClslValue band(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.band(other, set);
	}

	@Override
	public ClslValue bor(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.bor(other, set);
	}

	@Override
	public ClslInt sl(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.sl(other, set);
	}

	@Override
	public ClslInt sr(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.sr(other, set);
	}

	@Override
	public ClslValue xor(ClslValue other, boolean set) {
		if (set)
			throw new UnsupportedOperationException("lvalue required as left operand of assignment");
		return super.xor(other, set);
	}

	@Override
	public ClslIntConst copy() {
		return this;
	}
}