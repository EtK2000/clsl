package com.etk2000.clsl.stack;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.etk2000.clsl.exception.ClslStackUnderflowException;
import com.etk2000.clsl.exception.variable.ClslVariableRedefinitionException;
import com.etk2000.clsl.value.ClslIntConst;
import com.etk2000.clsl.value.ClslStructConstMapped;
import com.etk2000.clsl.value.ClslValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class TestDequeStack {
	private static final String VARIABLE_NAME = "test";
	final DequeStack stack = new DequeStack();

	@BeforeEach
	void setup() {
		stack.wipe();
	}

	@Test
	void testFrames() {
		final ClslStructConstMapped expected = new ClslStructConstMapped(new HashMap<>());

		assertNull(stack.getVar(VARIABLE_NAME));

		stack.defineVar(VARIABLE_NAME, expected);
		assertSame(expected, stack.getVar(VARIABLE_NAME));

		assertEquals(1, stack.frames().size());
		final Map<String, ClslValue> vars = stack.frames().iterator().next().getVars();
		assertEquals(1, vars.size());
		assertSame(expected, vars.get(VARIABLE_NAME));

		stack.push(false);
		{
			assertEquals(1, stack.frames().size());
			stack.push(true);
			{
				assertEquals(2, stack.frames().size());
				assertNotSame(
						vars,
						stack.frames().iterator().next().getVars()
				);
			}
			stack.pop(true);
			assertEquals(1, stack.frames().size());
		}
		stack.pop(false);
		assertEquals(1, stack.frames().size());
	}

	@Test
	void testGlobalScope() {
		final ClslStructConstMapped expected = new ClslStructConstMapped(new HashMap<>());

		assertNull(stack.getVar(VARIABLE_NAME));

		stack.defineVar(VARIABLE_NAME, expected);
		assertSame(expected, stack.getVar(VARIABLE_NAME));

		stack.push(false);
		{
			assertSame(expected, stack.getVar(VARIABLE_NAME));

			stack.push(true);
			{
				assertSame(expected, stack.getVar(VARIABLE_NAME));

				// test overriding in current scope
				final ClslIntConst override = ClslIntConst.of(1);
				stack.defineVar(VARIABLE_NAME, override);
				assertSame(override, stack.getVar(VARIABLE_NAME));

				stack.push(false);
				{
					assertSame(override, stack.getVar(VARIABLE_NAME));

					// test a new scope where the local is no longer visible
					stack.push(true);
					{
						assertSame(expected, stack.getVar(VARIABLE_NAME));
					}
					stack.pop(true);
				}
				stack.pop(false);
				assertSame(override, stack.getVar(VARIABLE_NAME));
			}
			stack.pop(true);
			assertSame(expected, stack.getVar(VARIABLE_NAME));
		}
		stack.pop(false);
		assertSame(expected, stack.getVar(VARIABLE_NAME));
	}

	@Test
	void testLocalScope() {
		final ClslStructConstMapped expected = new ClslStructConstMapped(new HashMap<>());
		stack.push(true);

		assertNull(stack.getVar(VARIABLE_NAME));

		stack.defineVar(VARIABLE_NAME, expected);
		assertSame(expected, stack.getVar(VARIABLE_NAME));

		stack.push(false);
		{
			assertSame(expected, stack.getVar(VARIABLE_NAME));

			stack.push(true);
			{
				assertNull(stack.getVar(VARIABLE_NAME));
			}
		}
	}

	@Test
	void testPop() {
		assertEquals(1, stack.frames().size());

		stack.push(false);
		{
			assertEquals(1, stack.frames().size());

			stack.push(true);
			{
				assertEquals(2, stack.frames().size());

				stack.push(false);
				{
					assertEquals(2, stack.frames().size());
				}
			}
			stack.pop(true);
			assertEquals(1, stack.frames().size());
		}
		stack.pop(false);
		assertEquals(1, stack.frames().size());
	}

	@Test
	void testPush() {
		assertEquals(1, stack.frames().size());

		stack.push(false);
		{
			assertEquals(1, stack.frames().size());

			stack.push(true);
			{
				assertEquals(2, stack.frames().size());
			}
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testStackUnderflow(boolean full) {
		assertThrows(ClslStackUnderflowException.class, () -> stack.pop(full));
	}

	@Test
	void testVariableRedefinition() {
		final ClslStructConstMapped expected = new ClslStructConstMapped(new HashMap<>());
		final ClslIntConst override = ClslIntConst.of(1);

		assertNull(stack.getVar(VARIABLE_NAME));

		stack.defineVar(VARIABLE_NAME, expected);
		assertSame(expected, stack.getVar(VARIABLE_NAME));
		assertThrows(ClslVariableRedefinitionException.class, () -> stack.defineVar(VARIABLE_NAME, override));

		stack.push(false);
		{
			assertThrows(ClslVariableRedefinitionException.class, () -> stack.defineVar(VARIABLE_NAME, override));
			stack.push(true);
			{
				assertDoesNotThrow(() -> stack.defineVar(VARIABLE_NAME, override));
			}
		}
	}

	@Test
	void testWipe() {
		final ClslStructConstMapped expected = new ClslStructConstMapped(new HashMap<>());

		assertNull(stack.getVar(VARIABLE_NAME));

		stack.defineVar(VARIABLE_NAME, expected);
		assertSame(expected, stack.getVar(VARIABLE_NAME));

		stack.push(false);
		{
			stack.push(true);
		}
		stack.wipe();
		assertEquals(1, stack.frames().size());
		assertNull(stack.getVar(VARIABLE_NAME));
	}
}