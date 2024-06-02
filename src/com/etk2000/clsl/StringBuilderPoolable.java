package com.etk2000.clsl;

import java.util.concurrent.ConcurrentLinkedQueue;

class StringBuilderPoolable implements AutoCloseable {
	// I'm not abstracting away the types so I remember they're concurrent
	private static final ConcurrentLinkedQueue<StringBuilder> small = new ConcurrentLinkedQueue<>(), large = new ConcurrentLinkedQueue<>();
	static final short MAX_SMALL = 512, DEFAULT_SIZE = 64;

	// LOW: maybe if we have multiple options look for the smallest one that
	// can accommodate us
	static StringBuilder get(int minSize) {
		StringBuilder res = (minSize > MAX_SMALL ? large : small).poll();
		if (res == null)
			res = new StringBuilder();

		res.ensureCapacity(minSize);
		return res;
	}

	static void free(StringBuilder sb) {
		sb.setLength(0);
		(sb.capacity() > MAX_SMALL ? large : small).offer(sb);
	}

	private final StringBuilder sb;

	StringBuilderPoolable() {
		sb = get(DEFAULT_SIZE);
	}

	StringBuilderPoolable(int minSize) {
		sb = get(minSize);
	}

	@Override
	public void close() {
		free(sb);
	}

	public StringBuilderPoolable append(char c) {
		sb.append(c);
		return this;
	}

	public StringBuilderPoolable append(char[] s) {
		sb.append(s);
		return this;
	}

	public StringBuilderPoolable append(String s) {
		sb.append(s);
		return this;
	}

	public StringBuilderPoolable append(Object o) {
		sb.append(o);
		return this;
	}

	public StringBuilderPoolable deleteLast() {
		sb.deleteCharAt(sb.length() - 1);
		return this;
	}

	public StringBuilderPoolable deleteLast(int n) {
		sb.setLength(sb.length() - n);
		return this;
	}

	public int length() {
		return sb.length();
	}

	public void setLength(int len) {
		sb.setLength(0);
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}