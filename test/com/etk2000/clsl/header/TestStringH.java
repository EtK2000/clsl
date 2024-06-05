package com.etk2000.clsl.header;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.MainScopeLookupContainer;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.chunk.FunctionalChunk;
import com.etk2000.clsl.exception.ClslRuntimeException;
import com.etk2000.clsl.exception.variable.ClslBufferOverflowException;
import com.etk2000.clsl.value.ClslArray;
import com.etk2000.clsl.value.ClslArrayConst;

import org.junit.jupiter.api.Test;

public class TestStringH {
	@Test
	void testStrcatBufferOverflow() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final FunctionalChunk strcat = StringH.INSTANCE.lookup("strcat");

		assertThrows(ClslBufferOverflowException.class, () -> strcat.call(env, new ClslArrayConst("1"), new ClslArrayConst("2")));
	}

	@Test
	void testStrcatWrongArgumentCount() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final FunctionalChunk strcat = StringH.INSTANCE.lookup("strcat");

		assertThrows(ClslRuntimeException.class, () -> strcat.call(env, new ClslArrayConst("1")));
		assertThrows(ClslRuntimeException.class, () -> strcat.call(env, new ClslArrayConst("1"), new ClslArrayConst("2"), new ClslArrayConst("3")));
	}

	// TODO: testStrcatWrongArgumentTypes

	@Test
	void testStrcat() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final FunctionalChunk strcat = StringH.INSTANCE.lookup("strcat");

		final ClslArray target = new ClslArray(ValueType.CHAR, (short) 15);

		assertSame(target, strcat.call(env, target, new ClslArrayConst("hello")));
		assertEquals(target.toJava(), "hello");

		assertSame(target, strcat.call(env, target, new ClslArrayConst(" world")));
		assertEquals(target.toJava(), "hello world");
	}
}