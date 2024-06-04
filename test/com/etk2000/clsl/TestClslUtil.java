package com.etk2000.clsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TestClslUtil {
	@CsvSource(quoteCharacter = '`', value = {
			"\\,\\\\",
			"`\b`,\\b",
			"`\f`,\\f",
			"`\n`,\\n",
			"`\r`,\\r",
			"`\t`,\\t",
			"\",\\\"",
			"`'`,\\'",
			"`hello world!\n`,hello world!\\n"
	})
	@ParameterizedTest
	void testEscape(String original, String expected) {
		assertEquals(expected, ClslUtil.escape(original));
	}

	@Test
	void testIsValidIdEmpty() {
		assertFalse(ClslUtil.isValidId(""));
	}

	@Test
	void testIsValidIdFirstCharacterCheck() {
		for (char c = 0; c < 256; c++) {
			final boolean expectedValidity = Character.isLetter(c) || c == '_' || c == '$';
			final String str = Character.toString(c) + 'a';
			assertEquals(
					expectedValidity,
					ClslUtil.isValidId(str),
					"testIsValidIdFirstCharacterCheck: " + str
			);
		}
	}

	@Test
	void testIsValidIdLeadingCharacterCheck() {
		for (char c = 0; c < 256; c++) {
			final boolean expectedValidity = Character.isLetter(c) || Character.isDigit(c) || c == '_' || c == '$';
			final String str = '_' + Character.toString(c);
			assertEquals(
					expectedValidity,
					ClslUtil.isValidId(str),
					"testIsValidIdLeadingCharacterCheck: " + str
			);
		}
	}

	@CsvSource(quoteCharacter = '`', value = {
			"\\,\\\\",
			"`\b`,\\b",
			"`\f`,\\f",
			"`\n`,\\n",
			"`\r`,\\r",
			"`\t`,\\t",
			"\",\\\"",
			"`'`,\\'",
			"`hello world!\n`,hello world!\\n"
	})
	@ParameterizedTest
	void testUnescape(String expected, String original) {
		assertEquals(expected, ClslUtil.unescape(original));
	}
}