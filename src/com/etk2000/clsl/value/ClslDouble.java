package com.etk2000.clsl.value;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ValueType;

public class ClslDouble extends ClslValue {
	public double val;

	public ClslDouble() {
		this(Double.NaN);
	}

	public ClslDouble(double val) {
		super(ValueType.DOUBLE);
		this.val = val;
	}

	@Override
	public ClslDouble set(ClslValue other) {
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
	public ClslDouble dec(boolean post) {
		if (post)
			return new ClslDoubleConst(val--);
		--val;
		return this;
	}

	@Override
	public ClslDouble inc(boolean post) {
		if (post)
			return new ClslDoubleConst(val++);
		++val;
		return this;
	}

	@Override
	public ClslDouble add(ClslValue other, boolean set) {
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
				return new ClslDoubleConst(val + other.toChar());
			case DOUBLE:
				return new ClslDoubleConst(val + other.toDouble());
			case FLOAT:
				return new ClslDoubleConst(val + other.toFloat());
			case INT:
				return new ClslDoubleConst(val + other.toInt());
			case LONG:
				return new ClslDoubleConst(val + other.toLong());
		}
		super.add(other, set);
		return this;
	}

	@Override
	public ClslDouble div(ClslValue other, boolean set) {
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
				return new ClslDoubleConst(val / other.toChar());
			case DOUBLE:
				return new ClslDoubleConst(val / other.toDouble());
			case FLOAT:
				return new ClslDoubleConst(val / other.toFloat());
			case INT:
				return new ClslDoubleConst(val / other.toInt());
			case LONG:
				return new ClslDoubleConst(val / other.toLong());
		}
		super.div(other, set);
		return this;
	}

	@Override
	public ClslDouble mod(ClslValue other, boolean set) {
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
				return new ClslDoubleConst(val % other.toChar());
			case DOUBLE:
				return new ClslDoubleConst(val % other.toDouble());
			case FLOAT:
				return new ClslDoubleConst(val % other.toFloat());
			case INT:
				return new ClslDoubleConst(val % other.toInt());
			case LONG:
				return new ClslDoubleConst(val % other.toLong());
		}
		super.mod(other, set);
		return this;
	}

	@Override
	public ClslDouble mul(ClslValue other, boolean set) {
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
				return new ClslDoubleConst(val * other.toChar());
			case DOUBLE:
				return new ClslDoubleConst(val * other.toDouble());
			case FLOAT:
				return new ClslDoubleConst(val * other.toFloat());
			case INT:
				return new ClslDoubleConst(val * other.toInt());
			case LONG:
				return new ClslDoubleConst(val * other.toLong());
		}
		super.mul(other, set);
		return this;
	}

	@Override
	public ClslDouble sub(ClslValue other, boolean set) {
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
				return new ClslDoubleConst(val - other.toChar());
			case DOUBLE:
				return new ClslDoubleConst(val - other.toDouble());
			case FLOAT:
				return new ClslDoubleConst(val - other.toFloat());
			case INT:
				return new ClslDoubleConst(val - other.toInt());
			case LONG:
				return new ClslDoubleConst(val - other.toLong());
		}
		super.sub(other, set);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ClslDoubleConst copy() {
		return new ClslDoubleConst(val);
	}

	@Override
	public boolean eq(ClslValue other) {
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
		return super.eq(other);
	}

	@Override
	public boolean lt(ClslValue other) {
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
	public boolean lte(ClslValue other) {
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
	public ClslIntConst sizeof() {
		return ClslIntConst.of(8);
	}

	@Override
	public String typeName() {
		return "double";
	}

	@Override
	public ClslConst cast(ValueType to) {
		switch (to) {
			case ARRAY:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
				return new ClslCharConst((char) val);
			case DOUBLE:
				if (Clsl.doWarn)
					System.out.println("redundant cast from double to double");
				return new ClslDoubleConst(val);
			case FLOAT:
				return new ClslFloatConst((float) val);
			case INT:
				return ClslIntConst.of((int) val);
			case LONG:
				return new ClslLongConst((long) val);
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