package com.etk2000.clsl.chunk.op;

import static com.etk2000.clsl.test.TestingUtils.transmitAndReceive;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.chunk.variable.GetVar;
import com.etk2000.clsl.header.DirectoryHeaderFinder;
import com.etk2000.clsl.value.ClslIntConst;
import com.etk2000.clsl.value.ClslStructConstMapped;
import com.etk2000.clsl.value.ClslValue;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestOpMember {
	private static final String VARIABLE_NAME = "test", VARIABLE_MEMBER = "mbr";
	private static final GetVar VARIABLE_ACCESS = new GetVar(VARIABLE_NAME);

	@Test
	void testGet() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final OpMember op = new OpMember(VARIABLE_ACCESS, VARIABLE_MEMBER);

		final Map<String, ClslValue> expected = new HashMap<>();
		expected.put(VARIABLE_MEMBER, ClslIntConst.of(42));
		env.defineVar(VARIABLE_NAME, new ClslStructConstMapped(expected));

		assertEquals(expected.get(VARIABLE_MEMBER), op.get(env));
	}

	// FIXME: test for invalid member
	// FIXME: test for call on something that doesn't have the op

	@Test
	void testIO() throws IOException {
		final OpMember original = new OpMember(VARIABLE_ACCESS, VARIABLE_MEMBER);

		assertEquals(
				original,
				transmitAndReceive(original, OpMember::new)
		);
	}

	@Test
	void testToString() {
		final OpMember op = new OpMember(VARIABLE_ACCESS, VARIABLE_MEMBER);
		assertEquals("(" + VARIABLE_ACCESS + '.' + VARIABLE_MEMBER + ')', op.toString());
	}

	@Test
	void testWithFirstOp() {
		final OpMember op = new OpMember(VARIABLE_ACCESS, VARIABLE_MEMBER);

		final GetVar newValue = new GetVar(VARIABLE_NAME + "a");
		final OpMember expected = new OpMember(newValue, VARIABLE_MEMBER);

		assertEquals(expected, op.withOp(newValue));
	}
}