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

public class TestOpAnd {
	private static final int VALUE_0 = 1, VALUE_1 = 2;
	private static final ConstIntChunk CHUNK_VALUE_0 = new ConstIntChunk(VALUE_0),
			CHUNK_VALUE_1 = new ConstIntChunk(VALUE_1);

	@CsvSource({
			"0,1,0",
			"1,1,1",
			"2,2,1"
	})
	@ParameterizedTest
	void testGet(int op1, int op2, int expected) {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final OpAnd op = new OpAnd(new ConstIntChunk(op1), new ConstIntChunk(op2));

		assertEquals(expected, op.get(env).toInt());
	}

	@Test
	void testIO() throws IOException {
		final OpAnd original = new OpAnd(CHUNK_VALUE_0, CHUNK_VALUE_1);

		assertEquals(
				original,
				transmitAndReceive(original, OpAnd::new)
		);
	}

	@Test
	void testToString() {
		final OpAnd op = new OpAnd(CHUNK_VALUE_0, CHUNK_VALUE_1);
		assertEquals("(" + VALUE_0 + " && " + VALUE_1 + ')', op.toString());
	}

	@Test
	void testWithFirstOp() {
		final OpAnd op = new OpAnd(CHUNK_VALUE_0, CHUNK_VALUE_1);

		final ConstIntChunk newValue = new ConstIntChunk(VALUE_0 + 1);
		final OpAnd expected = new OpAnd(newValue, CHUNK_VALUE_1);

		assertEquals(expected, op.withFirstOp(newValue));
	}
}