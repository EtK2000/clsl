package com.etk2000.clsl.compiler;

import com.etk2000.clsl.chunk.ExecutableChunk;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

class ClslCompilationEnv {
	final List<ExecutableChunk> exec = new ArrayList<>();
	final Matcher matcher;
	final String source;
	int indexInSource;

	ClslCompilationEnv(String source) {
		this.matcher = ClslCompiler.SYNTAX_PATTERN.matcher(source);
		this.source = source;
	}
}