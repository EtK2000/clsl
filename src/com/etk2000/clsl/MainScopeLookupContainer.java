package com.etk2000.clsl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@SuppressWarnings("serial")
public class MainScopeLookupContainer extends ArrayList<FunctionLookupTable> {
	private static class MainScopeLoopContainerImmutable extends MainScopeLookupContainer {
		MainScopeLoopContainerImmutable(MainScopeLookupContainer from) {
			super(from);
		}

		@Override
		public boolean add(FunctionLookupTable element) {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public void add(int index, FunctionLookupTable element) {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public boolean addAll(Collection<? extends FunctionLookupTable> c) {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public boolean addAll(int index, Collection<? extends FunctionLookupTable> c) {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public void clear() {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public void empty(boolean removeHeaders) {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public void ensureCapacity(int additionalCapacity) {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public void put(String name, FunctionalChunk func) {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public FunctionLookupTable remove(int index) {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public boolean remove(Object o) {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public boolean removeIf(Predicate<? super FunctionLookupTable> filter) {
			throw new IllegalAccessError("immutable");
		}

		@Override
		protected void removeRange(int fromIndex, int toIndex) {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public void removeSubTables() {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public void replaceAll(UnaryOperator<FunctionLookupTable> operator) {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public FunctionLookupTable set(int index, FunctionLookupTable element) {
			throw new IllegalAccessError("immutable");
		}

		@Override
		public void sort(Comparator<? super FunctionLookupTable> c) {
			throw new IllegalAccessError("immutable");
		}
	}

	private final MainScopeLookupTable programScope = new MainScopeLookupTable();

	public MainScopeLookupContainer() {
		super(1);
		add(programScope);
	}

	protected MainScopeLookupContainer(MainScopeLookupContainer from) {
		super(from);
	}

	public MainScopeLookupContainer immutableCopy() {
		return new MainScopeLoopContainerImmutable(this);
	}

	public void empty(boolean removeHeaders) {
		programScope.clear();
		if (removeHeaders)
			removeSubTables();
	}

	public FunctionalChunk lookup(String name) {
		FunctionalChunk res;
		for (FunctionLookupTable flt : this) {
			if ((res = flt.lookup(name)) != null)
				return res;
		}
		return null;//throw new CLSL_RuntimeException("undefined reference to '" + name + '\'');
	}

	public void put(String name, FunctionalChunk func) {
		programScope.put(name, func);
	}

	public void removeSubTables() {
		removeRange(1, size());
	}
}