package com.etk2000.clsl.exception;

import com.etk2000.clsl.compiler.ClslCompilationEnv;

import java.util.regex.Matcher;

public class ClslCompilerException extends ClslException {
	private static int findNextRelevantMatch(Matcher m) {
		try {
			int start = m.start(), i = start;
			while (m.find()) {
				i = m.start();
				switch (m.group()) {
					case ")":
					case "}":
						return i + 1;
				}
			}
			return i + 1;
		}
		catch (IllegalStateException e) {
			return -1;// FIXME: avoid having to throw IllegalStateException
		}
	}

	private static int findPrevRelevantMatch(Matcher m) {
		try {
			int start = m.start(), i, j = start;
			try {
				m.reset();
				while (m.find() && (i = m.start()) < start) {
					switch (m.group()) {
						case "(":
						case "{":
							j = i;
					}
				}
				return j;
			}
			finally {
				m.find(start);
			}
		}
		catch (IllegalStateException e) {
			return -1;// FIXME: avoid having to throw IllegalStateException
		}
	}

	public ClslCompilerException(String msg) {
		super(msg);
		setStackTrace(new StackTraceElement[0]);
	}

	public ClslCompilerException(String msg, Exception cause) {
		super(msg, cause);
		setStackTrace(new StackTraceElement[0]);
	}

	// use this constructor to show the code nearby
	public ClslCompilerException(ClslCompilationEnv env, String msg) {
		this(msg + "; near: " + env.source.substring(Math.max(0, Math.min(env.indexInSource, findPrevRelevantMatch(env.matcher))), Math.max(0, findNextRelevantMatch(env.matcher))));
	}
}