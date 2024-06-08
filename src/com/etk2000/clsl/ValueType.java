package com.etk2000.clsl;

import com.etk2000.clsl.value.ClslArray;
import com.etk2000.clsl.value.ClslChar;
import com.etk2000.clsl.value.ClslDouble;
import com.etk2000.clsl.value.ClslFloat;
import com.etk2000.clsl.value.ClslInt;
import com.etk2000.clsl.value.ClslLong;
import com.etk2000.clsl.value.ClslPointer;
import com.etk2000.clsl.value.ClslStruct;
import com.etk2000.clsl.value.ClslValue;

public enum ValueType {
	// pointer types
	ARRAY(SubType.POINTER, ClslArray.class),
	POINTER(SubType.POINTER, ClslPointer.class),

	// number types
	CHAR(SubType.NUMBER, ClslChar.class),
	DOUBLE(SubType.NUMBER, ClslDouble.class),
	FLOAT(SubType.NUMBER, ClslFloat.class),
	INT(SubType.NUMBER, ClslInt.class),
	LONG(SubType.NUMBER, ClslLong.class),

	STRUCT(SubType.STRUCT, ClslStruct.class),

	// NULL and void
	VOID(null, null);

	// the sub-type helps determine if a value can be auto-casted
	public enum SubType {
		NUMBER, POINTER, STRUCT
	}

	public static ValueType fromJava(Class<?> type) {
		if (type == char.class || type == Character.class)
			return CHAR;
		if (type == double.class || type == Double.class)
			return DOUBLE;
		if (type == float.class || type == Float.class)
			return FLOAT;
		if (type == int.class || type == Integer.class)
			return INT;
		if (type == long.class || type == Long.class)
			return LONG;
		if (type == void.class || type == Void.class)
			return VOID;

		throw new IllegalArgumentException(type.getName() + " has no apparent CLSL type parallel");
	}

	public final Class<? extends ClslValue> clazz;
	public final SubType subType;

	ValueType(SubType subType, Class<? extends ClslValue> clazz) {
		this.subType = subType;
		this.clazz = clazz;
	}
}