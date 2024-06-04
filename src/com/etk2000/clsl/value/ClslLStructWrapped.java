package com.etk2000.clsl.value;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.StringBuilderPoolable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

// FIXME: this class doesn't update the wrapped class yet!!!
//TODO: create a non-const version of this class that updates the underlying object
//TODO: allow mapping Java ByteBuffer objects to CLSL Pointers
public class ClslLStructWrapped<T> extends ClslLStruct {
	public static final byte WRAP_FIELDS = 1, WRAP_METHODS = 2,

	// synthetic example: $VALUES in enum
	WRAP_SYNTHETIC = 4,

	// transient is usually garbage related to serialization
	WRAP_TRANSIENT = 8,

	// enable this to allow accessing inaccessible fields
	CHANGE_ACCESS = 16,

	WRAP_ALL = WRAP_FIELDS | WRAP_METHODS | WRAP_SYNTHETIC | WRAP_TRANSIENT, DEFAULT_WRAP = WRAP_FIELDS;

	public static Map<String, ClslValue> wrap(Object o, byte wraptype, boolean constant) {
		Map<String, ClslValue> res = new HashMap<>();

		try {
			boolean trans = (wraptype & WRAP_TRANSIENT) != 0, synt = (wraptype & WRAP_SYNTHETIC) != 0, acce = (wraptype & CHANGE_ACCESS) != 0;

			if ((wraptype & WRAP_FIELDS) != 0) {
				for (Field f : o.getClass().getDeclaredFields()) {
					if (((f.getModifiers() & Modifier.TRANSIENT) == 0 || trans) && (!f.isSynthetic() || synt) && (f.isAccessible() || acce)) {
						f.setAccessible(true);
						res.put(f.getName(), valueOf(f.get(o), constant));
					}
				}
			}

			if ((wraptype & WRAP_METHODS) != 0) {
				if (Clsl.doWarn)
					System.err.println("method wrapping is not implemented yet");
				/*for (Method m : o.getClass().getDeclaredMethods()) {
					if ((!m.isSynthetic() || synt) && (m.isAccessible() || acce)) {
						m.setAccessible(true);
						res.put(m.getName(), valueOf(m.get(o)));
					}
				}*/
			}
		}
		catch (ReflectiveOperationException e) {
			if (Clsl.doWarn)
				e.printStackTrace();
		}

		return Collections.unmodifiableMap(res);
	}

	// TODO: keep this updated with implemented ClslValue types
	public static ClslValue valueOf(Object o, boolean constant) {
		// TODO: add support for arrays

		if (o instanceof Boolean)
			return constant ? new ClslCharConst((boolean) o ? 1 : 0) : new ClslChar((boolean) o ? 1 : 0);
		else if (o instanceof Byte)
			return constant ? new ClslCharConst((byte) o) : new ClslChar((byte) o);
		else if (o instanceof Character)
			return constant ? new ClslCharConst((char) o) : new ClslChar((char) o);

		/*
		else if (j instanceof Double)
			write(o, (double) j);
		*/

		else if (o instanceof Float)
			return constant ? new ClslFloatConst((float) o) : new ClslFloat((float) o);

		else if (o instanceof Integer)
			return constant ? new ClslIntConst((int) o) : new ClslInt((int) o);

		else if (o instanceof Long)
			return constant ? new ClslLongConst((long) o) : new ClslLong((long) o);

		/*else if (j instanceof Short)
			write(o, (short) j);
		*/

		else if (o instanceof String)
			return /*constant ? */new ClslArrayConst((String) o)/* : new ClslArray((String) o)*/;

		else
			return constant ? new ClslLStructConstWrapped<>(o) : new ClslLStructWrapped<>(o);
	}

	public final T wrapped;
	private final Map<String, ClslValue> members;

	public ClslLStructWrapped(T o) {
		this(o, DEFAULT_WRAP, false);
	}

	public ClslLStructWrapped(T o, byte wraptype) {
		this(o, wraptype, false);
	}

	protected ClslLStructWrapped(T o, byte wraptype, boolean constant) {
		members = wrap(o, wraptype, constant);
		wrapped = o;
	}

	@Override
	public ClslValue dot(String member) {
		return members.get(member);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ClslLStructConstWrapped<T> copy() {
		return new ClslLStructConstWrapped<>(wrapped);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ClslValue) {
			ClslValue other = (ClslValue) obj;
			switch (other.type) {
				case ARRAY:
				case CHAR:
				case DOUBLE:
				case FLOAT:
				case INT:
				case LONG:
				case VOID:
					break;
				case STRUCT:
					return other instanceof ClslLStructWrapped<?> ? wrapped.equals(((ClslLStructWrapped<?>) other).wrapped) : false;
			}
			throw new UnsupportedOperationException(
					"The operator == is undefined for the argument type(s) " + typeName() + ", " + ((ClslValue) obj).typeName());
		}
		return false;
	}

	@Override
	public ClslIntConst sizeof() {
		int res = 0;
		for (ClslValue v : members.values())
			res += v.sizeof().val;
		return new ClslIntConst(res);
	}

	@Override
	public String typeName() {
		return "struct";
	}

	@Override
	public String toString() {
		try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
			sb.append("struct { ");
			for (Entry<String, ClslValue> e : members.entrySet())
				sb.append(e.getKey()).append(" = ").append(e.getValue()).append(", ");
			return sb.deleteLast(members.size() > 0 ? 2 : 1).append('}').toString();
		}
	}

	@Override
	public T toJava() {
		return wrapped;
	}
}