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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class TestStringH {
	@Test
	void testStrcatBufferOverflow() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final FunctionalChunk strcat = StringH.INSTANCE.lookup("strcat");

		assertThrows(ClslBufferOverflowException.class, () -> strcat.call(env, new ClslArrayConst("1"), new ClslArrayConst("2")));
	}

	// TODO: testStrcatConstDestination

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
		assertEquals("hello", target.toJava());

		assertSame(target, strcat.call(env, target, new ClslArrayConst(" world")));
		assertEquals("hello world", target.toJava());
	}

	//

	@Test
	void testStrcmpWrongArgumentCount() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final FunctionalChunk strcmp = StringH.INSTANCE.lookup("strcmp");

		assertThrows(ClslRuntimeException.class, () -> strcmp.call(env, new ClslArrayConst("1")));
		assertThrows(ClslRuntimeException.class, () -> strcmp.call(env, new ClslArrayConst("1"), new ClslArrayConst("2"), new ClslArrayConst("3")));
	}

	// TODO: testStrcmpWrongArgumentTypes

	@CsvSource({
			"a,b,-1",
			"a,a,0",
			"b,a,1"
	})
	@ParameterizedTest
	void testStrcmp(String value0, String value1, int expectedResult) {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final FunctionalChunk strcmp = StringH.INSTANCE.lookup("strcmp");

		assertEquals(expectedResult, strcmp.call(
				env,
				new ClslArrayConst(value0),
				new ClslArrayConst(value1)
		).toInt());
	}

	//

	// TODO: testStrcpyConstDestination

	@Test
	void testStrcpyWrongArgumentCount() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final FunctionalChunk strcpy = StringH.INSTANCE.lookup("strcpy");

		assertThrows(ClslRuntimeException.class, () -> strcpy.call(env, new ClslArrayConst("1")));
		assertThrows(ClslRuntimeException.class, () -> strcpy.call(env, new ClslArrayConst("1"), new ClslArrayConst("2"), new ClslArrayConst("3")));
	}

	// TODO: testStrcpyWrongArgumentTypes

	@Test
	void testStrcpy() {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final FunctionalChunk strcpy = StringH.INSTANCE.lookup("strcpy");

		final ClslArray target = new ClslArray(ValueType.CHAR, (short) 15);

		assertSame(target, strcpy.call(env, target, new ClslArrayConst("hello")));
		assertEquals("hello", target.toJava());

		assertSame(target, strcpy.call(env, target, new ClslArrayConst(" world")));
		assertEquals(" world", target.toJava());
	}


	//

	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"test",
			"a very long string"
	})
	void testStrlen(String value) {
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder(), new MainScopeLookupContainer());
		final FunctionalChunk strlen = StringH.INSTANCE.lookup("strlen");

		assertEquals(value.length(), strlen.call(env, new ClslArrayConst(value)).toInt());
	}
}