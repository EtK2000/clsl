package com.etk2000.clsl.exception;

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
	public ClslCompilerException(String msg, int i, String res, Matcher m) {
		this(msg + "; near: " + res.substring(Math.max(0, Math.min(i, findPrevRelevantMatch(m))), Math.max(0, findNextRelevantMatch(m))));
	}
}