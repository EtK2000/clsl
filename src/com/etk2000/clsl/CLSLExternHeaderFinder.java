package com.etk2000.clsl;

// a program can create its own ExternHeaderFinder to include headers from
// memory, code, or literally anywhere else
public interface CLSLExternHeaderFinder {
	CLSLCode find(String header) throws CLSL_CompilerException;
}