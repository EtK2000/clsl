package com.etk2000.clsl.chunk.op;

import static com.etk2000.clsl.test.TestingUtils.transmitAndReceive;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.chunk.variable.GetVar;
import com.etk2000.clsl.header.DirectoryHeaderFinder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.util.ArrayList;

public class TestOpBinOr {
	private static final int VALUE_0 = 2, VALUE_1 = 3;
	private static final ConstIntChunk CHUNK_VALUE_0 = new ConstIntChunk(VALUE_0),
			CHUNK_VALUE_1 = new ConstIntChunk(VALUE_1);

	@CsvSource({
			"0,0",
			"4,2"
	})
	@ParameterizedTest
	void testGet(int op1, int op2) {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final OpBinOr op = new OpBinOr(new ConstIntChunk(op1), new ConstIntChunk(op2));

		assertEquals(op1 | op2, op.get(env).toInt());
	}

	@Test
	void testIO() throws IOException {
		final OpBinOr original = new OpBinOr(CHUNK_VALUE_0, CHUNK_VALUE_1);

		assertEquals(
				original,
				transmitAndReceive(original, OpBinOr::new)
		);
	}

	@Test
	void testOptimize() {
		final ConstIntChunk zero = new ConstIntChunk(0);
		final OptimizationEnvironment env = new OptimizationEnvironment(new ArrayList<>());

		// test that normal usage stays the same
		final GetVar getVar = new GetVar("test");
		assertEquals(
				new OpBinOr(getVar, getVar),
				new OpBinOr(getVar, getVar).optimize(env)
		);

		// test that constants are merged
		assertEquals(
				new ConstIntChunk(VALUE_0 | VALUE_1),
				new OpBinOr(CHUNK_VALUE_0, CHUNK_VALUE_1).optimize(env)
		);

		// test that (X | 0) -> X
		assertEquals(
				getVar,
				new OpBinOr(getVar, zero).optimize(env)
		);

		// test that (0 | X) -> X
		assertEquals(
				getVar,
				new OpBinOr(zero, getVar).optimize(env)
		);
	}

	@Test
	void testToString() {
		final OpBinOr op = new OpBinOr(CHUNK_VALUE_0, CHUNK_VALUE_1);
		assertEquals("(" + VALUE_0 + " | " + VALUE_1 + ')', op.toString());
	}

	@Test
	void testWithFirstOp() {
		final OpBinOr op = new OpBinOr(CHUNK_VALUE_0, CHUNK_VALUE_1);

		final ConstIntChunk newValue = new ConstIntChunk(VALUE_0 + 1);
		final OpBinOr expected = new OpBinOr(newValue, CHUNK_VALUE_1);

		assertEquals(expected, op.withFirstOp(newValue));
	}
}