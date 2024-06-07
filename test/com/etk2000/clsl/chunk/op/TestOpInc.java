package com.etk2000.clsl.chunk.op;

import static com.etk2000.clsl.test.TestingUtils.transmitAndReceive;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.header.DirectoryHeaderFinder;
import com.etk2000.clsl.value.ClslInt;
import com.etk2000.clsl.value.ClslIntConst;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

public class TestOpInc {
	private static final String VARIABLE_NAME = "test";

	@Test
	void testGetPostfix() {
		final int initialValue = 5;
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final OpInc chunk = new OpInc(VARIABLE_NAME, true);

		env.defineVar(VARIABLE_NAME, new ClslInt(initialValue));
		assertEquals(new ClslIntConst(initialValue), chunk.get(env));
		assertEquals(new ClslInt(initialValue + 1), env.getVar(VARIABLE_NAME));
	}

	@Test
	void testGetPrefix() {
		final int initialValue = 5;
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final OpInc chunk = new OpInc(VARIABLE_NAME, false);
		final ClslInt subtracted = new ClslInt(initialValue + 1);

		env.defineVar(VARIABLE_NAME, new ClslInt(initialValue));
		assertEquals(subtracted, chunk.get(env));
		assertEquals(subtracted, env.getVar(VARIABLE_NAME));
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testIO(boolean isPostfix) throws IOException {
		final OpInc original = new OpInc(VARIABLE_NAME, isPostfix);

		assertEquals(
				original,
				transmitAndReceive(original, OpInc::new)
		);
	}

	@Test
	void testToStringPostfix() {
		assertEquals(
				VARIABLE_NAME + "++",
				new OpInc(VARIABLE_NAME, true).toString()
		);
	}

	@Test
	void testToStringPrefix() {
		assertEquals(
				"++" + VARIABLE_NAME,
				new OpInc(VARIABLE_NAME, false).toString()
		);
	}
}