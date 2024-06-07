package com.etk2000.clsl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.etk2000.clsl.compiler.ClslCompiler;

import org.junit.jupiter.api.Test;

public class TestClslCode {
	@Test
	void testToString() {
		final String source = "#include <string.h>\nprintf(\"test\\n\");";
		final ClslCode code = ClslCompiler.compile(source, false);
		assertEquals(source, code.toString().trim());
	}
}