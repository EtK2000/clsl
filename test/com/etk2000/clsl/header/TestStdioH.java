package com.etk2000.clsl.header;

import static com.etk2000.clsl.test.TestingUtils.assertOutput;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.exception.function.ClslTooFewArgumentsException;
import com.etk2000.clsl.value.ClslArrayConst;
import com.etk2000.clsl.value.ClslValue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TestStdioH {
	@Test
	void testPrintfNoArguments() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());

		StdioH.INSTANCE.execute(env);
		final ClslValue printf = env.getVar("printf");

		assertThrows(ClslTooFewArgumentsException.class, () -> printf.call(env, new ClslValue[0]));
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"test",
			"\nhi\t9"
	})
	void testPrintfSingleArgument(String text) {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());

		StdioH.INSTANCE.execute(env);
		final ClslValue printf = env.getVar("printf");

		assertOutput(text, () -> printf.call(env, new ClslArrayConst(text)));
	}
}