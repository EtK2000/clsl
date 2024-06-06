package com.etk2000.clsl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.etk2000.clsl.chunk.FunctionalChunk;

import org.junit.jupiter.api.Test;

public class TestMainScopeLookupTable {
	private static final String KEY_0 = "test", KEY_1 = "anotherKey";

	private static final FunctionalChunk VALUE_0 = createFunctionalChunk(KEY_0),
			VALUE_1 = createFunctionalChunk(KEY_1);

	private static FunctionalChunk createFunctionalChunk(String name) {
		return new FunctionalChunk(name, ValueType.VOID, null, null);
	}

	@Test
	void testClear() {
		final MainScopeLookupTable lookupTable = new MainScopeLookupTable();

		assertEquals(0, lookupTable.funcs.size());
		lookupTable.funcs.put(KEY_0, VALUE_0);
		lookupTable.funcs.put(KEY_1, VALUE_1);
		assertEquals(2, lookupTable.funcs.size());

		lookupTable.clear();
		assertEquals(0, lookupTable.funcs.size());
	}

	@Test
	void testLookup() {
		final MainScopeLookupTable lookupTable = new MainScopeLookupTable();

		assertEquals(0, lookupTable.funcs.size());
		lookupTable.funcs.put(KEY_0, VALUE_0);
		lookupTable.funcs.put(KEY_1, VALUE_1);
		assertEquals(2, lookupTable.funcs.size());

		assertEquals(VALUE_0, lookupTable.lookup(KEY_0));
		assertEquals(VALUE_1, lookupTable.lookup(KEY_1));
	}

	@Test
	void testPut() {
		final MainScopeLookupTable lookupTable = new MainScopeLookupTable();

		assertEquals(0, lookupTable.funcs.size());

		lookupTable.put(KEY_0, VALUE_0);
		assertEquals(1, lookupTable.funcs.size());
		assertEquals(VALUE_0, lookupTable.funcs.get(KEY_0));

		lookupTable.put(KEY_1, VALUE_1);
		assertEquals(2, lookupTable.funcs.size());
		assertEquals(VALUE_0, lookupTable.funcs.get(KEY_0));
		assertEquals(VALUE_1, lookupTable.funcs.get(KEY_1));
	}
}