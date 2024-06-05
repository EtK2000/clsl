package com.etk2000.clsl.header;

import com.etk2000.clsl.ClslCode;
import com.etk2000.clsl.exception.ClslRuntimeException;

// a program can create its own ExternHeaderFinder to include headers from
// memory, code, or literally anywhere else
@FunctionalInterface
public interface ClslExternHeaderFinder {
	ClslCode find(String header) throws ClslRuntimeException;
}