package com.etk2000.clsl.chunk;

import static com.etk2000.clsl.test.TestingUtils.transmitAndReceive;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.MainScopeLookupContainer;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.value.ConstArrayChunk;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.exception.function.ClslFunctionCallException;
import com.etk2000.clsl.header.DirectoryHeaderFinder;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class TestFunctionCallChunk {
	private static final String FUNCTION_NAME = "test";
	private static final ValueChunk ARGUMENT_0 = new ConstIntChunk(42069),
			ARGUMENT_1 = new ConstArrayChunk("banana");

	@Test
	void testExecute() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final FunctionCallChunk chunk = new FunctionCallChunk(FUNCTION_NAME);
		final boolean[] callStatus = {false};

		env.addFunction(FUNCTION_NAME, Clsl.createFunctionalChunk((_env, args) -> {
			assertEquals(env, _env);
			assertEquals(0, args.length);
			callStatus[0] = true;
		}));

		assertNull(chunk.execute(env));
		assertTrue(callStatus[0]);
	}

	@Test
	void testExecuteUnresolvedFunction() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final FunctionCallChunk chunk = new FunctionCallChunk("missing");

		assertThrows(ClslFunctionCallException.class, () -> chunk.execute(env));
	}

	@Test
	void testExecuteWithArguments() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final FunctionCallChunk chunk = new FunctionCallChunk(FUNCTION_NAME, ARGUMENT_0, ARGUMENT_1);
		final boolean[] callStatus = {false};

		env.addFunction(FUNCTION_NAME, Clsl.createFunctionalChunk((_env, args) -> {
			assertEquals(env, _env);

			assertEquals(2, args.length);
			assertEquals(ARGUMENT_0.get(env), args[0]);
			assertEquals(ARGUMENT_1.get(env), args[1]);

			callStatus[0] = true;
		}));

		assertNull(chunk.execute(env));
		assertTrue(callStatus[0]);
	}

	@Test
	void testGetExecutablePart() {
		final OptimizationEnvironment env = new OptimizationEnvironment(new ArrayList<>());
		final FunctionCallChunk chunk = new FunctionCallChunk(FUNCTION_NAME);

		assertEquals(chunk, chunk.getExecutablePart(env));
	}

	@Test
	void testIO() throws IOException {
		final FunctionCallChunk original = new FunctionCallChunk(FUNCTION_NAME);

		assertEquals(
				original,
				transmitAndReceive(original, FunctionCallChunk::new)
		);
	}

	@Test
	void testIOWithArguments() throws IOException {
		final FunctionCallChunk original = new FunctionCallChunk(FUNCTION_NAME, ARGUMENT_0, ARGUMENT_1);

		assertEquals(
				original,
				transmitAndReceive(original, FunctionCallChunk::new)
		);
	}

	@Test
	void testOptimize() {
		final OptimizationEnvironment env = new OptimizationEnvironment(new ArrayList<>());
		final FunctionCallChunk chunk = new FunctionCallChunk(FUNCTION_NAME);

		assertEquals(chunk, chunk.optimize(env));
		assertEquals(chunk, chunk.optimize(env.secondPass()));
	}

	// FIXME: test for optimization of parameters

	@Test
	void testToString() {
		assertEquals(
				FUNCTION_NAME + "()",
				new FunctionCallChunk(FUNCTION_NAME).toString()
		);
	}

	@Test
	void testToStringWithArguments() {
		assertEquals(
				FUNCTION_NAME + '(' + ARGUMENT_0 + ", " + ARGUMENT_1 + ')',
				new FunctionCallChunk(FUNCTION_NAME, ARGUMENT_0, ARGUMENT_1).toString()
		);
	}
}