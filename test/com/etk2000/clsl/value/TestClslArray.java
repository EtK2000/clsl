package com.etk2000.clsl.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.etk2000.clsl.ClslUtil;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.exception.type.ClslInvalidArrayComponentTypeException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.EnumSet;

public class TestClslArray {
	@ParameterizedTest
	@ValueSource(strings = {
			"\\\\",
			"\\b",
			"\\f",
			"\\n",
			"\\r",
			"\\t",
			"\\\"",
			"\\'",
			"hello world!\\n"
	})
	void testToStringCharArray(String value) {
		assertEquals(
				'"' + value + '"',
				new ClslArrayConst(ClslUtil.unescape(value)).toString()
		);
	}

	@ParameterizedTest
	@CsvSource({
			"'',0",
			"test,4"
	})
	void testStrlen(String value, int expectedLength) {
		assertEquals(
				expectedLength,
				new ClslArrayConst(ClslUtil.unescape(value)).strlen()
		);
	}

	@Test
	void testStrlenInvalidComponentType() {
		final EnumSet<ValueType> types = EnumSet.allOf(ValueType.class);
		types.remove(ValueType.CHAR);

		for (ValueType type : types) {
			assertThrows(
					ClslInvalidArrayComponentTypeException.class,
					() -> new ClslArray(type).strlen(),
					"testStrlenInvalidComponentType: " + type
			);
		}
	}
}