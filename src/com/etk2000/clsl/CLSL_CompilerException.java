package com.etk2000.clsl;

import java.util.regex.Matcher;

@SuppressWarnings("serial")
public class CLSL_CompilerException extends CLSL_Exception {
	private static int findNextReleventMatch(Matcher m) {
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

	private static int findPrevReleventMatch(Matcher m) {
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

	CLSL_CompilerException(String msg) {
		super(msg);
		setStackTrace(new StackTraceElement[0]);
	}

	// use this constructor to show the code nearby
	CLSL_CompilerException(String msg, int i, String res, Matcher m) {
		this(msg + "; near: " + res.substring(Math.max(0, Math.min(i, findPrevReleventMatch(m))), Math.max(0, findNextReleventMatch(m))));
	}

	@Override
	public String toString() {
		return "CLSL compile error: " + getMessage();
	}
}