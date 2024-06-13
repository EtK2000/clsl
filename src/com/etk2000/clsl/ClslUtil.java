package com.etk2000.clsl;

import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;

import java.util.Arrays;

public class ClslUtil {
	private static final String[] INVALID_VAR_NAMES = {
			"break", "case", "char", "const", "continue", "do", "else", "enum", "float",
			"for", "goto", "if", "int", "NULL", "return", "short", "sizeof", "static",
			"struct", "switch", "typedef", "unsigned", "void", "while"
	};

	public static final ExecutableChunk[] CHUNK_EXEC = {};
	public static final ValueChunk[] CHUNK_VALUE = {};

	@SafeVarargs
	public static <T> T[] array(T... args) {
		return args;
	}

	public static <T> boolean contains(T[] arr, T val, boolean identity) {
		int i = arr.length - 1;
		if (identity || val == null) {
			while (i >= 0) {
				if (arr[i--] == val)
					return true;
			}
		}
		else {
			while (i >= 0) {
				if (val.equals(arr[i--]))
					return true;
			}
		}
		return false;
	}

	// TODO: deal with octal and unicode?
	public static String escape(char c) {
		switch (c) {
			case '\\':
				return "\\\\";
			case '\b':
				return "\\b";
			case '\f':
				return "\\f";
			case '\n':
				return "\\n";
			case '\r':
				return "\\r";
			case '\t':
				return "\\t";
			case '\"':
				return "\\\"";
			case '\'':
				return "\\'";
			default:
				return Character.toString(c);
		}
	}

	public static String escape(String str) {
		try (StringBuilderPoolable sb = new StringBuilderPoolable(str.length())) {
			for (int i = 0; i < str.length(); ++i)
				sb.append(escape(str.charAt(i)));
			return sb.toString();
		}
	}

	/**
	 * Returns true for the following inputs:
	 * ¢ (162)
	 * £ (163)
	 * ¤ (164)
	 * ¥ (165)
	 */
	public static boolean isNonDollarCurrencySymbol(char c) {
		return c >= 162 && c <= 165;
	}

	public static boolean isValidId(String str) {
		if (str.isEmpty())
			return false;

		final char first = str.charAt(0);
		if (!Character.isJavaIdentifierStart(first) || isNonDollarCurrencySymbol(first))
			return false;

		for (short i = 1; i < str.length(); ++i) {
			final char c = str.charAt(i);
			if (!Character.isJavaIdentifierPart(c) || Character.isIdentifierIgnorable(c) || isNonDollarCurrencySymbol(c))
				return false;
		}

		return Arrays.binarySearch(INVALID_VAR_NAMES, str) < 0;
	}

	// TODO: deal with " and ' potentially not requiring escaping
	public static String unescape(String str) {
		try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
			char c, n;
			for (int i = 0; i < str.length(); ++i) {
				if ((c = str.charAt(i)) == '\\') {
					n = i == str.length() - 1 ? '\\' : str.charAt(++i);

					// octal sequence
					if (n >= '0' && n <= '7') {
						boolean hasTwo = i < str.length() - 1 && (c = str.charAt(i + 1)) >= '0' && c <= '7';

						try (StringBuilderPoolable oct = new StringBuilderPoolable()) {
							oct.append(n);
							if (hasTwo)
								oct.append(c);
							sb.append((char) Integer.parseInt(oct.toString(), 8));
						}

						if (hasTwo)
							++i;
						continue;
					}

					// anything else
					switch (n) {
						case '\\':
							c = '\\';
							break;
						case 'b':
							c = '\b';
							break;
						case 'f':
							c = '\f';
							break;
						case 'n':
							c = '\n';
							break;
						case 'r':
							c = '\r';
							break;
						case 't':
							c = '\t';
							break;
						case '"':
							c = '"';
							break;
						case '\'':
							c = '\'';
							break;

						// hex
						case 'u':
							if (i >= str.length() - 5) {
								c = 'u';
								break;
							}
							sb.append(Character.toChars(Integer.parseInt(str.substring(i + 2, i + 6), 16)));
							i += 4;
							continue;
					}
				}
				sb.append(c);
			}

			return sb.toString();
		}
	}

	private ClslUtil() {
	}
}