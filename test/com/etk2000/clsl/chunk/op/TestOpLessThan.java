package com.etk2000.clsl.chunk.op;

import static com.etk2000.clsl.test.TestingUtils.transmitAndReceive;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.header.DirectoryHeaderFinder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

public class TestOpLessThan {
	private static final int VALUE_0 = 1, VALUE_1 = 2;
	private static final ConstIntChunk CHUNK_VALUE_0 = new ConstIntChunk(VALUE_0),
			CHUNK_VALUE_1 = new ConstIntChunk(VALUE_1);

	@CsvSource({
			"0,1,1,1",
			"1,1,0,1",
			"3,2,0,0"
	})
	@ParameterizedTest
	void testGet(int op1, int op2, int expectedLt, int expectedLte) {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final OpLessThan opLt = new OpLessThan(new ConstIntChunk(op1), new ConstIntChunk(op2), false);
		final OpLessThan opLte = new OpLessThan(new ConstIntChunk(op1), new ConstIntChunk(op2), true);

		assertEquals(expectedLt, opLt.get(env).toInt());
		assertEquals(expectedLte, opLte.get(env).toInt());
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testIO() throws IOException {
		final OpLessThan original = new OpLessThan(CHUNK_VALUE_0, CHUNK_VALUE_1, false);

		assertEquals(
				original,
				transmitAndReceive(original, OpLessThan::new)
		);
	}

	@Test
	void testToString() {
		final OpLessThan op = new OpLessThan(CHUNK_VALUE_0, CHUNK_VALUE_1, false);
		assertEquals("(" + VALUE_0 + " < " + VALUE_1 + ')', op.toString());
	}

	@Test
	void testToStringEqual() {
		final OpLessThan op = new OpLessThan(CHUNK_VALUE_0, CHUNK_VALUE_1, true);
		assertEquals("(" + VALUE_0 + " <= " + VALUE_1 + ')', op.toString());
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testWithFirstOp(boolean not) {
		final OpLessThan op = new OpLessThan(CHUNK_VALUE_0, CHUNK_VALUE_1, not);

		final ConstIntChunk newValue = new ConstIntChunk(VALUE_0 + 1);
		final OpLessThan expected = new OpLessThan(newValue, CHUNK_VALUE_1, not);

		assertEquals(expected, op.withFirstOp(newValue));
	}
}