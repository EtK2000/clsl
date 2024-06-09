package com.etk2000.clsl.compiler;

import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.VariableAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class ClslCompilationEnv {
	public final List<ExecutableChunk> exec = new ArrayList<>();
	public final Matcher matcher;
	public final String source;
	public int indexInSource;
	VariableAccess currentValueAccess;

	ClslCompilationEnv(String source) {
		this.matcher = ClslCompiler.SYNTAX_PATTERN.matcher(source);
		this.source = source;
	}

	VariableAccess popCurrentValueAccess() {
		final VariableAccess res = currentValueAccess;
		currentValueAccess = null;
		return res;
	}
}