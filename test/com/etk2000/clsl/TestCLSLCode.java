package com.etk2000.clsl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestCLSLCode {
	@Test
	void testToString() {
		final String source = "#include <string.h>\nprintf(\"test\");";
		final CLSLCode code = new CLSLCompiler().compile(source, false);
		assertEquals(source, code.toString().trim());
	}
}