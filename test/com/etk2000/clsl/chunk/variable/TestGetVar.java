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
	private static final ClslIntConst EXPECTED_VALUE = ClslIntConst.of(42069);

	@Test
	void testGet() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final GetVar chunk = new GetVar(VARIABLE_NAME);

		env.defineVar(VARIABLE_NAME, EXPECTED_VALUE);
		env.defineVar(VARIABLE_NAME + 1, new ClslCharConst('a'));

		assertEquals(EXPECTED_VALUE, chunk.get(env));
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
	void testGlobalScopeAccess() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final GetVar chunk = new GetVar(VARIABLE_NAME);

		env.defineVar(VARIABLE_NAME, EXPECTED_VALUE);
		assertEquals(EXPECTED_VALUE, chunk.get(env));

		env.pushStack(true);
		assertEquals(EXPECTED_VALUE, chunk.get(env));
	}

	@Test
	void testGlobalScopeOverride() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final GetVar chunk = new GetVar(VARIABLE_NAME);
		final ClslValue expectedLocal = EXPECTED_VALUE.add(ClslIntConst.of(1), false);

		env.defineVar(VARIABLE_NAME, EXPECTED_VALUE);
		assertEquals(EXPECTED_VALUE, chunk.get(env));

		env.pushStack(true);
		env.defineVar(VARIABLE_NAME, expectedLocal);
		assertEquals(expectedLocal, chunk.get(env));
	}

	@Test
	void testIO() throws IOException {
		final GetVar original = new GetVar(VARIABLE_NAME);

		assertEquals(original, transmitAndReceive(original, GetVar::new));
	}

	@Test
	void testInnerScopeInaccessible() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final GetVar chunk = new GetVar(VARIABLE_NAME);
		env.pushStack(true);

		env.defineVar(VARIABLE_NAME, EXPECTED_VALUE);
		assertEquals(EXPECTED_VALUE, chunk.get(env));

		env.pushStack(true);
		assertThrows(ClslVariableCannotBeResolvedException.class, () -> chunk.get(env));
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