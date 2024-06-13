package com.etk2000.clsl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.etk2000.clsl.compiler.ClslCompiler;
import com.etk2000.clsl.exception.type.ClslUnknownTypeException;
import com.etk2000.clsl.header.DirectoryHeaderFinder;
import com.etk2000.clsl.value.ClslStructConstMapped;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class TestClslStructInterop {
	private static final String STRUCT_NAME = "test";

	private ClslStructConstMapped createStruct() {
		return new ClslStructConstMapped(new HashMap<>());
	}

	@Test
	void testFlowCompiled() {
		final String variableName = "abc";
		final ClslRuntimeEnv env = new ClslRuntimeEnv(new DirectoryHeaderFinder());
		final ClslCode code = ClslCompiler.compile(
				"struct " + STRUCT_NAME + ' ' + variableName + ';',
				false
		);

		assertThrows(ClslUnknownTypeException.class, () -> code.execute(env));

		// TODO: replace `registerStruct` call with code that does the same when supported

		env.structInterop.registerStruct(STRUCT_NAME, this::createStruct);
		assertDoesNotThrow(() -> code.execute(env));
		assertInstanceOf(ClslStructConstMapped.class, env.getVar(variableName));
	}

	@Test
	void testFlowRaw() {
		final ClslStructInterop structInterop = new ClslStructInterop();

		assertThrows(ClslUnknownTypeException.class, () -> structInterop.newInstance(STRUCT_NAME));

		structInterop.registerStruct(STRUCT_NAME, this::createStruct);

		assertNotSame(
				structInterop.newInstance(STRUCT_NAME),
				structInterop.newInstance(STRUCT_NAME)
		);
	}
}