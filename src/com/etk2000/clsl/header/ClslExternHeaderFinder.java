package com.etk2000.clsl.header;

import com.etk2000.clsl.ClslCode;
import com.etk2000.clsl.exception.ClslCompilerException;

// a program can create its own ExternHeaderFinder to include headers from
// memory, code, or literally anywhere else
public interface ClslExternHeaderFinder {
	ClslCode find(String header) throws ClslCompilerException;
}