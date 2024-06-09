package com.etk2000.clsl.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.etk2000.clsl.Group;
import com.etk2000.clsl.chunk.ExecutableValueChunk;
import com.etk2000.clsl.chunk.FunctionCallChunk;
import com.etk2000.clsl.chunk.VariableAccess;
import com.etk2000.clsl.chunk.op.OpDec;
import com.etk2000.clsl.chunk.op.OpInc;
import com.etk2000.clsl.chunk.op.OpIndex;
import com.etk2000.clsl.chunk.op.OpMember;
import com.etk2000.clsl.chunk.variable.GetVar;
import com.etk2000.clsl.chunk.variable.set.SetVar;
import com.etk2000.clsl.chunk.variable.set.SetVarAbstract;
import com.etk2000.clsl.chunk.variable.set.SetVarAdd;
import com.etk2000.clsl.chunk.variable.set.SetVarBinAnd;
import com.etk2000.clsl.chunk.variable.set.SetVarBinOr;
import com.etk2000.clsl.chunk.variable.set.SetVarDiv;
import com.etk2000.clsl.chunk.variable.set.SetVarModulus;
import com.etk2000.clsl.chunk.variable.set.SetVarMul;
import com.etk2000.clsl.chunk.variable.set.SetVarShiftLeft;
import com.etk2000.clsl.chunk.variable.set.SetVarShiftRight;
import com.etk2000.clsl.chunk.variable.set.SetVarSub;
import com.etk2000.clsl.chunk.variable.set.SetVarXor;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TestOperatorParser {
	private static final String VARIABLE_0 = "test", VARIABLE_1 = "another",
			VARIABLE_0_INDEX = "idx", VARIABLE_1_INDEX = "idy",
			VARIABLE_0_MEMBER = "something", VARIABLE_1_MEMBER = "thing";
	private static final GetVar VARIABLE_0_ACCESS = new GetVar(VARIABLE_0), VARIABLE_1_ACCESS = new GetVar(VARIABLE_1);
	private static final OpIndex VARIABLE_0_INDEX_ACCESS = new OpIndex(VARIABLE_0_ACCESS, new GetVar(VARIABLE_0_INDEX)),
			VARIABLE_1_INDEX_ACCESS = new OpIndex(VARIABLE_1_ACCESS, new GetVar(VARIABLE_1_INDEX));
	private static final OpMember VARIABLE_0_MEMBER_ACCESS = new OpMember(VARIABLE_0_ACCESS, VARIABLE_0_MEMBER),
			VARIABLE_1_MEMBER_ACCESS = new OpMember(VARIABLE_1_ACCESS, VARIABLE_1_MEMBER);

	// FIXME: do something similar to the below for DEC, INC, and FUNC()
	private static void testBiOp(
			BiFunction<VariableAccess, VariableAccess, SetVarAbstract> newInstance,
			String operator
	) {
		final List<Group<SetVarAbstract, String>> toTest = new ArrayList<>();

		//  a, x
		toTest.add(new Group<>(
				newInstance.apply(VARIABLE_0_ACCESS, VARIABLE_1_ACCESS),
				VARIABLE_0 + operator + VARIABLE_1
		));

		// a.b, x
		toTest.add(new Group<>(
				newInstance.apply(VARIABLE_0_MEMBER_ACCESS, VARIABLE_1_ACCESS),
				VARIABLE_0 + '.' + VARIABLE_0_MEMBER + operator + VARIABLE_1
		));

		// a, x.y
		toTest.add(new Group<>(
				newInstance.apply(VARIABLE_0_ACCESS, VARIABLE_1_MEMBER_ACCESS),
				VARIABLE_0 + operator + VARIABLE_1 + '.' + VARIABLE_1_MEMBER
		));

		// a[c], x
		toTest.add(new Group<>(
				newInstance.apply(VARIABLE_0_INDEX_ACCESS, VARIABLE_1_ACCESS),
				VARIABLE_0 + '[' + VARIABLE_0_INDEX + ']' + operator + VARIABLE_1
		));

		// a, x[z]
		toTest.add(new Group<>(
				newInstance.apply(VARIABLE_0_ACCESS, VARIABLE_1_INDEX_ACCESS),
				VARIABLE_0 + operator + VARIABLE_1 + '[' + VARIABLE_1_INDEX + ']'
		));

		for (Group<SetVarAbstract, String> currentTest : toTest) {
			final SetVarAbstract expected = currentTest.a;
			final ClslCompilationEnv env = new ClslCompilationEnv(currentTest.b + ';');
			assertTrue(env.matcher.find());

			OperatorParser.parseNext(env);
			assertEquals(1, env.exec.size(), '"' + currentTest.b + '"');
			assertEquals(expected, env.exec.get(0), '"' + currentTest.b + '"');
		}
	}

	private static void testOp(
			Function<VariableAccess, ExecutableValueChunk> newInstance,
			String operator,
			boolean post
	) {
		final List<Group<ExecutableValueChunk, String>> toTest = new ArrayList<>();

		final String postStr = post ? operator : "";
		final String preStr = post ? "" : operator;

		//  a
		toTest.add(new Group<>(
				newInstance.apply(VARIABLE_0_ACCESS),
				preStr + VARIABLE_0 + postStr
		));

		// a.b
		toTest.add(new Group<>(
				newInstance.apply(VARIABLE_0_MEMBER_ACCESS),
				preStr + VARIABLE_0 + '.' + VARIABLE_0_MEMBER + postStr
		));

		// a[c]
		toTest.add(new Group<>(
				newInstance.apply(VARIABLE_0_INDEX_ACCESS),
				preStr + VARIABLE_0 + '[' + VARIABLE_0_INDEX + ']' + postStr
		));

		// FIXME: add tests for INDEX access

		for (Group<ExecutableValueChunk, String> currentTest : toTest) {
			final ExecutableValueChunk expected = currentTest.a;
			final ClslCompilationEnv env = new ClslCompilationEnv(currentTest.b + ';');
			assertTrue(env.matcher.find());

			OperatorParser.parseNext(env);
			assertEquals(1, env.exec.size(), '"' + currentTest.b + '"');
			assertEquals(expected, env.exec.get(0), '"' + currentTest.b + '"');
		}
	}

	@Test
	void testParsingDecrement() {
		testOp(variableAccess -> new OpDec(variableAccess, true), "--", true);
		testOp(variableAccess -> new OpDec(variableAccess, false), "--", false);
	}

	@Test
	void testParsingFunctionCall() {
		testOp(FunctionCallChunk::new, "()", true);
	}

	// FIXME: testParsingFunctionCallWithArguments

	@Test
	void testParsingIncrement() {
		testOp(variableAccess -> new OpInc(variableAccess, true), "++", true);
		testOp(variableAccess -> new OpInc(variableAccess, false), "++", false);
	}

	@Test
	void testParsingIncrementPrefix() {
		final OpInc expected = new OpInc(VARIABLE_0_ACCESS, false);
		final ClslCompilationEnv env = new ClslCompilationEnv("++" + VARIABLE_0 + ';');
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	// FIXME: add more complex tests for `Set`s

	@Test
	void testParsingSet() {
		testBiOp(SetVar::new, "=");
	}

	@Test
	void testParsingSetAddition() {
		testBiOp(SetVarAdd::new, "+=");
	}

	@Test
	void testParsingSetBinaryAnd() {
		testBiOp(SetVarBinAnd::new, "&=");
	}

	@Test
	void testParsingSetBinaryOr() {
		testBiOp(SetVarBinOr::new, "|=");
	}

	@Test
	void testParsingSetDivision() {
		testBiOp(SetVarDiv::new, "/=");
	}

	@Test
	void testParsingSetMultiplication() {
		testBiOp(SetVarMul::new, "*=");
	}

	@Test
	void testParsingSetModulus() {
		testBiOp(SetVarModulus::new, "%=");
	}

	@Test
	void testParsingSetShiftLeft() {
		testBiOp(SetVarShiftLeft::new, "<<=");
	}

	@Test
	void testParsingSetShiftRight() {
		testBiOp(SetVarShiftRight::new, ">>=");
	}

	@Test
	void testParsingSetSubtraction() {
		testBiOp(SetVarSub::new, "-=");
	}

	@Test
	void testParsingSetXor() {
		testBiOp(SetVarXor::new, "^=");
	}
}