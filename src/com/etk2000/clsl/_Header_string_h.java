package com.etk2000.clsl;

public class _Header_string_h extends _HeaderBase {
	private static final FunctionalChunk strcat = CLSL.createFunctionalChunk(ValueType.ARRAY, (env, args) -> {
		if (args.length != 2)
			throw new CLSL_RuntimeException("invalid number of arguments, got " + args.length + " expected 2");
		if (!CLSL.isString(args[0]))
			throw new CLSL_RuntimeException("incompatible type for argument 0 of `strcat`");
		if (!CLSL.isString(args[1]))
			throw new CLSL_RuntimeException("incompatible type for argument 1 of `strcat`");

		return ((CLSLArray) args[0]).append((CLSLArray) args[1]);
	});

	private static final FunctionalChunk strcmp = CLSL.createFunctionalChunk(ValueType.INT, (env, args) -> {
		if (args.length != 2)
			throw new CLSL_RuntimeException("invalid number of arguments, got " + args.length + " expected 2");
		if (!CLSL.isString(args[0]))
			throw new CLSL_RuntimeException("incompatible type for argument 0 of `strcmp`");
		if (!CLSL.isString(args[1]))
			throw new CLSL_RuntimeException("incompatible type for argument 1 of `strcmp`");

		return new CLSLIntConst(((CLSLArray) args[0]).compareTo((CLSLArray) args[1]));
	});

	private static final FunctionalChunk strcpy = CLSL.createFunctionalChunk(ValueType.ARRAY, (env, args) -> {
		if (args.length != 2)
			throw new CLSL_RuntimeException("invalid number of arguments, got " + args.length + " expected 2");
		if (!CLSL.isString(args[0]))
			throw new CLSL_RuntimeException("incompatible type for argument 0 of `strcpy`");
		if (!CLSL.isString(args[1]))
			throw new CLSL_RuntimeException("incompatible type for argument 1 of `strcpy`");

		return ((CLSLArray) args[0]).fill((CLSLArray) args[1]);
	});

	private static final FunctionalChunk strlen = CLSL.createFunctionalChunk(ValueType.INT, (env, args) -> new CLSLInt(assertString(args, "strlen").strlen()));

	@Override
	public FunctionalChunk lookup(String functionName) {
		switch (functionName) {
			case "strcat":
				return strcat;
			case "strcmp":
				return strcmp;
			case "strcpy":
				return strcpy;
			case "strlen":
				return strlen;
		}
		return null;
	}
}