package com.etk2000.clsl.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.etk2000.clsl.ValueType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TestClslInt {
	@Test
	void testConstructor() {
		final ClslInt cInt = new ClslInt();
		Assertions.assertEquals(ValueType.INT, cInt.type);
		assertEquals(0, cInt.val);
	}

	@ParameterizedTest
	@ValueSource(ints = {Integer.MIN_VALUE, 0, Integer.MAX_VALUE})
	void testConstructorInt(int value) {
		final ClslInt cInt = new ClslInt(value);
		assertEquals(ValueType.INT, cInt.type);
		assertEquals(value, cInt.val);
	}

	@ParameterizedTest
	@ValueSource(ints = {Integer.MIN_VALUE + 1, 0, Integer.MAX_VALUE})
	void testOpDecPost(int value) {
		final ClslInt cInt = new ClslInt(value);
		assertEquals(ValueType.INT, cInt.type);
		assertEquals(value, cInt.val);

		assertEquals(value, cInt.dec(true).val);
		assertEquals(value - 1, cInt.val);
	}

	@Test
	void testOpDecPostUnderflow() {
		final ClslInt cInt = new ClslInt(Integer.MIN_VALUE);
		assertEquals(ValueType.INT, cInt.type);

		assertEquals(Integer.MIN_VALUE, cInt.dec(true).val);
		assertEquals(Integer.MAX_VALUE, cInt.val);
	}

	@ParameterizedTest
	@ValueSource(ints = {Integer.MIN_VALUE + 1, 0, Integer.MAX_VALUE})
	void testOpDecPre(int value) {
		final ClslInt cInt = new ClslInt(value);
		assertEquals(ValueType.INT, cInt.type);
		assertEquals(value, cInt.val);

		assertEquals(value - 1, cInt.dec(false).val);
		assertEquals(value - 1, cInt.val);
	}

	@Test
	void testOpDecPreUnderflow() {
		final ClslInt cInt = new ClslInt(Integer.MIN_VALUE);
		assertEquals(ValueType.INT, cInt.type);
		assertEquals(Integer.MIN_VALUE, cInt.val);

		assertEquals(Integer.MAX_VALUE, cInt.dec(false).val);
		assertEquals(Integer.MAX_VALUE, cInt.val);
	}

	@ParameterizedTest
	@ValueSource(ints = {Integer.MIN_VALUE + 1, 0, Integer.MAX_VALUE})
	void testOpIncPost(int value) {
		final ClslInt cInt = new ClslInt(value);
		assertEquals(ValueType.INT, cInt.type);
		assertEquals(value, cInt.val);

		assertEquals(value, cInt.inc(true).val);
		assertEquals(value + 1, cInt.val);
	}

	@Test
	void testOpIncPostUnderflow() {
		final ClslInt cInt = new ClslInt(Integer.MAX_VALUE);
		assertEquals(ValueType.INT, cInt.type);

		assertEquals(Integer.MAX_VALUE, cInt.inc(true).val);
		assertEquals(Integer.MIN_VALUE, cInt.val);
	}

	@ParameterizedTest
	@ValueSource(ints = {Integer.MIN_VALUE, 0, Integer.MAX_VALUE - 1})
	void testOpIncPre(int value) {
		final ClslInt cInt = new ClslInt(value);
		assertEquals(ValueType.INT, cInt.type);
		assertEquals(value, cInt.val);

		assertEquals(value + 1, cInt.inc(false).val);
		assertEquals(value + 1, cInt.val);
	}

	@Test
	void testOpIncPreUnderflow() {
		final ClslInt cInt = new ClslInt(Integer.MAX_VALUE);
		assertEquals(ValueType.INT, cInt.type);
		assertEquals(Integer.MAX_VALUE, cInt.val);

		assertEquals(Integer.MIN_VALUE, cInt.inc(false).val);
		assertEquals(Integer.MIN_VALUE, cInt.val);
	}
}