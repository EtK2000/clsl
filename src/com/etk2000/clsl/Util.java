package com.etk2000.clsl;

class Util {
	static final ExecutableChunk[] CHUNK_EXEC = {};
	static final ValueChunk[] CHUNK_VALUE = {};
	static final CLSLValue[] VALUE = {};

	@SafeVarargs
	static <T> T[] array(T... args) {
		return args;
	}

	static <T> boolean contains(T[] arr, T val, boolean identity) {
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
	static String escape(String str) {
		try (StringBuilderPoolable sb = new StringBuilderPoolable(str.length())) {
			char c;
			for (int i = 0; i < str.length(); ++i) {
				switch (c = str.charAt(i)) {
					case '\\':
						sb.append("\\\\");
						break;
					case '\b':
						sb.append("\\b");
						break;
					case '\f':
						sb.append("\\f");
						break;
					case '\n':
						sb.append("\\n");
						break;
					case '\r':
						sb.append("\\r");
						break;
					case '\t':
						sb.append("\\t");
						break;
					case '\"':
						sb.append("\\\"");
						break;
					case '\'':
						sb.append("\\\'");
						break;
					default:
						sb.append(c);
						break;
				}
			}

			return sb.toString();
		}
	}

	// TODO: deal with " and ' potentially not requiring escaping
	static String unescape(String str) {
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

	static int nextPowerOfTwo(int value) {
		if (value == 0)
			return 1;
		--value;
		value |= value >> 1;
		value |= value >> 2;
		value |= value >> 4;
		value |= value >> 8;
		value |= value >> 16;
		return value + 1;
	}
}