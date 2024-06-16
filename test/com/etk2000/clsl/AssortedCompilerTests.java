package com.etk2000.clsl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.FunctionCallChunk;
import com.etk2000.clsl.chunk.FunctionalChunk;
import com.etk2000.clsl.chunk.IfChunk;
import com.etk2000.clsl.chunk.op.OpIndex;
import com.etk2000.clsl.chunk.op.OpMember;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.chunk.variable.GetVar;
import com.etk2000.clsl.compiler.ClslCompiler;

import org.junit.jupiter.api.Test;

public class AssortedCompilerTests {
	private static final ValueType[] VALID_FUNCTION_RETURN_TYPES = {
			ValueType.CHAR,
			ValueType.DOUBLE,
			ValueType.FLOAT,
			ValueType.INT,
			ValueType.LONG,
			ValueType.STRUCT,// FIXME: change if/when syntax changes
			ValueType.VOID

			// FIXME: add in modifiers (<T>*, <T>[], const <T>, etc)
	};

	@Test
	void testParsingFunctionCallInIf() {
		final String functionName = "func";
		final ClslCode code = ClslCompiler.compile(
				"if (" + functionName + "());",
				true
		);

		assertEquals(1, code.chunks.size());
		assertEquals(
				new IfChunk(
						new FunctionCallChunk(new GetVar(functionName)),
						new ExecutableChunk[0]
				),
				code.chunks.get(0)
		);
	}

	@Test
	void testParsingFunctionCallWithIndexesInIf() {
		final String functionName = "func";
		final int index0 = 42;
		final int index1 = 69;
		final ClslCode code = ClslCompiler.compile(
				"if (" + functionName + "()[" + index0 + "][" + index1 + "]);",
				true
		);

		assertEquals(1, code.chunks.size());
		assertEquals(
				new IfChunk(
						new OpIndex(
								new OpIndex(
										new FunctionCallChunk(new GetVar(functionName)),
										new ConstIntChunk(index0)
								),
								new ConstIntChunk(index1)
						),
						new ExecutableChunk[0]
				),
				code.chunks.get(0)
		);
	}

	@Test
	void testParsingFunctionCallWithMembersInIf() {
		final String functionName = "func";
		final String memberName0 = "test";
		final String memberName1 = "another";
		final ClslCode code = ClslCompiler.compile(
				"if (" + functionName + "()." + memberName0 + '.' + memberName1 + ");",
				true
		);

		assertEquals(1, code.chunks.size());
		assertEquals(
				new IfChunk(
						new OpMember(
								new OpMember(
										new FunctionCallChunk(new GetVar(functionName)),
										memberName0
								), memberName1
						),
						new ExecutableChunk[0]
				),
				code.chunks.get(0)
		);
	}

	@Test
	void testParsingFunctionNoArgs() {
		final String functionName = "func";
		final String innerCode = "doSomething();";

		for (ValueType type : VALID_FUNCTION_RETURN_TYPES) {

			final ClslCode code = ClslCompiler.compile(
					type.name().toLowerCase() + ' ' + functionName + "(){" + innerCode + '}',
					true
			);

			assertEquals(1, code.chunks.size());
			assertEquals(
					new FunctionalChunk(
							functionName,
							type,
							new Group[0],
							ClslCompiler.compile(innerCode, false).chunks.toArray(new ExecutableChunk[0])
					),
					code.chunks.get(0)
			);
		}
	}
}