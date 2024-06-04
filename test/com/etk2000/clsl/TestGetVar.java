package com.etk2000.clsl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.etk2000.clsl.chunk.variable.GetVar;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TestGetVar {
	@ParameterizedTest
	@ValueSource(strings = {"a", "banana", "vArIaBle_69"})
	void testConstructorString(String name) {
		final GetVar chunk = new GetVar(name);
		assertEquals(name, chunk.name);
	}
}