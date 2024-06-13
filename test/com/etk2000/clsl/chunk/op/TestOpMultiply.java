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

public class TestOpMultiply {
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
		final OpMultiply op = new OpMultiply(new ConstIntChunk(op1), new ConstIntChunk(op2));

		assertEquals(op1 * op2, op.get(env).toInt());
	}

	@Test
	void testIO() throws IOException {
		final OpMultiply original = new OpMultiply(CHUNK_VALUE_0, CHUNK_VALUE_1);

		assertEquals(
				original,
				transmitAndReceive(original, OpMultiply::new)
		);
	}

	@Test
	void testOptimize() {
		final ConstIntChunk one = new ConstIntChunk(1);
		final ConstIntChunk zero = new ConstIntChunk(0);
		final OptimizationEnvironment env = new OptimizationEnvironment(new ArrayList<>());

		// test that normal usage stays the same
		final GetVar getVar = new GetVar("test");
		assertEquals(
				new OpMultiply(getVar, getVar),
				new OpMultiply(getVar, getVar).optimize(env)
		);

		// test that constants are merged
		assertEquals(
				new ConstIntChunk(VALUE_0 * VALUE_1),
				new OpMultiply(CHUNK_VALUE_0, CHUNK_VALUE_1).optimize(env)
		);

		// test that (X * 0) -> 0
		assertEquals(
				zero,
				new OpMultiply(getVar, zero).optimize(env)
		);

		// test that (X * 1) -> X
		assertEquals(
				getVar,
				new OpMultiply(getVar, one).optimize(env)
		);

		// test that (0 * X) -> 0
		assertEquals(
				zero,
				new OpMultiply(zero, getVar).optimize(env)
		);

		// test that (1 * X) -> X
		assertEquals(
				getVar,
				new OpMultiply(one, getVar).optimize(env)
		);
	}

	@Test
	void testToString() {
		final OpMultiply op = new OpMultiply(CHUNK_VALUE_0, CHUNK_VALUE_1);
		assertEquals("(" + VALUE_0 + " * " + VALUE_1 + ')', op.toString());
	}

	@Test
	void testWithFirstOp() {
		final OpMultiply op = new OpMultiply(CHUNK_VALUE_0, CHUNK_VALUE_1);

		final ConstIntChunk newValue = new ConstIntChunk(VALUE_0 + 1);
		final OpMultiply expected = new OpMultiply(newValue, CHUNK_VALUE_1);

		assertEquals(expected, op.withFirstOp(newValue));
	}
}