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

public class TestOpBool {
	private static final int VALUE = 1;
	private static final ConstIntChunk CHUNK_VALUE = new ConstIntChunk(VALUE);

	@CsvSource({
			"0,0",
			"1,1",
			"2,1",
	})
	@ParameterizedTest
	void testGet(int value, int expected) {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final OpBool op = new OpBool(new ConstIntChunk(value));

		assertEquals(expected, op.get(env).toInt());
	}

	@Test
	void testIO() throws IOException {
		final OpBool original = new OpBool(CHUNK_VALUE);

		assertEquals(
				original,
				transmitAndReceive(original, OpBool::new)
		);
	}

	@Test
	void testToString() {
		final OpBool op = new OpBool(CHUNK_VALUE);
		assertEquals(Integer.toString(VALUE), op.toString());
	}
}