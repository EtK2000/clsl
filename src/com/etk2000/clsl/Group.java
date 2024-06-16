package com.etk2000.clsl;

import java.util.Arrays;
import java.util.Objects;

// TODO: is Comparable needed?
public class Group<A, B> implements Comparable<Group<A, B>> {
	private static boolean areEqual(Object a, Object b) {
		final Class<?> clazz = a.getClass();

		if (clazz != b.getClass())
			return false;
		if (clazz.isArray())
			return Arrays.equals((Object[]) a, (Object[]) b);
		return Objects.equals(a, b);
	}

	public A a;
	public B b;

	public Group(A a, B b) {
		this.a = a;
		this.b = b;
	}


	@Override
	public String toString() {
		return a + " <-> " + b;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Group<A, B> other) {
		if (a instanceof Comparable<?>) {
			int res = ((Comparable<A>) a).compareTo(other.a);
			if (res == 0 && b instanceof Comparable<?>)
				return ((Comparable<B>) b).compareTo(other.b);
			return res;
		}
		else if (b instanceof Comparable<?>)
			return ((Comparable<B>) b).compareTo(other.b);
		return 0;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;

		final Group<?, ?> that = (Group<?, ?>) other;
		return areEqual(a, that.a) && areEqual(b, that.b);
	}
}