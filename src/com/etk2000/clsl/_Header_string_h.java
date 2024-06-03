package com.etk2000.clsl;

import com.etk2000.clsl.exception.function.ClslIncompatibleArgumentTypeException;
import com.etk2000.clsl.exception.function.ClslInvalidNumberOfArgumentsException;

public class _Header_string_h extends _HeaderBase {
	private static final FunctionalChunk strcat = CLSL.createFunctionalChunk(ValueType.ARRAY, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		if (!CLSL.isString(args[0]))
			throw new ClslIncompatibleArgumentTypeException(0, "strcat");
		if (!CLSL.isString(args[1]))
			throw new ClslIncompatibleArgumentTypeException(1, "strcat");

		return ((CLSLArray) args[0]).append((CLSLArray) args[1]);
	});

	private static final FunctionalChunk strcmp = CLSL.createFunctionalChunk(ValueType.INT, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		if (!CLSL.isString(args[0]))
			throw new ClslIncompatibleArgumentTypeException(0, "strcmp");
		if (!CLSL.isString(args[1]))
			throw new ClslIncompatibleArgumentTypeException(1, "strcmp");

		return new CLSLIntConst(((CLSLArray) args[0]).compareTo((CLSLArray) args[1]));
	});

	private static final FunctionalChunk strcpy = CLSL.createFunctionalChunk(ValueType.ARRAY, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		if (!CLSL.isString(args[0]))
			throw new ClslIncompatibleArgumentTypeException(0, "strcpy");
		if (!CLSL.isString(args[1]))
			throw new ClslIncompatibleArgumentTypeException(1, "strcpy");

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