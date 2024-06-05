package com.etk2000.clsl;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {
	R apply(T var1) throws E;
}