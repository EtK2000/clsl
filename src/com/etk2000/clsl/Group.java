package com.etk2000.clsl;

// TODO: is Comparable needed?
public class Group<A, B> implements Comparable<Group<A, B>> {
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
}