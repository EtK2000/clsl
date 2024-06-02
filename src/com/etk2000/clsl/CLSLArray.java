package com.etk2000.clsl;

import java.util.Arrays;

// TODO: allow arr[i] = x;
// FIXME: use generics for val and component
public class CLSLArray extends CLSLValue {
	public final ValueType component;
	public CLSLValue[] val;

	// used in const Array
	protected CLSLArray(ValueType component) {
		this(ValueType.ARRAY, component);
	}

	// used in Pointer
	protected CLSLArray(ValueType type, ValueType component) {
		super(type);
		this.component = component;
	}

	public CLSLArray(ValueType component, short length) {
		this(ValueType.ARRAY, component, length);
	}

	// used in Pointer
	protected CLSLArray(ValueType type, ValueType component, short length) {
		super(type);
		if (type == ValueType.POINTER ? length < 1 : length < 0)
			throw new CLSL_CompilerException("invalid size");

		switch (this.component = component) {
			case ARRAY:// TODO: allow multidimensional arrays
			case POINTER:// TODO: allow multidimensional pointers
				throw new CLSL_CompilerException("cannot create " + CLSL.typeName(type) + " of type " + CLSL.typeName(component));
			case CHAR:
				val = new CLSLChar[length];
				for (short i = 0; i < length; ++i)
					val[i] = new CLSLChar();
				break;
			case DOUBLE:
				val = new CLSLDouble[length];
				for (short i = 0; i < length; ++i)
					val[i] = new CLSLDouble();
				break;
			case FLOAT:
				val = new CLSLFloat[length];
				for (short i = 0; i < length; ++i)
					val[i] = new CLSLFloat();
				break;
			case INT:
				val = new CLSLInt[length];
				for (short i = 0; i < length; ++i)
					val[i] = new CLSLInt();
				break;
			case LONG:
				val = new CLSLLong[length];
				for (short i = 0; i < length; ++i)
					val[i] = new CLSLLong();
				break;
			case VOID:// TODO: maybe fill so we know about memory leaks
				if (type == ValueType.POINTER)
					val = new CLSLValue[length];// void*
				else
					throw new CLSL_CompilerException("cannot create " + CLSL.typeName(type) + " of type " + CLSL.typeName(component));
		}
	}

	public CLSLArray(CLSLValue[] val) {
		this(ValueType.ARRAY, val);
	}

	// used in Pointer
	protected CLSLArray(ValueType type, CLSLValue[] val) {
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
			throw new CLSL_CompilerException("invalid array component type");
	}

	CLSLArray fill(CharSequence put) {
		if (component != ValueType.CHAR)
			throw new CLSL_RuntimeException("invalid component type");
		if (put.length() + 1 > val.length)
			throw new CLSL_RuntimeException("buffer overflow");

		int i = 0;
		for (; i < put.length(); ++i)
			val[i] = new CLSLChar(put.charAt(i));
		val[i] = new CLSLChar(0);// NULL termination
		return this;
	}

	CLSLArray fill(CLSLArray other) {
		if (component != ValueType.CHAR || other.component != ValueType.CHAR)
			throw new CLSL_RuntimeException("invalid component type");
		if ((other.val[other.val.length - 1].toBoolean() ? other.val.length + 1 : other.val.length) > val.length)
			throw new CLSL_RuntimeException("buffer overflow");

		int i = 0;
		for (; i < other.val.length; ++i)
			val[i] = new CLSLChar(other.val[i].toChar());
		val[i] = new CLSLChar(0);// NULL termination (JIC)
		return this;
	}

	CLSLArray append(CLSLArray other) {
		if (component != ValueType.CHAR || other.component != ValueType.CHAR)
			throw new CLSL_RuntimeException("invalid component type");

		int strlen = strlen();
		if ((other.val[other.val.length - 1].toBoolean() ? other.val.length + 1 : other.val.length) > val.length - strlen)
			throw new CLSL_RuntimeException("buffer overflow");

		int i = 0;
		for (; i < other.val.length; ++i)
			val[i + strlen] = new CLSLChar(other.val[i].toChar());
		val[i + strlen] = new CLSLChar(0);// NULL termination (JIC)
		return this;
	}

	int compareTo(CLSLArray other) {
		if (component != ValueType.CHAR || other.component != ValueType.CHAR)
			throw new CLSL_RuntimeException("can only compare strings");

		int len1 = strlen(), len2 = other.strlen(), lim = Math.min(len1, len2);
		for (int i = 0; i < lim; ++i) {
			char c1 = val[i].toChar(), c2 = other.val[i].toChar();
			if (c1 != c2)
				return c1 - c2;
		}
		return len1 - len2;
	}

	int strlen() {
		if (component != ValueType.CHAR)
			throw new CLSL_RuntimeException("invalid component type");

		for (int i = 0; i < val.length; ++i) {
			if (val[i] == null || !val[i].toBoolean())
				return i;
		}

		if (CLSL.doWarn)
			System.out.println("no NULL termination: " + toString());
		return val.length;
	}

	@Override
	public CLSLArray set(CLSLValue other) {
		if (other instanceof CLSLArray)
			val = ((CLSLArray) other).val;
		else
			super.set(other);
		return this;
	}

	@Override
	public CLSLValue index(CLSLValue index) {
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

	@SuppressWarnings("unchecked")
	@Override
	public CLSLArrayConst copy() {
		return new CLSLArrayConst(val);
	}

	@Override
	public CLSLIntConst sizeof() {
		return new CLSLIntConst(val.length > 0 ? val.length * val[0].sizeof().val : 0);
	}

	@Override
	public String typeName() {
		return component.name() + "[]";
	}

	@Override
	public CLSLConst cast(ValueType to) {
		switch (to) {
			case ARRAY:// FIXME: deal with component types
				if (CLSL.doWarn)
					System.out.println("redundant cast from array to array");
				return new CLSLArrayConst(val);
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case VOID:
				break;
			case POINTER:
				return new CLSLPointerConst(val);// keep this const?
		}
		return super.cast(to);
	}

	// TODO: show toString as { ... } and not [ ... ]?
	@Override
	public String toString() {
		if (component == ValueType.CHAR) {
			try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
				for (short i = 0; i < val.length && val[i].toBoolean(); ++i)
					sb.append(val[i].toChar());
				return sb.toString();
			}
		}
		return Arrays.toString(val);
	}

	// TODO: find a way to not require creating new arrays every time
	@Override
	public Object toJava() {
		switch (component) {
			case CHAR:// TODO: optimize this, i.e. don't do 2 fors
				try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
					char c = 255;
					for (short i = 0; i < val.length; ++i) {
						c = val[i].toChar();
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
		throw new UnsupportedOperationException("CLSLArray(" + CLSL.typeName(component) + ") not supported yet");
	}
}