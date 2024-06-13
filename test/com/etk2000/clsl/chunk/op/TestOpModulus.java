package com.etk2000.clsl.chunk.op;

import static com.etk2000.clsl.test.TestingUtils.transmitAndReceive;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.exception.math.ClslDivisionByZeroException;
import com.etk2000.clsl.header.DirectoryHeaderFinder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

public class TestOpModulus {
	private static final int VALUE_0 = 1, VALUE_1 = 2;
	private static final ConstIntChunk CHUNK_VALUE_0 = new ConstIntChunk(VALUE_0),
			CHUNK_VALUE_1 = new ConstIntChunk(VALUE_1);

	@CsvSource({
			"0,1",
			"1,1",
			"8,3"
	})
	@ParameterizedTest
	void testGet(int op1, int op2) {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final OpModulus op = new OpModulus(new ConstIntChunk(op1), new ConstIntChunk(op2));

		assertEquals(op1 % op2, op.get(env).toInt());
	}

	@Test
	void testGetDivisionByZero() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final OpModulus op = new OpModulus(CHUNK_VALUE_0, new ConstIntChunk(0));

		// TODO: wrap in a ClslException?
		assertThrows(ClslDivisionByZeroException.class, () -> op.get(env));
	}

	@Test
	void testIO() throws IOException {
		final OpModulus original = new OpModulus(CHUNK_VALUE_0, CHUNK_VALUE_1);

		assertEquals(
				original,
				transmitAndReceive(original, OpModulus::new)
		);
	}

	@Test
	void testToString() {
		final OpModulus op = new OpModulus(CHUNK_VALUE_0, CHUNK_VALUE_1);
		assertEquals("(" + VALUE_0 + " % " + VALUE_1 + ')', op.toString());
	}

	@Test
	void testWithFirstOp() {
		final OpModulus op = new OpModulus(CHUNK_VALUE_0, CHUNK_VALUE_1);

		final ConstIntChunk newValue = new ConstIntChunk(VALUE_0 + 1);
		final OpModulus expected = new OpModulus(newValue, CHUNK_VALUE_1);

		assertEquals(expected, op.withFirstOp(newValue));
	}
}