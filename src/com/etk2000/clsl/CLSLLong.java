package com.etk2000.clsl;

public class CLSLLong extends CLSLValue {
	public long val;

	protected CLSLLong() {
		this(0);
	}

	public CLSLLong(long val) {
		super(ValueType.LONG);
		this.val = val;
	}

	@Override
	public CLSLLong set(CLSLValue other) {
		switch (other.type) {
			case ARRAY:
			case STRUCT:
			case VOID:
				super.set(other);
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
				val = other.toLong();
				break;
		}
		return this;
	}

	@Override
	public CLSLLong dec(boolean post) {
		if (post)
			return new CLSLLongConst(val--);
		--val;
		return this;
	}

	@Override
	public CLSLLong inc(boolean post) {
		if (post)
			return new CLSLLongConst(val++);
		++val;
		return this;
	}

	@Override
	public CLSLValue add(CLSLValue other, boolean set) {
		if (set) {
			switch (other.type) {
				case ARRAY:
				case STRUCT:
				case VOID:
					super.add(other, set);
				case CHAR:
					val += other.toChar();
					break;
				case DOUBLE:
					val += other.toDouble();
					break;
				case FLOAT:
					val += other.toFloat();
					break;
				case INT:
					val += other.toInt();
					break;
				case LONG:
					val += other.toLong();
					break;
			}
			return this;
		}
		switch (other.type) {
			case ARRAY:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
				return new CLSLLongConst(val + other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val + other.toDouble());
			case FLOAT:
				return new CLSLDoubleConst(val + other.toFloat());
			case INT:
				return new CLSLLongConst(val + other.toInt());
			case LONG:
				return new CLSLLongConst(val + other.toLong());
		}
		super.add(other, set);
		return this;
	}

	@Override
	public CLSLValue div(CLSLValue other, boolean set) {
		if (set) {
			switch (other.type) {
				case ARRAY:
				case STRUCT:
				case VOID:
					super.div(other, set);
				case CHAR:
					val /= other.toChar();
					break;
				case DOUBLE:
					val /= other.toDouble();
					break;
				case FLOAT:
					val /= other.toFloat();
					break;
				case INT:
					val /= other.toInt();
					break;
				case LONG:
					val /= other.toLong();
					break;
			}
			return this;
		}
		switch (other.type) {
			case ARRAY:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
				return new CLSLLongConst(val / other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val / other.toDouble());
			case FLOAT:
				return new CLSLDoubleConst(val / other.toFloat());
			case INT:
				return new CLSLLongConst(val / other.toInt());
			case LONG:
				return new CLSLLongConst(val / other.toLong());
		}
		super.div(other, set);
		return this;
	}

	@Override
	public CLSLValue mod(CLSLValue other, boolean set) {
		if (set) {
			switch (other.type) {
				case ARRAY:
				case STRUCT:
				case VOID:
					super.mod(other, set);
				case CHAR:
					val %= other.toChar();
					break;
				case DOUBLE:
					val %= other.toDouble();
					break;
				case FLOAT:
					val %= other.toFloat();
					break;
				case INT:
					val %= other.toInt();
					break;
				case LONG:
					val %= other.toLong();
					break;
			}
			return this;
		}
		switch (other.type) {
			case ARRAY:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
				return new CLSLLongConst(val % other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val % other.toDouble());
			case FLOAT:
				return new CLSLDoubleConst(val % other.toFloat());
			case INT:
				return new CLSLLongConst(val % other.toInt());
			case LONG:
				return new CLSLLongConst(val % other.toLong());
		}
		super.mod(other, set);
		return this;
	}

	@Override
	public CLSLValue mul(CLSLValue other, boolean set) {
		if (set) {
			switch (other.type) {
				case ARRAY:
				case STRUCT:
				case VOID:
					super.mul(other, set);
				case CHAR:
					val *= other.toChar();
					break;
				case DOUBLE:
					val *= other.toDouble();
					break;
				case FLOAT:
					val *= other.toFloat();
					break;
				case INT:
					val *= other.toInt();
					break;
				case LONG:
					val *= other.toLong();
					break;
			}
			return this;
		}
		switch (other.type) {
			case ARRAY:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
				return new CLSLLongConst(val * other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val * other.toDouble());
			case FLOAT:
				return new CLSLDoubleConst(val * other.toFloat());
			case INT:
				return new CLSLLongConst(val * other.toInt());
			case LONG:
				return new CLSLLongConst(val * other.toLong());
		}
		super.mul(other, set);
		return this;
	}

	@Override
	public CLSLValue sub(CLSLValue other, boolean set) {
		if (set) {
			switch (other.type) {
				case ARRAY:
				case STRUCT:
				case VOID:
					super.sub(other, set);
				case CHAR:
					val -= other.toChar();
					break;
				case DOUBLE:
					val -= other.toDouble();
					break;
				case FLOAT:
					val -= other.toFloat();
					break;
				case INT:
					val -= other.toInt();
					break;
				case LONG:
					val -= other.toLong();
					break;
			}
			return this;
		}
		switch (other.type) {
			case ARRAY:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
				return new CLSLLongConst(val - other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val - other.toDouble());
			case FLOAT:
				return new CLSLDoubleConst(val - other.toFloat());
			case INT:
				return new CLSLLongConst(val - other.toInt());
			case LONG:
				return new CLSLLongConst(val - other.toLong());
		}
		super.sub(other, set);
		return this;
	}

	@Override
	public CLSLValue band(CLSLValue other, boolean set) {
		if (set) {
			switch (other.type) {
				case ARRAY:
				case DOUBLE:
				case FLOAT:
				case STRUCT:
				case VOID:
					super.band(other, set);
				case CHAR:
					val &= other.toChar();
					break;
				case INT:
					val &= other.toInt();
					break;
				case LONG:
					val &= other.toLong();
					break;
			}
			return this;
		}
		switch (other.type) {
			case ARRAY:
			case DOUBLE:
			case FLOAT:
			case VOID:
				break;
			case CHAR:
				return new CLSLCharConst((char) (val & other.toChar()));
			case INT:
				return new CLSLIntConst((int) (val & other.toInt()));
			case LONG:
				return new CLSLLongConst(val & other.toLong());
		}
		super.band(other, set);
		return this;
	}

	@Override
	public CLSLLong bor(CLSLValue other, boolean set) {
		if (set) {
			switch (other.type) {
				case ARRAY:
				case DOUBLE:
				case FLOAT:
				case STRUCT:
				case VOID:
					super.bor(other, set);
				case CHAR:
					val |= other.toChar();
					break;
				case INT:
					val |= other.toInt();
					break;
				case LONG:
					val |= other.toLong();
					break;
			}
			return this;
		}
		switch (other.type) {
			case ARRAY:
			case DOUBLE:
			case FLOAT:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
				return new CLSLLongConst(val | other.toChar());
			case INT:
				return new CLSLLongConst(val | other.toInt());
			case LONG:
				return new CLSLLongConst(val | other.toLong());
		}
		super.bor(other, set);
		return this;
	}

	@Override
	public CLSLLong sl(CLSLValue other, boolean set) {
		if (set) {
			switch (other.type) {
				case ARRAY:
				case DOUBLE:
				case FLOAT:
				case STRUCT:
				case VOID:
					super.sl(other, set);
				case CHAR:
					val <<= other.toChar();
					break;
				case INT:
					val <<= other.toInt();
					break;
				case LONG:
					val <<= other.toLong();
					break;
			}
			return this;
		}
		switch (other.type) {
			case ARRAY:
			case DOUBLE:
			case FLOAT:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
				return new CLSLLongConst(val << other.toChar());
			case INT:
				return new CLSLLongConst(val << other.toInt());
			case LONG:
				return new CLSLLongConst(val << other.toLong());
		}
		super.sl(other, set);
		return this;
	}

	@Override
	public CLSLLong sr(CLSLValue other, boolean set) {
		if (set) {
			switch (other.type) {
				case ARRAY:
				case DOUBLE:
				case FLOAT:
				case STRUCT:
				case VOID:
					super.sr(other, set);
				case CHAR:
					val >>= other.toChar();
					break;
				case INT:
					val >>= other.toInt();
					break;
				case LONG:
					val >>= other.toLong();
					break;
			}
			return this;
		}
		switch (other.type) {
			case ARRAY:
			case FLOAT:
			case DOUBLE:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
				return new CLSLLongConst(val >> other.toChar());
			case INT:
				return new CLSLLongConst(val >> other.toInt());
			case LONG:
				return new CLSLLongConst(val >> other.toLong());
		}
		super.sr(other, set);
		return this;
	}

	@Override
	public CLSLLong xor(CLSLValue other, boolean set) {
		if (set) {
			switch (other.type) {
				case ARRAY:
				case FLOAT:
				case DOUBLE:
				case STRUCT:
				case VOID:
					super.xor(other, set);
				case CHAR:
					val ^= other.toChar();
					break;
				case INT:
					val ^= other.toInt();
					break;
				case LONG:
					val ^= other.toLong();
					break;
			}
			return this;
		}
		switch (other.type) {
			case ARRAY:
			case DOUBLE:
			case FLOAT:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
				return new CLSLLongConst(val ^ other.toChar());
			case INT:
				return new CLSLLongConst(val ^ other.toInt());
			case LONG:
				return new CLSLLongConst(val ^ other.toLong());
		}
		super.xor(other, set);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CLSLLongConst copy() {
		return new CLSLLongConst(val);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CLSLValue) {
			CLSLValue other = (CLSLValue) obj;
			switch (other.type) {
				case ARRAY:
				case STRUCT:
				case VOID:
					break;
				case CHAR:
				case DOUBLE:
				case FLOAT:
				case INT:
				case LONG:
					return val == other.toLong();
			}
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public boolean lt(CLSLValue other) {
		switch (other.type) {
			case ARRAY:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
				return val < other.toLong();
		}
		return super.lt(other);
	}

	@Override
	public boolean lte(CLSLValue other) {
		switch (other.type) {
			case ARRAY:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
				return val < other.toLong();
		}
		return super.lte(other);
	}

	@Override
	public CLSLIntConst sizeof() {
		return new CLSLIntConst(8);
	}

	@Override
	public String typeName() {
		return "long";
	}

	@Override
	public CLSLConst cast(ValueType to) {
		switch (to) {
			case ARRAY:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
				return new CLSLCharConst((char) val);
			case DOUBLE:
				return new CLSLDoubleConst(val);
			case FLOAT:
				return new CLSLFloatConst(val);
			case INT:
				return new CLSLIntConst((int) val);
			case LONG:
				if (CLSL.doWarn)
					System.out.println("redundant cast from char to char");
				return new CLSLLongConst(val);

		}
		return super.cast(to);
	}

	@Override
	public String toString() {
		return Long.toString(val);
	}

	@Override
	public Object toJava() {
		return val;
	}

	@Override
	public boolean toBoolean() {
		return val != 0;
	}

	@Override
	public char toChar() {
		return (char) val;
	}

	@Override
	public double toDouble() {
		return val;
	}

	@Override
	public float toFloat() {
		return val;
	}

	@Override
	public int toInt() {
		return (int) val;
	}

	@Override
	public long toLong() {
		return val;
	}
}