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

public class TestOpNot {
	private static final int VALUE = 1;
	private static final ConstIntChunk CHUNK_VALUE = new ConstIntChunk(VALUE);

	@CsvSource({
			"0,1",
			"1,0",
			"2,0",
	})
	@ParameterizedTest
	void testGet(int value, int expected) {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final OpNot op = new OpNot(new ConstIntChunk(value));

		assertEquals(expected, op.get(env).toInt());
	}

	@Test
	void testIO() throws IOException {
		final OpNot original = new OpNot(CHUNK_VALUE);

		assertEquals(
				original,
				transmitAndReceive(original, OpNot::new)
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
				new OpNot(getVar),
				new OpNot(getVar).optimize(env)
		);

		// test that !0 -> 1
		assertEquals(
				one,
				new OpNot(zero).optimize(env)
		);

		// test that !1 -> 0
		assertEquals(
				zero,
				new OpNot(one).optimize(env)
		);
	}

	@Test
	void testToString() {
		final OpNot op = new OpNot(CHUNK_VALUE);
		assertEquals("!" + VALUE, op.toString());
	}
}