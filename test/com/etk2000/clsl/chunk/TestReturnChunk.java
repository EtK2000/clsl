package com.etk2000.clsl.chunk;

import static com.etk2000.clsl.test.TestingUtils.transmitAndReceive;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.MainScopeLookupContainer;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.exception.ClslRuntimeException;
import com.etk2000.clsl.header.DirectoryHeaderFinder;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class TestReturnChunk {
	private static final ValueChunk RESULT = new ConstIntChunk(42069);

	@Test
	void testExecute() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final ReturnChunk chunk = new ReturnChunk(RESULT);

		assertThrows(ClslRuntimeException.class, () -> chunk.execute(env));
	}

	@Test
	void testIO() throws IOException {
		final ReturnChunk original = new ReturnChunk(RESULT);

		assertEquals(
				original,
				transmitAndReceive(original, ReturnChunk::new)
		);
	}

	@Test
	void testOptimize() {
		final OptimizationEnvironment env = new OptimizationEnvironment(new ArrayList<>());
		final ReturnChunk chunk = new ReturnChunk((ValueChunk) null);
		final ReturnChunk chunkWithResult = new ReturnChunk(RESULT);

		assertEquals(chunk, chunk.optimize(env));
		assertEquals(chunkWithResult, chunkWithResult.optimize(env));

		final OptimizationEnvironment envSecondPass = env.secondPass();
		assertEquals(chunk, chunk.optimize(envSecondPass));
		assertEquals(chunkWithResult, chunkWithResult.optimize(envSecondPass));
	}

	// FIXME: test for optimization of result

	@Test
	void testToString() {
		assertEquals(
				"return " + RESULT,
				new ReturnChunk(RESULT).toString()
		);
	}

	@Test
	void testToStringVoid() {
		assertEquals(
				"return",
				new ReturnChunk((ValueChunk) null).toString()
		);
	}
}