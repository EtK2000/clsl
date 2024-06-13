package com.etk2000.clsl.chunk.op;

import static com.etk2000.clsl.test.TestingUtils.transmitAndReceive;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.chunk.variable.GetVar;
import com.etk2000.clsl.header.DirectoryHeaderFinder;
import com.etk2000.clsl.value.ClslArray;
import com.etk2000.clsl.value.ClslIntConst;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestOpIndex {
	private static final String VARIABLE_NAME = "test", VARIABLE_INDEX = "idx";
	private static final GetVar VARIABLE_ACCESS = new GetVar(VARIABLE_NAME),
			VARIABLE_INDEX_ACCESS = new GetVar(VARIABLE_INDEX);

	@Test
	void testGet() {
		final int index = 1;
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final OpIndex op = new OpIndex(VARIABLE_ACCESS, new ConstIntChunk(index));

		final ClslIntConst[] expected = new ClslIntConst[]{
				ClslIntConst.of(1),
				ClslIntConst.of(2),
		};

		env.defineVar(VARIABLE_NAME, new ClslArray(expected));

		assertEquals(expected[index], op.get(env));
	}

	// FIXME: test for invalid index/OOB
	// FIXME: test for call on something that doesn't have the op

	@Test
	void testIO() throws IOException {
		final OpIndex original = new OpIndex(VARIABLE_ACCESS, VARIABLE_INDEX_ACCESS);

		assertEquals(
				original,
				transmitAndReceive(original, OpIndex::new)
		);
	}

	@Test
	void testToString() {
		final OpIndex op = new OpIndex(VARIABLE_ACCESS, VARIABLE_INDEX_ACCESS);
		assertEquals("(" + VARIABLE_ACCESS + '[' + VARIABLE_INDEX_ACCESS + "])", op.toString());
	}

	@Test
	void testWithFirstOp() {
		final OpIndex op = new OpIndex(VARIABLE_ACCESS, VARIABLE_INDEX_ACCESS);

		final GetVar newValue = new GetVar(VARIABLE_NAME + "a");
		final OpIndex expected = new OpIndex(newValue, VARIABLE_INDEX_ACCESS);

		assertEquals(expected, op.withOp(newValue));
	}
}