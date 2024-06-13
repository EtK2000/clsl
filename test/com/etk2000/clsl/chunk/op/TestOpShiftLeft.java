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

public class TestOpShiftLeft {
	private static final int VALUE_0 = 1, VALUE_1 = 2;
	private static final ConstIntChunk CHUNK_VALUE_0 = new ConstIntChunk(VALUE_0),
			CHUNK_VALUE_1 = new ConstIntChunk(VALUE_1);

	@CsvSource({
			"0,1",
			"1,1",
			"3,2"
	})
	@ParameterizedTest
	void testGet(int op1, int op2) {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final OpShiftLeft op = new OpShiftLeft(new ConstIntChunk(op1), new ConstIntChunk(op2));

		assertEquals(op1 << op2, op.get(env).toInt());
	}

	@Test
	void testIO() throws IOException {
		final OpShiftLeft original = new OpShiftLeft(CHUNK_VALUE_0, CHUNK_VALUE_1);

		assertEquals(
				original,
				transmitAndReceive(original, OpShiftLeft::new)
		);
	}

	@Test
	void testToString() {
		final OpShiftLeft op = new OpShiftLeft(CHUNK_VALUE_0, CHUNK_VALUE_1);
		assertEquals("(" + VALUE_0 + " << " + VALUE_1 + ')', op.toString());
	}

	@Test
	void testWithFirstOp() {
		final OpShiftLeft op = new OpShiftLeft(CHUNK_VALUE_0, CHUNK_VALUE_1);

		final ConstIntChunk newValue = new ConstIntChunk(VALUE_0 + 1);
		final OpShiftLeft expected = new OpShiftLeft(newValue, CHUNK_VALUE_1);

		assertEquals(expected, op.withFirstOp(newValue));
	}
}