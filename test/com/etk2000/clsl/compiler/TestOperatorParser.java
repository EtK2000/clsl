package com.etk2000.clsl.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.etk2000.clsl.chunk.FunctionCallChunk;
import com.etk2000.clsl.chunk.op.OpDec;
import com.etk2000.clsl.chunk.op.OpInc;
import com.etk2000.clsl.chunk.variable.GetVar;
import com.etk2000.clsl.chunk.variable.set.SetVar;
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

public class TestOperatorParser {
	private static final String VARIABLE_0 = "test", VARIABLE_1 = "another";

	@Test
	void testParsingDecrementPostfix() {
		final OpDec expected = new OpDec(VARIABLE_0, true);
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + "--;");
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	@Test
	void testParsingDecrementPrefix() {
		final OpDec expected = new OpDec(VARIABLE_0, false);
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
		final OpInc expected = new OpInc(VARIABLE_0, true);
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + "++;");
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	@Test
	void testParsingIncrementPrefix() {
		final OpInc expected = new OpInc(VARIABLE_0, false);
		final ClslCompilationEnv env = new ClslCompilationEnv("++" + VARIABLE_0 + ';');
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	// FIXME: add more complex tests for `Set`s

	@Test
	void testParsingSet() {
		final SetVar expected = new SetVar(VARIABLE_0, new GetVar(VARIABLE_1));
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + '=' + VARIABLE_1 + ';');
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	@Test
	void testParsingSetAddition() {
		final SetVarAdd expected = new SetVarAdd(VARIABLE_0, new GetVar(VARIABLE_1));
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + "+=" + VARIABLE_1 + ';');
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	@Test
	void testParsingSetBinaryAnd() {
		final SetVarBinAnd expected = new SetVarBinAnd(VARIABLE_0, new GetVar(VARIABLE_1));
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + "&=" + VARIABLE_1 + ';');
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	@Test
	void testParsingSetBinaryOr() {
		final SetVarBinOr expected = new SetVarBinOr(VARIABLE_0, new GetVar(VARIABLE_1));
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + "|=" + VARIABLE_1 + ';');
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	@Test
	void testParsingSetDivision() {
		final SetVarDiv expected = new SetVarDiv(VARIABLE_0, new GetVar(VARIABLE_1));
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + "/=" + VARIABLE_1 + ';');
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	@Test
	void testParsingSetMultiplication() {
		final SetVarMul expected = new SetVarMul(VARIABLE_0, new GetVar(VARIABLE_1));
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + "*=" + VARIABLE_1 + ';');
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	@Test
	void testParsingSetModulus() {
		final SetVarModulus expected = new SetVarModulus(VARIABLE_0, new GetVar(VARIABLE_1));
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + "%=" + VARIABLE_1 + ';');
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	@Test
	void testParsingSetShiftLeft() {
		final SetVarShiftLeft expected = new SetVarShiftLeft(VARIABLE_0, new GetVar(VARIABLE_1));
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + "<<=" + VARIABLE_1 + ';');
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	@Test
	void testParsingSetShiftRight() {
		final SetVarShiftRight expected = new SetVarShiftRight(VARIABLE_0, new GetVar(VARIABLE_1));
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + ">>=" + VARIABLE_1 + ';');
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	@Test
	void testParsingSetSubtraction() {
		final SetVarSub expected = new SetVarSub(VARIABLE_0, new GetVar(VARIABLE_1));
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + "-=" + VARIABLE_1 + ';');
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}

	@Test
	void testParsingSetXor() {
		final SetVarXor expected = new SetVarXor(VARIABLE_0, new GetVar(VARIABLE_1));
		final ClslCompilationEnv env = new ClslCompilationEnv(VARIABLE_0 + "^=" + VARIABLE_1 + ';');
		assertTrue(env.matcher.find());

		OperatorParser.parseNext(env);
		assertEquals(1, env.exec.size());
		assertEquals(expected, env.exec.get(0));
	}
}