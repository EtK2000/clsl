package com.etk2000.clsl.value;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslUtil;
import com.etk2000.clsl.StringBuilderPoolable;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.exception.ClslRuntimeException;
import com.etk2000.clsl.exception.type.ClslInvalidArrayComponentTypeException;
import com.etk2000.clsl.exception.type.ClslInvalidArraySizeException;
import com.etk2000.clsl.exception.variable.ClslBufferOverflowException;

import java.util.Arrays;

// TODO: allow arr[i] = x;
// FIXME: use generics for val and component
public class ClslArray extends ClslValue {
	public final ValueType component;
	public ClslValue[] val;

	// used in const Array
	protected ClslArray(ValueType component) {
		this(ValueType.ARRAY, component);
	}

	// used in Pointer
	protected ClslArray(ValueType type, ValueType component) {
		super(type);
		this.component = component;
	}

	public ClslArray(ValueType component, short length) {
		this(ValueType.ARRAY, component, length);
	}

	// used in Pointer
	protected ClslArray(ValueType type, ValueType component, short length) {
		super(type);
		if (type == ValueType.POINTER ? length < 1 : length < 0)
			throw new ClslInvalidArraySizeException();

		switch (this.component = component) {
			case ARRAY:// TODO: allow multidimensional arrays
			case POINTER:// TODO: allow multidimensional pointers
				throw new ClslInvalidArrayComponentTypeException(Clsl.typeName(type), Clsl.typeName(component));
			case CHAR:
				val = new ClslChar[length];
				for (short i = 0; i < length; ++i)
					val[i] = new ClslChar();
				break;
			case DOUBLE:
				val = new ClslDouble[length];
				for (short i = 0; i < length; ++i)
					val[i] = new ClslDouble();
				break;
			case FLOAT:
				val = new ClslFloat[length];
				for (short i = 0; i < length; ++i)
					val[i] = new ClslFloat();
				break;
			case INT:
				val = new ClslInt[length];
				for (short i = 0; i < length; ++i)
					val[i] = new ClslInt();
				break;
			case LONG:
				val = new ClslLong[length];
				for (short i = 0; i < length; ++i)
					val[i] = new ClslLong();
				break;
			case VOID:// TODO: maybe fill so we know about memory leaks
				if (type == ValueType.POINTER)
					val = new ClslValue[length];// void*
				else
					throw new ClslInvalidArrayComponentTypeException(Clsl.typeName(type), Clsl.typeName(component));
		}
	}

	public ClslArray(ClslValue[] val) {
		this(ValueType.ARRAY, val);
	}

	// used in Pointer
	protected ClslArray(ValueType type, ClslValue[] val) {
		super(type);
		this.val = val;

		// TODO: find common denominator if multiple types given
		// TODO: make Array/Pointer correct
		ValueType comp = null;// TODO: deal with const arrays
		Class<?> c = val.getClass().getComponentType();
		for (ValueType v : ValueType.values()) {
			if (v != ValueType.VOID && v.clazz.isAssignableFrom(c)) {
				comp = v;
				break;
			}
		}

		if ((component = comp) == null)
			throw new ClslInvalidArrayComponentTypeException();
	}

	public ClslArray append(ClslArray other) {
		if (component != ValueType.CHAR || other.component != ValueType.CHAR)
			throw new ClslInvalidArrayComponentTypeException();

		int strlen = strlen();
		if ((other.val[other.val.length - 1].toBoolean() ? other.val.length + 1 : other.val.length) > val.length - strlen)
			throw new ClslBufferOverflowException();

		int i = 0;
		for (; i < other.val.length; ++i)
			val[i + strlen] = new ClslChar(other.val[i].toChar());
		val[i + strlen] = new ClslChar(0);// NULL termination (JIC)
		return this;
	}

	@Override
	public ClslConst cast(ValueType to) {
		switch (to) {
			case ARRAY:// FIXME: deal with component types
				if (Clsl.doWarn)
					System.out.println("redundant cast from array to array");
				return new ClslArrayConst(val);
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case VOID:
				break;
			case POINTER:
				if (Clsl.doWarn)
					System.out.println("redundant cast from array to pointer");
				return new ClslPointerConst(val);// keep this const?
		}
		return super.cast(to);
	}

	public int compareTo(ClslArray other) {
		if (component != ValueType.CHAR || other.component != ValueType.CHAR)
			throw new ClslRuntimeException("can only compare strings");

		int len1 = strlen(), len2 = other.strlen(), lim = Math.min(len1, len2);
		for (int i = 0; i < lim; ++i) {
			char c1 = val[i].toChar(), c2 = other.val[i].toChar();
			if (c1 != c2)
				return c1 - c2;
		}
		return len1 - len2;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ClslArrayConst copy() {
		return new ClslArrayConst(val);
	}

	public ClslArray fill(CharSequence put) {
		if (component != ValueType.CHAR)
			throw new ClslInvalidArrayComponentTypeException();
		if (put.length() + 1 > val.length)
			throw new ClslBufferOverflowException();

		int i = 0;
		for (; i < put.length(); ++i)
			val[i] = new ClslChar(put.charAt(i));
		val[i] = new ClslChar(0);// NULL termination
		return this;
	}

	public ClslArray fill(ClslArray other) {
		if (component != ValueType.CHAR || other.component != ValueType.CHAR)
			throw new ClslInvalidArrayComponentTypeException();
		if ((other.val[other.val.length - 1].toBoolean() ? other.val.length + 1 : other.val.length) > val.length)
			throw new ClslBufferOverflowException();

		int i = 0;
		for (; i < other.val.length; ++i)
			val[i] = new ClslChar(other.val[i].toChar());
		val[i] = new ClslChar(0);// NULL termination (JIC)
		return this;
	}

	@Override
	public ClslValue index(ClslValue index) {
		switch (index.type) {
			case ARRAY:
			case DOUBLE:
			case FLOAT:
			case POINTER:
			case STRUCT:
			case VOID:
				break;
			case CHAR:
			case INT:
			case LONG:
				return val[index.toChar()];// max arr len is same as char
		}
		return super.index(index);
	}

	@Override
	public ClslArray set(ClslValue other) {
		if (other instanceof ClslArray)
			val = ((ClslArray) other).val;
		else
			super.set(other);
		return this;
	}

	@Override
	public ClslIntConst sizeof() {
		return ClslIntConst.of(val.length > 0 ? val.length * val[0].sizeof().val : 0);
	}

	public int strlen() {
		if (component != ValueType.CHAR)
			throw new ClslInvalidArrayComponentTypeException();

		for (int i = 0; i < val.length; ++i) {
			if (val[i] == null || !val[i].toBoolean())
				return i;
		}

		if (Clsl.doWarn)
			System.out.println("no NULL termination: " + this);
		return val.length;
	}

	@Override
	public Object toJava() {
		switch (component) {
			case CHAR:// TODO: optimize this, i.e. don't do 2 fors
				try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
					char c = 255;
					for (ClslValue clslValue : val) {
						c = clslValue.toChar();
						if (c == 0)
							break;// hit NULL termination
						sb.append(c);
					}
					if (c != 0) {// no NULL termination
						char[] res = new char[val.length];
						for (short i = 0; i < res.length; ++i)
							res[i] = val[i].toChar();
						return res;
					}
					return sb.toString();
				}
			case DOUBLE: {
				double[] res = new double[val.length];
				for (short i = 0; i < res.length; ++i)
					res[i] = val[i].toDouble();
				return res;
			}
			case FLOAT: {
				float[] res = new float[val.length];
				for (short i = 0; i < res.length; ++i)
					res[i] = val[i].toFloat();
				return res;
			}
			case INT: {
				int[] res = new int[val.length];
				for (short i = 0; i < res.length; ++i)
					res[i] = val[i].toInt();
				return res;
			}
			case LONG: {
				long[] res = new long[val.length];
				for (short i = 0; i < res.length; ++i)
					res[i] = val[i].toLong();
				return res;
			}
			case VOID:
				break;
		}
		throw new UnsupportedOperationException("ClslArray(" + Clsl.typeName(component) + ") not supported yet");
	}

	@Override
	public String toString() {
		if (component == ValueType.CHAR) {
			try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
				sb.append('"');
				for (short i = 0; i < val.length && val[i].toBoolean(); ++i)
					sb.append(ClslUtil.escape(val[i].toChar()));
				return sb.append('"').toString();
			}
		}

		// TODO: show toString as { ... } and not [ ... ]?
		return Arrays.toString(val);
	}

	@Override
	public String typeName() {
		return component.name() + "[]";
	}
}