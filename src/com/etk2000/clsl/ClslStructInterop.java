package com.etk2000.clsl;

import com.etk2000.clsl.exception.type.ClslUnknownTypeException;
import com.etk2000.clsl.value.ClslStruct;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ClslStructInterop {
	private final Map<String, Supplier<ClslStruct>> constructors = new HashMap<>();

	public ClslStruct newInstance(String name) {
		final Supplier<ClslStruct> constructor = constructors.get(name);

		if (constructor == null)
			throw new ClslUnknownTypeException(name);

		return constructor.get();
	}

	public void registerStruct(String name, Supplier<ClslStruct> constructor) {
		constructors.put(name, constructor);
	}
}