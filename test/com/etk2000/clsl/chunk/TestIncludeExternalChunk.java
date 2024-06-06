package com.etk2000.clsl.chunk;

import static com.etk2000.clsl.test.TestingUtils.transmitAndReceive;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.etk2000.clsl.ClslCode;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.MainScopeLookupContainer;
import com.etk2000.clsl.exception.include.ClslHeaderNotFoundException;
import com.etk2000.clsl.header.DirectoryHeaderFinder;
import com.etk2000.clsl.test.TestingExternalHeaderFinder;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestIncludeExternalChunk {
	@Test
	void testExecute() {
		final TestingExternalHeaderFinder headerFinder = new TestingExternalHeaderFinder();
		final ClslRuntimeEnv env = new ClslRuntimeEnv(headerFinder, new MainScopeLookupContainer());
		final IncludeExternalChunk chunk = new IncludeExternalChunk("test");

		headerFinder.add("test", ClslCode.NOP);
		// FIXME: test that include code is called

		assertDoesNotThrow(() -> chunk.execute(env));
	}

	@Test
	void testExecuteHeaderNotFound() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final IncludeExternalChunk chunk = new IncludeExternalChunk("missing");

		assertThrows(ClslHeaderNotFoundException.class, () -> chunk.execute(env));
	}

	@Test
	void testIO() throws IOException {
		final IncludeExternalChunk original = new IncludeExternalChunk("test");

		assertEquals(
				original,
				transmitAndReceive(original, IncludeExternalChunk::new)
		);
	}

	@Test
	void testToString() {
		assertEquals(
				"#include \"test\"",
				new IncludeExternalChunk("test").toString()
		);
	}
}