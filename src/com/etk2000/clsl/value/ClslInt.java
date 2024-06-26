package com.etk2000.clsl.value;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ValueType;

public class ClslInt extends ClslValue {
	public int val;

	public ClslInt() {
		this(0);
	}

	public ClslInt(int val) {
		super(ValueType.INT);
		this.val = val;
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
				return ClslIntConst.of(val + other.toChar());
			case DOUBLE:
				return new ClslDoubleConst(val + other.toDouble());
			case FLOAT:
				return new ClslFloatConst(val + other.toFloat());
			case INT:
				return ClslIntConst.of(val + other.toInt());
			case LONG:
				return new ClslLongConst(val + other.toLong());
		}
		super.add(other, set);
		return this;
	}

	@Override
	public ClslValue band(ClslValue other, boolean set) {
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
				return new ClslCharConst(val & other.toChar());
			case INT:
				return ClslIntConst.of(val & other.toInt());
			case LONG:
				return ClslIntConst.of((int) (val & other.toLong()));
		}
		super.band(other, set);
		return this;
	}

	@Override
	public ClslValue bor(ClslValue other, boolean set) {
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
				return ClslIntConst.of(val | other.toChar());
			case INT:
				return ClslIntConst.of(val | other.toInt());
			case LONG:
				return new ClslLongConst(val | other.toLong());
		}
		super.bor(other, set);
		return this;
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
				return new ClslFloatConst(val);
			case INT:
				if (Clsl.doWarn)
					System.out.println("redundant cast from int to int");
				return ClslIntConst.of(val);
			case LONG:
				return new ClslLongConst(val);
		}
		return super.cast(to);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ClslIntConst copy() {
		return ClslIntConst.of(val);
	}

	@Override
	public ClslInt dec(boolean post) {
		if (post)
			return ClslIntConst.of(val--);
		--val;
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
				return ClslIntConst.of(val / other.toChar());
			case DOUBLE:
				return new ClslDoubleConst(val / other.toDouble());
			case FLOAT:
				return new ClslFloatConst(val / other.toFloat());
			case INT:
				return ClslIntConst.of(val / other.toInt());
			case LONG:
				return new ClslLongConst(val / other.toLong());
		}
		super.div(other, set);
		return this;
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
				return val == other.toInt();
		}
		return super.eq(other);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;

		return val == ((ClslInt) other).val;
	}

	@Override
	public ClslInt inc(boolean post) {
		if (post)
			return ClslIntConst.of(val++);
		++val;
		return this;
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
				return val < other.toInt();
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
				return val <= other.toInt();
		}
		return super.lte(other);
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
				return ClslIntConst.of(val % other.toChar());
			case DOUBLE:
				return new ClslDoubleConst(val % other.toDouble());
			case FLOAT:
				return new ClslFloatConst(val % other.toFloat());
			case INT:
				return ClslIntConst.of(val % other.toInt());
			case LONG:
				return new ClslLongConst(val % other.toLong());
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
				return ClslIntConst.of(val * other.toChar());
			case DOUBLE:
				return new ClslDoubleConst(val * other.toDouble());
			case FLOAT:
				return new ClslFloatConst(val * other.toFloat());
			case INT:
				return ClslIntConst.of(val * other.toInt());
			case LONG:
				return new ClslLongConst(val * other.toLong());
		}
		super.mul(other, set);
		return this;
	}

	@Override
	public ClslInt set(ClslValue other) {
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
	public ClslIntConst sizeof() {
		return ClslIntConst.of(4);
	}

	@Override
	public ClslInt sl(ClslValue other, boolean set) {
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
				return ClslIntConst.of(val << other.toChar());
			case INT:
				return ClslIntConst.of(val << other.toInt());
			case LONG:
				return ClslIntConst.of(val << other.toLong());
		}
		super.sl(other, set);
		return this;
	}

	@Override
	public ClslInt sr(ClslValue other, boolean set) {
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
				return ClslIntConst.of(val >> other.toChar());
			case INT:
				return ClslIntConst.of(val >> other.toInt());
			case LONG:
				return ClslIntConst.of(val >> other.toLong());
		}
		super.sr(other, set);
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
				return ClslIntConst.of(val - other.toChar());
			case DOUBLE:
				return new ClslDoubleConst(val - other.toDouble());
			case FLOAT:
				return new ClslFloatConst(val - other.toFloat());
			case INT:
				return ClslIntConst.of(val - other.toInt());
			case LONG:
				return new ClslLongConst(val - other.toLong());
		}
		super.sub(other, set);
		return this;
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
	public Object toJava() {
		return val;
	}

	@Override
	public long toLong() {
		return val;
	}

	@Override
	public String toString() {
		return Integer.toString(val);
	}

	@Override
	public String typeName() {
		return "int";
	}

	@Override
	public ClslValue xor(ClslValue other, boolean set) {
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
				return ClslIntConst.of(val ^ other.toChar());
			case INT:
				return ClslIntConst.of(val ^ other.toInt());
			case LONG:
				return new ClslLongConst(val ^ other.toLong());
		}
		super.xor(other, set);
		return this;
	}
}