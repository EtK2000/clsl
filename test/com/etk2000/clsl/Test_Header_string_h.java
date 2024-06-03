package com.etk2000.clsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.etk2000.clsl.exception.ClslRuntimeException;
import com.etk2000.clsl.exception.variable.ClslBufferOverflowException;

import org.junit.jupiter.api.Test;

public class Test_Header_string_h {
	private static final _Header_string_h HEADER = new _Header_string_h();

	@Test
	void testStrcatBufferOverflow() {
		final CLSLRuntimeEnv env = new CLSLRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final FunctionalChunk strcat = HEADER.lookup("strcat");

		assertThrows(ClslBufferOverflowException.class, () -> strcat.call(env, new CLSLArrayConst("1"), new CLSLArrayConst("2")));
	}

	@Test
	void testStrcatWrongArgumentCount() {
		final CLSLRuntimeEnv env = new CLSLRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final FunctionalChunk strcat = HEADER.lookup("strcat");

		assertThrows(ClslRuntimeException.class, () -> strcat.call(env, new CLSLArrayConst("1")));
		assertThrows(ClslRuntimeException.class, () -> strcat.call(env, new CLSLArrayConst("1"), new CLSLArrayConst("2"), new CLSLArrayConst("3")));
	}

	// TODO: testStrcatWrongArgumentTypes

	@Test
	void testStrcat() {
		final CLSLRuntimeEnv env = new CLSLRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final FunctionalChunk strcat = HEADER.lookup("strcat");

		final CLSLArray target = new CLSLArray(ValueType.CHAR, (short) 15);

		assertSame(target, strcat.call(env, target, new CLSLArrayConst("hello")));
		assertEquals(target.toJava(), "hello");

		assertSame(target, strcat.call(env, target, new CLSLArrayConst(" world")));
		assertEquals(target.toJava(), "hello world");
	}
}