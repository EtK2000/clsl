package com.etk2000.clsl;

public enum ValueType {
	// pointer types
	ARRAY(SubType.POINTER, CLSLArray.class), POINTER(SubType.POINTER, CLSLPointer.class),

	// number types
	CHAR(SubType.NUMBER, CLSLChar.class), DOUBLE(SubType.NUMBER, CLSLDouble.class), FLOAT(SubType.NUMBER, CLSLFloat.class), INT(SubType.NUMBER,
			CLSLInt.class), LONG(SubType.NUMBER, CLSLLong.class),

	STRUCT(SubType.STRUCT, CLSLStruct.class),

	// NULL and void
	VOID(null, null);

	// the sub-type helps determine if a value can be auto-casted
	public enum SubType {
		NUMBER, POINTER, STRUCT
	}

	final Class<? extends CLSLValue> clazz;
	public final SubType subType;

	private ValueType(SubType subType, Class<? extends CLSLValue> clazz) {
		this.subType = subType;
		this.clazz = clazz;
	}
}