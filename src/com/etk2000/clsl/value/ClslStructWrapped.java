package com.etk2000.clsl.value;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.StringBuilderPoolable;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.exception.ClslRuntimeException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

// FIXME: this class doesn't update the wrapped class yet!!!
//TODO: create a non-const version of this class that updates the underlying object
//TODO: allow mapping Java ByteBuffer objects to CLSL Pointers
public class ClslStructWrapped<T> extends ClslStruct {
	public static final byte WRAP_FIELDS = 1, WRAP_METHODS = 2,

	// synthetic example: $VALUES in enum
	WRAP_SYNTHETIC = 4,

	// transient is usually garbage related to serialization
	WRAP_TRANSIENT = 8,

	// enable this to allow accessing inaccessible fields
	CHANGE_ACCESS = 16,

	WRAP_ALL = WRAP_FIELDS | WRAP_METHODS | WRAP_SYNTHETIC | WRAP_TRANSIENT,

	DEFAULT_WRAP = WRAP_FIELDS;

	public static Map<String, ClslValue> wrap(Object o, byte wrapType, boolean constant) {
		Map<String, ClslValue> res = new HashMap<>();

		try {
			boolean wrapTransient = (wrapType & WRAP_TRANSIENT) != 0,
					wrapSynthetics = (wrapType & WRAP_SYNTHETIC) != 0,
					wrapInaccessible = (wrapType & CHANGE_ACCESS) != 0;

			if ((wrapType & WRAP_FIELDS) != 0) {
				for (Field f : o.getClass().getDeclaredFields()) {
					if (((f.getModifiers() & Modifier.TRANSIENT) == 0 || wrapTransient) &&
							(!f.isSynthetic() || wrapSynthetics) &&
							((f.getModifiers() & Modifier.PUBLIC) != 0 || wrapInaccessible)
					) {
						f.setAccessible(true);
						res.put(f.getName(), valueOf(f.get(o), constant));
					}
				}
			}

			if ((wrapType & WRAP_METHODS) != 0) {
				for (Method m : o.getClass().getDeclaredMethods()) {
					if ((!m.isSynthetic() || wrapSynthetics) &&
							((m.getModifiers() & Modifier.PUBLIC) != 0 || wrapInaccessible)) {
						m.setAccessible(true);

						final int parameterCount = m.getParameterCount();
						if (parameterCount != 0 && parameterCount != 2) {
							if (Clsl.doWarn)
								System.out.println("Cannot wrap " + o.getClass().getName() + "::" + m.getName());
							continue;
						}

						// FIXME: handle wrapping better

						res.put(
								m.getName(),
								Clsl.createFunctionalChunk(
										ValueType.fromJava(m.getReturnType()),
										(env, args) -> {
											try {
												if (parameterCount == 0)
													return Clsl.wrap(m.invoke(o));
												return Clsl.wrap(m.invoke(o, env, args));
											}
											catch (ReflectiveOperationException e) {
												throw new ClslRuntimeException(e.getMessage());
											}
										}
								).access()
						);
					}
				}
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
		else if (o instanceof Double)
			return constant ? new ClslDoubleConst((double) o) : new ClslDouble((double) o);
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
			return constant ? new ClslStructConstWrapped<>(o) : new ClslStructWrapped<>(o);
	}

	public final T wrapped;
	private final Map<String, ClslValue> members;

	public ClslStructWrapped(T o) {
		this(o, DEFAULT_WRAP, false);
	}

	public ClslStructWrapped(T o, byte wrapType) {
		this(o, wrapType, false);
	}

	protected ClslStructWrapped(T o, byte wrapType, boolean constant) {
		members = wrap(o, wrapType, constant);
		wrapped = o;
	}

	@Override
	public ClslValue dot(String member) {
		return members.get(member);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ClslStructConstWrapped<T> copy() {
		return new ClslStructConstWrapped<>(wrapped);
	}

	@Override
	public boolean eq(ClslValue other) {
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
				return other instanceof ClslStructWrapped<?> && wrapped.equals(((ClslStructWrapped<?>) other).wrapped);
		}
		throw new UnsupportedOperationException(
				"The operator == is undefined for the argument type(s) " + typeName() + ", " + other.typeName()
		);
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
			return sb.deleteLast(members.isEmpty() ? 1 : 2).append('}').toString();
		}
	}

	@Override
	public T toJava() {
		return wrapped;
	}
}