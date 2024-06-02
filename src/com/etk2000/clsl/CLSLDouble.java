package com.etk2000.clsl;

public class CLSLDouble extends CLSLValue {
	public double val;

	public CLSLDouble() {
		this(Double.NaN);
	}

	public CLSLDouble(double val) {
		super(ValueType.DOUBLE);
		this.val = val;
	}

	@Override
	public CLSLDouble set(CLSLValue other) {
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
				val = other.toDouble();
				break;
		}
		return this;
	}

	@Override
	public CLSLDouble dec(boolean post) {
		if (post)
			return new CLSLDoubleConst(val--);
		--val;
		return this;
	}

	@Override
	public CLSLDouble inc(boolean post) {
		if (post)
			return new CLSLDoubleConst(val++);
		++val;
		return this;
	}

	@Override
	public CLSLDouble add(CLSLValue other, boolean set) {
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
				return new CLSLDoubleConst(val + other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val + other.toDouble());
			case FLOAT:
				return new CLSLDoubleConst(val + other.toFloat());
			case INT:
				return new CLSLDoubleConst(val + other.toInt());
			case LONG:
				return new CLSLDoubleConst(val + other.toLong());
		}
		super.add(other, set);
		return this;
	}

	@Override
	public CLSLDouble div(CLSLValue other, boolean set) {
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
				return new CLSLDoubleConst(val / other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val / other.toDouble());
			case FLOAT:
				return new CLSLDoubleConst(val / other.toFloat());
			case INT:
				return new CLSLDoubleConst(val / other.toInt());
			case LONG:
				return new CLSLDoubleConst(val / other.toLong());
		}
		super.div(other, set);
		return this;
	}

	@Override
	public CLSLDouble mod(CLSLValue other, boolean set) {
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
				return new CLSLDoubleConst(val % other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val % other.toDouble());
			case FLOAT:
				return new CLSLDoubleConst(val % other.toFloat());
			case INT:
				return new CLSLDoubleConst(val % other.toInt());
			case LONG:
				return new CLSLDoubleConst(val % other.toLong());
		}
		super.mod(other, set);
		return this;
	}

	@Override
	public CLSLDouble mul(CLSLValue other, boolean set) {
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
				return new CLSLDoubleConst(val * other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val * other.toDouble());
			case FLOAT:
				return new CLSLDoubleConst(val * other.toFloat());
			case INT:
				return new CLSLDoubleConst(val * other.toInt());
			case LONG:
				return new CLSLDoubleConst(val * other.toLong());
		}
		super.mul(other, set);
		return this;
	}

	@Override
	public CLSLDouble sub(CLSLValue other, boolean set) {
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
				return new CLSLDoubleConst(val - other.toChar());
			case DOUBLE:
				return new CLSLDoubleConst(val - other.toDouble());
			case FLOAT:
				return new CLSLDoubleConst(val - other.toFloat());
			case INT:
				return new CLSLDoubleConst(val - other.toInt());
			case LONG:
				return new CLSLDoubleConst(val - other.toLong());
		}
		super.sub(other, set);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CLSLDoubleConst copy() {
		return new CLSLDoubleConst(val);
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
					return val == other.toDouble();
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
				return val < other.toDouble();
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
				return val <= other.toDouble();
		}
		return super.lte(other);
	}

	@Override
	public CLSLIntConst sizeof() {
		return new CLSLIntConst(8);
	}

	@Override
	public String typeName() {
		return "double";
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
				if (CLSL.doWarn)
					System.out.println("redundant cast from double to double");
				return new CLSLDoubleConst(val);
			case FLOAT:
				return new CLSLFloatConst((float) val);
			case INT:
				return new CLSLIntConst((int) val);
			case LONG:
				return new CLSLLongConst((long) val);
		}
		return super.cast(to);
	}

	@Override
	public String toString() {
		return Double.toString(val);
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
		return (float) val;
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