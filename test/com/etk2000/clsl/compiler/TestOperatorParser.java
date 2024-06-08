package com.etk2000.clsl.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.etk2000.clsl.Group;
import com.etk2000.clsl.chunk.FunctionCallChunk;
import com.etk2000.clsl.chunk.VariableAccess;
import com.etk2000.clsl.chunk.op.OpDec;
import com.etk2000.clsl.chunk.op.OpInc;
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

public class TestOperatorParser {
	private static final String VARIABLE_0 = "test", VARIABLE_1 = "another",
			VARIABLE_0_MEMBER = "something", VARIABLE_1_MEMBER = "thing";
	private static final GetVar VARIABLE_0_ACCESS = new GetVar(VARIABLE_0), VARIABLE_1_ACCESS = new GetVar(VARIABLE_1);
	private static final OpMember VARIABLE_0_MEMBER_ACCESS = new OpMember(new GetVar(VARIABLE_0), VARIABLE_0_MEMBER),
			VARIABLE_1_MEMBER_ACCESS = new OpMember(new GetVar(VARIABLE_1), VARIABLE_1_MEMBER);


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

		// a.b, x.y
		toTest.add(new Group<>(
				newInstance.apply(VARIABLE_0_MEMBER_ACCESS, VARIABLE_1_MEMBER_ACCESS),
				VARIABLE_0 + '.' + VARIABLE_0_MEMBER + operator + VARIABLE_1 + '.' + VARIABLE_1_MEMBER
		));

		// FIXME: add tests for INDEX access

		for (Group<SetVarAbstract, String> currentTest : toTest) {
			final SetVarAbstract expected = currentTest.a;
			final ClslCompilationEnv env = new ClslCompilationEnv(currentTest.b + ';');
			assertTrue(env.matcher.find());

			OperatorParser.parseNext(env);
			assertEquals(1, env.exec.size(), '"' + currentTest.b + '"');
			assertEquals(expected, env.exec.get(0), '"' + currentTest.b + '"');
		}
	}

	@Test
	void testParsingDecrementPostfix() {
		final OpDec expected = new OpDec(VARIABLE_0_ACCESS, true);
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + "--;");
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	@Test
	void testParsingDecrementPrefix() {
		final OpDec expected = new OpDec(VARIABLE_0_ACCESS, false);
		final ClslCompilationEnv env = new ClslCompilationEnv("--" + VARIABLE_0 + ';');
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	@Test
	void testParsingFunctionCall() {
		final FunctionCallChunk expected = new FunctionCallChunk(new GetVar(VARIABLE_0));
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + "();");
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	// FIXME: testParsingFunctionCallWithArguments


	@Test
	void testParsingIncrementPostfix() {
		final OpInc expected = new OpInc(VARIABLE_0_ACCESS, true);
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + "++;");
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
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