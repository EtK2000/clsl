package com.etk2000.clsl;

public class CLSLInt extends CLSLValue {
	public int val;

	public CLSLInt() {
		this(0);
	}

	public CLSLInt(int val) {
		super(ValueType.INT);
		this.val = val;
	}

	@Override
	public CLSLInt set(CLSLValue other) {
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
				val = other.toInt();
				return this;
		}
		super.set(other);
		return this;
	}

	@Override
	public CLSLInt dec(boolean post) {
		if (post)
			return new CLSLIntConst(val--);
		--val;
		return this;
	}

	@Override
	public CLSLInt inc(boolean post) {
		if (post)
			return new CLSLIntConst(val++);
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
				return new CLSLIntConst(val + other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val + other.toDouble());
			case FLOAT:
				return new CLSLFloatConst(val + other.toFloat());
			case INT:
				return new CLSLIntConst(val + other.toInt());
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
				return new CLSLIntConst(val / other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val / other.toDouble());
			case FLOAT:
				return new CLSLFloatConst(val / other.toFloat());
			case INT:
				return new CLSLIntConst(val / other.toInt());
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
				return new CLSLIntConst(val % other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val % other.toDouble());
			case FLOAT:
				return new CLSLFloatConst(val % other.toFloat());
			case INT:
				return new CLSLIntConst(val % other.toInt());
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
				return new CLSLIntConst(val * other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val * other.toDouble());
			case FLOAT:
				return new CLSLFloatConst(val * other.toFloat());
			case INT:
				return new CLSLIntConst(val * other.toInt());
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
				return new CLSLIntConst(val - other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val - other.toDouble());
			case FLOAT:
				return new CLSLFloatConst(val - other.toFloat());
			case INT:
				return new CLSLIntConst(val - other.toInt());
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
			case STRUCT:
			case VOID:
				break;
			case CHAR:
				return new CLSLCharConst(val & other.toChar());
			case INT:
				return new CLSLIntConst(val & other.toInt());
			case LONG:
				return new CLSLIntConst((int) (val & other.toLong()));
		}
		super.band(other, set);
		return this;
	}

	@Override
	public CLSLValue bor(CLSLValue other, boolean set) {
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
				return new CLSLIntConst(val | other.toChar());
			case INT:
				return new CLSLIntConst(val | other.toInt());
			case LONG:
				return new CLSLLongConst(val | other.toLong());
		}
		super.bor(other, set);
		return this;
	}

	@Override
	public CLSLInt sl(CLSLValue other, boolean set) {
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
				return new CLSLIntConst(val << other.toChar());
			case INT:
				return new CLSLIntConst(val << other.toInt());
			case LONG:
				return new CLSLIntConst(val << other.toLong());
		}
		super.sl(other, set);
		return this;
	}

	@Override
	public CLSLInt sr(CLSLValue other, boolean set) {
		if (set) {
			switch (other.type) {
				case ARRAY:
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
			case DOUBLE:
			case FLOAT:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
				return new CLSLIntConst(val >> other.toChar());
			case INT:
				return new CLSLIntConst(val >> other.toInt());
			case LONG:
				return new CLSLIntConst(val >> other.toLong());
		}
		super.sr(other, set);
		return this;
	}

	@Override
	public CLSLValue xor(CLSLValue other, boolean set) {
		if (set) {
			switch (other.type) {
				case ARRAY:
				case DOUBLE:
				case FLOAT:
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
				return new CLSLIntConst(val ^ other.toChar());
			case INT:
				return new CLSLIntConst(val ^ other.toInt());
			case LONG:
				return new CLSLLongConst(val ^ other.toLong());
		}
		super.xor(other, set);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CLSLIntConst copy() {
		return new CLSLIntConst(val);
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
					return val == other.toInt();
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
				return val < other.toInt();
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
				return val < other.toInt();
		}
		return super.lte(other);
	}

	@Override
	public CLSLIntConst sizeof() {
		return new CLSLIntConst(4);
	}

	@Override
	public String typeName() {
		return "int";
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
				if (CLSL.doWarn)
					System.out.println("redundant cast from int to int");
				return new CLSLIntConst(val);
			case LONG:
				return new CLSLLongConst(val);
		}
		return super.cast(to);
	}

	@Override
	public String toString() {
		return Integer.toString(val);
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
		return val;
	}

	@Override
	public long toLong() {
		return val;
	}
}