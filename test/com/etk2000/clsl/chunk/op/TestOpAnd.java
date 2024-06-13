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
	void testOptimize() {
		final ConstIntChunk one = new ConstIntChunk(1);
		final ConstIntChunk zero = new ConstIntChunk(0);
		final OptimizationEnvironment env = new OptimizationEnvironment(new ArrayList<>());

		// test that normal usage stays the same
		final GetVar getVar = new GetVar("test");
		assertEquals(
				new OpAnd(getVar, getVar),
				new OpAnd(getVar, getVar).optimize(env)
		);

		// test that constants are merged
		assertEquals(
				one,
				new OpAnd(CHUNK_VALUE_0, CHUNK_VALUE_1).optimize(env)
		);

		// test that (X && 0) -> 0
		assertEquals(
				zero,
				new OpAnd(getVar, zero).optimize(env)
		);

		// test that (X && 1) -> X
		assertEquals(
				getVar,
				new OpAnd(getVar, one).optimize(env)
		);

		// test that (0 && X) -> 0
		assertEquals(
				zero,
				new OpAnd(zero, getVar).optimize(env)
		);

		// test that (1 && X) -> X
		assertEquals(
				getVar,
				new OpAnd(one, getVar).optimize(env)
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