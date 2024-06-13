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

public class TestOpEquals {
	private static final int VALUE_0 = 1, VALUE_1 = 2;
	private static final ConstIntChunk CHUNK_VALUE_0 = new ConstIntChunk(VALUE_0),
			CHUNK_VALUE_1 = new ConstIntChunk(VALUE_1);

	@CsvSource({
			"0,1,0",
			"1,1,1",
			"3,2,0"
	})
	@ParameterizedTest
	void testGet(int op1, int op2, int expected) {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final OpEquals opEq = new OpEquals(new ConstIntChunk(op1), new ConstIntChunk(op2), false);
		final OpEquals opNe = new OpEquals(new ConstIntChunk(op1), new ConstIntChunk(op2), true);

		assertEquals(expected, opEq.get(env).toInt());
		assertEquals(1 - expected, opNe.get(env).toInt());
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testIO() throws IOException {
		final OpEquals original = new OpEquals(CHUNK_VALUE_0, CHUNK_VALUE_1, false);

		assertEquals(
				original,
				transmitAndReceive(original, OpEquals::new)
		);
	}

	@Test
	void testToString() {
		final OpEquals op = new OpEquals(CHUNK_VALUE_0, CHUNK_VALUE_1, false);
		assertEquals("(" + VALUE_0 + " == " + VALUE_1 + ')', op.toString());
	}

	@Test
	void testToStringNot() {
		final OpEquals op = new OpEquals(CHUNK_VALUE_0, CHUNK_VALUE_1, true);
		assertEquals("(" + VALUE_0 + " != " + VALUE_1 + ')', op.toString());
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testWithFirstOp(boolean not) {
		final OpEquals op = new OpEquals(CHUNK_VALUE_0, CHUNK_VALUE_1, not);

		final ConstIntChunk newValue = new ConstIntChunk(VALUE_0 + 1);
		final OpEquals expected = new OpEquals(newValue, CHUNK_VALUE_1, not);

		assertEquals(expected, op.withFirstOp(newValue));
	}
}