package com.etk2000.clsl.chunk.op;

import static com.etk2000.clsl.test.TestingUtils.transmitAndReceive;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.header.DirectoryHeaderFinder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

public class TestOpNot {
	private static final int VALUE = 1;
	private static final ConstIntChunk CHUNK_VALUE = new ConstIntChunk(VALUE);

	@CsvSource({
			"0,1",
			"1,0",
			"2,0",
	})
	@ParameterizedTest
	void testGet(int value, int expected) {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final OpNot op = new OpNot(new ConstIntChunk(value));

		assertEquals(expected, op.get(env).toInt());
	}

	@Test
	void testIO() throws IOException {
		final OpNot original = new OpNot(CHUNK_VALUE);

		assertEquals(
				original,
				transmitAndReceive(original, OpNot::new)
		);
	}

	@Test
	void testToString() {
		final OpNot op = new OpNot(CHUNK_VALUE);
		assertEquals("!" + VALUE, op.toString());
	}
}