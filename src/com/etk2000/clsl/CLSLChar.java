package com.etk2000.clsl;

public class CLSLChar extends CLSLValue {
	public char val;// TODO: maybe use a byte instead?

	protected CLSLChar() {
		this(0);
	}

	public CLSLChar(int val) {
		this((char) val);
	}

	public CLSLChar(char val) {
		super(ValueType.CHAR);
		this.val = val;
	}

	@Override
	public CLSLChar set(CLSLValue other) {
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
				val = other.toChar();
				break;
		}
		return this;
	}

	@Override
	public CLSLChar dec(boolean post) {
		if (post)
			return new CLSLCharConst(val--);
		--val;
		return this;
	}

	@Override
	public CLSLChar inc(boolean post) {
		if (post)
			return new CLSLCharConst(val++);
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
				return new CLSLCharConst(val + other.toChar());
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
				return new CLSLCharConst(val / other.toChar());
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
				return new CLSLCharConst(val % other.toChar());
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
				return new CLSLCharConst(val * other.toChar());
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
				return new CLSLCharConst(val - other.toChar());
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
			case VOID:
				break;
			case CHAR:
				return new CLSLCharConst(val & other.toChar());
			case INT:
				return new CLSLCharConst(val & other.toInt());
			case LONG:
				return new CLSLCharConst((char) (val & other.toLong()));
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
				return new CLSLCharConst(val | other.toChar());
			case INT:
				return new CLSLIntConst(val | other.toInt());
			case LONG:
				return new CLSLLongConst(val | other.toLong());
		}
		super.bor(other, set);
		return this;
	}

	@Override
	public CLSLValue sl(CLSLValue other, boolean set) {
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
				return new CLSLCharConst(val << other.toChar());
			case INT:
				return new CLSLCharConst(val << other.toInt());
			case LONG:
				return new CLSLCharConst(val << other.toLong());
		}
		super.sl(other, set);
		return this;
	}

	@Override
	public CLSLValue sr(CLSLValue other, boolean set) {
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
			case DOUBLE:
			case FLOAT:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
				return new CLSLCharConst(val >> other.toChar());
			case INT:
				return new CLSLCharConst(val >> other.toInt());
			case LONG:
				return new CLSLCharConst(val >> other.toLong());
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
				return new CLSLCharConst(val ^ other.toChar());
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
	public CLSLCharConst copy() {
		return new CLSLCharConst(val);
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
					return val == other.toChar();
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
				return val < other.toChar();
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
				return val < other.toChar();
		}
		return super.lte(other);
	}

	@Override
	public CLSLIntConst sizeof() {
		return new CLSLIntConst(1);
	}

	@Override
	public String typeName() {
		return "char";
	}

	@Override
	public CLSLConst cast(ValueType to) {
		switch (to) {
			case ARRAY:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
				if (CLSL.doWarn)
					System.out.println("redundant cast from char to char");
				return new CLSLCharConst(val);
			case DOUBLE:
				return new CLSLDoubleConst(val);
			case FLOAT:
				return new CLSLFloatConst(val);
			case INT:
				return new CLSLIntConst(val);
			case LONG:
				return new CLSLLongConst(val);
		}
		return super.cast(to);
	}

	@Override
	public String toString() {
		return Character.toString(val);
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
		return val;
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