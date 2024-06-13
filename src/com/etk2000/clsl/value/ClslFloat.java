package com.etk2000.clsl.value;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ValueType;

public class ClslFloat extends ClslValue {
	public float val;

	public ClslFloat() {
		this(Float.NaN);
	}

	public ClslFloat(float val) {
		super(ValueType.FLOAT);
		this.val = val;
	}

	@Override
	public ClslFloat set(ClslValue other) {
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
	public ClslFloat dec(boolean post) {
		if (post)
			return new ClslFloatConst(val--);
		--val;
		return this;
	}

	@Override
	public ClslFloat inc(boolean post) {
		if (post)
			return new ClslFloatConst(val++);
		++val;
		return this;
	}

	@Override
	public ClslValue add(ClslValue other, boolean set) {
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
				return new ClslFloatConst(val + other.toChar());
			case DOUBLE:
				return new ClslDoubleConst(val + other.toDouble());
			case FLOAT:
				return new ClslFloatConst(val + other.toFloat());
			case INT:
				return new ClslFloatConst(val + other.toInt());
			case LONG:
				return new ClslDoubleConst(val + other.toLong());
		}
		super.add(other, set);
		return this;
	}

	@Override
	public ClslValue div(ClslValue other, boolean set) {
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
				return new ClslFloatConst(val / other.toChar());
			case DOUBLE:
				return new ClslDoubleConst(val / other.toDouble());
			case FLOAT:
				return new ClslFloatConst(val / other.toFloat());
			case INT:
				return new ClslFloatConst(val / other.toInt());
			case LONG:
				return new ClslDoubleConst(val / other.toLong());
		}
		super.div(other, set);
		return this;
	}

	@Override
	public ClslValue mod(ClslValue other, boolean set) {
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
				return new ClslFloatConst(val % other.toChar());
			case DOUBLE:
				return new ClslDoubleConst(val % other.toDouble());
			case FLOAT:
				return new ClslFloatConst(val % other.toFloat());
			case INT:
				return new ClslFloatConst(val % other.toInt());
			case LONG:
				return new ClslDoubleConst(val % other.toLong());
		}
		super.mod(other, set);
		return this;
	}

	@Override
	public ClslValue mul(ClslValue other, boolean set) {
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
				return new ClslFloatConst(val * other.toChar());
			case DOUBLE:
				return new ClslDoubleConst(val * other.toDouble());
			case FLOAT:
				return new ClslFloatConst(val * other.toFloat());
			case INT:
				return new ClslFloatConst(val * other.toInt());
			case LONG:
				return new ClslDoubleConst(val * other.toLong());
		}
		super.mul(other, set);
		return this;
	}

	@Override
	public ClslValue sub(ClslValue other, boolean set) {
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
				return new ClslFloatConst(val - other.toChar());
			case DOUBLE:
				return new ClslDoubleConst(val - other.toDouble());
			case FLOAT:
				return new ClslFloatConst(val - other.toFloat());
			case INT:
				return new ClslFloatConst(val - other.toInt());
			case LONG:
				return new ClslDoubleConst(val - other.toLong());
		}
		super.sub(other, set);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ClslFloatConst copy() {
		return new ClslFloatConst(val);
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
				return val == other.toFloat();
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
				return val < other.toFloat();
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
				return val <= other.toFloat();
		}
		return super.lte(other);
	}

	@Override
	public ClslIntConst sizeof() {
		return ClslIntConst.of(4);
	}

	@Override
	public String typeName() {
		return "float";
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
				return new ClslDoubleConst(val);
			case FLOAT:
				if (Clsl.doWarn)
					System.out.println("redundant cast from float to float");
				return new ClslFloatConst(val);
			case INT:
				return ClslIntConst.of((int) val);
			case LONG:
				return new ClslLongConst((long) val);
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