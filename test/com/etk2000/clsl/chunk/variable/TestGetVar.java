package com.etk2000.clsl.chunk.variable;

import static com.etk2000.clsl.test.TestingUtils.transmitAndReceive;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.exception.variable.ClslVariableCannotBeResolvedException;
import com.etk2000.clsl.header.DirectoryHeaderFinder;
import com.etk2000.clsl.value.ClslCharConst;
import com.etk2000.clsl.value.ClslIntConst;
import com.etk2000.clsl.value.ClslValue;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class TestGetVar {
	private static final String VARIABLE_NAME = "test";

	@Test
	void testGet() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final GetVar chunk = new GetVar(VARIABLE_NAME);

		final ClslValue expected = new ClslIntConst(42069);
		env.defineVar(VARIABLE_NAME, expected);
		env.defineVar(VARIABLE_NAME + 1, new ClslCharConst('a'));

		assertEquals(expected, chunk.get(env));
	}

	@Test
	void testGetUnresolvedVariable() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final GetVar chunk = new GetVar(VARIABLE_NAME);

		env.defineVar(VARIABLE_NAME + 1, new ClslCharConst('a'));

		assertThrows(ClslVariableCannotBeResolvedException.class, () -> chunk.get(env));
	}

	@Test
	void testGetExecutablePart() {
		final GetVar chunk = new GetVar(VARIABLE_NAME);

		assertNull(chunk.getExecutablePart(new OptimizationEnvironment(new ArrayList<>())));
	}

	@Test
	void testIO() throws IOException {
		final GetVar original = new GetVar(VARIABLE_NAME);

		assertEquals(original, transmitAndReceive(original, GetVar::new));
	}

	@Test
	void testOptimizeFirstPass() {
		final OptimizationEnvironment env = new OptimizationEnvironment(new ArrayList<>());
		final GetVar chunk = new GetVar(VARIABLE_NAME);

		assertEquals(chunk, chunk.optimize(env));
		assertEquals(0, env.unusedVars.size());

		env.unusedVars.add(VARIABLE_NAME);
		assertEquals(chunk, chunk.optimize(env));
		assertEquals(0, env.unusedVars.size());
	}

	@Test
	void testOptimizeSecondPass() {
		final OptimizationEnvironment env = new OptimizationEnvironment(new ArrayList<>()).secondPass();
		final GetVar chunk = new GetVar(VARIABLE_NAME);

		assertEquals(chunk, chunk.optimize(env));

		env.unusedVars.add(VARIABLE_NAME);
		assertNull(chunk.optimize(env));
	}

	@Test
	void testToString() {
		assertEquals(
				VARIABLE_NAME,
				new GetVar(VARIABLE_NAME).toString()
		);
	}
}