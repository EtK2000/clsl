package com.etk2000.clsl;

public class CLSLFloat extends CLSLValue {
	public float val;

	public CLSLFloat() {
		this(Float.NaN);
	}

	public CLSLFloat(float val) {
		super(ValueType.FLOAT);
		this.val = val;
	}

	@Override
	public CLSLFloat set(CLSLValue other) {
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
				val = other.toFloat();
				break;
		}
		return this;
	}

	@Override
	public CLSLFloat dec(boolean post) {
		if (post)
			return new CLSLFloatConst(val--);
		--val;
		return this;
	}

	@Override
	public CLSLFloat inc(boolean post) {
		if (post)
			return new CLSLFloatConst(val++);
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
				return new CLSLFloatConst(val + other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val + other.toDouble());
			case FLOAT:
				return new CLSLFloatConst(val + other.toFloat());
			case INT:
				return new CLSLFloatConst(val + other.toInt());
			case LONG:
				return new CLSLDoubleConst(val + other.toLong());
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
				return new CLSLFloatConst(val / other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val / other.toDouble());
			case FLOAT:
				return new CLSLFloatConst(val / other.toFloat());
			case INT:
				return new CLSLFloatConst(val / other.toInt());
			case LONG:
				return new CLSLDoubleConst(val / other.toLong());
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
				return new CLSLFloatConst(val % other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val % other.toDouble());
			case FLOAT:
				return new CLSLFloatConst(val % other.toFloat());
			case INT:
				return new CLSLFloatConst(val % other.toInt());
			case LONG:
				return new CLSLDoubleConst(val % other.toLong());
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
				return new CLSLFloatConst(val * other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val * other.toDouble());
			case FLOAT:
				return new CLSLFloatConst(val * other.toFloat());
			case INT:
				return new CLSLFloatConst(val * other.toInt());
			case LONG:
				return new CLSLDoubleConst(val * other.toLong());
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
				return new CLSLFloatConst(val - other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val - other.toDouble());
			case FLOAT:
				return new CLSLFloatConst(val - other.toFloat());
			case INT:
				return new CLSLFloatConst(val - other.toInt());
			case LONG:
				return new CLSLDoubleConst(val - other.toLong());
		}
		super.sub(other, set);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CLSLFloatConst copy() {
		return new CLSLFloatConst(val);
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
					return val == other.toFloat();
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
				return val < other.toFloat();
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
				return val <= other.toFloat();
		}
		return super.lte(other);
	}

	@Override
	public CLSLIntConst sizeof() {
		return new CLSLIntConst(4);
	}

	@Override
	public String typeName() {
		return "float";
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
				if (CLSL.doWarn)
					System.out.println("redundant cast from float to float");
				return new CLSLFloatConst(val);
			case INT:
				return new CLSLIntConst((int) val);
			case LONG:
				return new CLSLLongConst((long) val);
		}
		return super.cast(to);
	}

	@Override
	public String toString() {
		return Float.toString(val);
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
		return (long) val;
	}
}