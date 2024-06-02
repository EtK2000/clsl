package com.etk2000.clsl;

class IntGroup<A> {
	public static <T> IntGroup<T> make(T a, int b) {
		return new IntGroup<>(a, b);
	}

	public final A a;
	public final int b;

	private IntGroup(A a, int b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public String toString() {
		return a + " <-> " + b;
	}
}