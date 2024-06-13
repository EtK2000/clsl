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

public class TestOpOr {
	private static final int VALUE_0 = 1, VALUE_1 = 2;
	private static final ConstIntChunk CHUNK_VALUE_0 = new ConstIntChunk(VALUE_0),
			CHUNK_VALUE_1 = new ConstIntChunk(VALUE_1);

	@CsvSource({
			"0,0,0",
			"0,1,1",
			"9,3,1",
	})
	@ParameterizedTest
	void testGet(int op1, int op2, int expected) {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final OpOr op = new OpOr(new ConstIntChunk(op1), new ConstIntChunk(op2));

		assertEquals(expected, op.get(env).toInt());
	}

	@Test
	void testIO() throws IOException {
		final OpOr original = new OpOr(CHUNK_VALUE_0, CHUNK_VALUE_1);

		assertEquals(
				original,
				transmitAndReceive(original, OpOr::new)
		);
	}

	@Test
	void testToString() {
		final OpOr op = new OpOr(CHUNK_VALUE_0, CHUNK_VALUE_1);
		assertEquals("(" + VALUE_0 + " || " + VALUE_1 + ')', op.toString());
	}

	@Test
	void testWithFirstOp() {
		final OpOr op = new OpOr(CHUNK_VALUE_0, CHUNK_VALUE_1);

		final ConstIntChunk newValue = new ConstIntChunk(VALUE_0 + 1);
		final OpOr expected = new OpOr(newValue, CHUNK_VALUE_1);

		assertEquals(expected, op.withFirstOp(newValue));
	}
}