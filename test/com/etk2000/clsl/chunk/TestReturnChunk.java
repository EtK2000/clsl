package com.etk2000.clsl.chunk;

import static com.etk2000.clsl.test.TestingUtils.transmitAndReceive;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.MainScopeLookupContainer;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.exception.ClslRuntimeException;
import com.etk2000.clsl.header.DirectoryHeaderFinder;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestReturnChunk {
	@Test
	void testExecute() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final ReturnChunk chunk = new ReturnChunk((ValueChunk) null);

		assertThrows(ClslRuntimeException.class, () -> chunk.execute(env));
	}

	@Test
	void testIO() throws IOException {
		final ReturnChunk original = new ReturnChunk(new ConstIntChunk(42069));

		assertEquals(
				original.val,
				transmitAndReceive(original, ReturnChunk::new).val
		);
	}

	@Test
	void testToString() {
		assertEquals(
				"return 42069",
				new ReturnChunk(new ConstIntChunk(42069)).toString()
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