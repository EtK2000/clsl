package com.etk2000.clsl.header;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.chunk.FunctionalChunk;
import com.etk2000.clsl.chunk.ReturnChunk;
import com.etk2000.clsl.exception.function.ClslIncompatibleArgumentTypeException;
import com.etk2000.clsl.exception.function.ClslInvalidNumberOfArgumentsException;
import com.etk2000.clsl.value.ClslArray;
import com.etk2000.clsl.value.ClslInt;
import com.etk2000.clsl.value.ClslIntConst;

public class StringH extends HeaderBase {
	public static final StringH INSTANCE = new StringH();

	private static final FunctionalChunk strcat = Clsl.createFunctionalChunk(ValueType.ARRAY, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		if (!Clsl.isString(args[0]))
			throw new ClslIncompatibleArgumentTypeException(0, "strcat");
		if (!Clsl.isString(args[1]))
			throw new ClslIncompatibleArgumentTypeException(1, "strcat");

		return ((ClslArray) args[0]).append((ClslArray) args[1]);
	});

	private static final FunctionalChunk strcmp = Clsl.createFunctionalChunk(ValueType.INT, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		if (!Clsl.isString(args[0]))
			throw new ClslIncompatibleArgumentTypeException(0, "strcmp");
		if (!Clsl.isString(args[1]))
			throw new ClslIncompatibleArgumentTypeException(1, "strcmp");

		return new ClslIntConst(((ClslArray) args[0]).compareTo((ClslArray) args[1]));
	});

	private static final FunctionalChunk strcpy = Clsl.createFunctionalChunk(ValueType.ARRAY, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		if (!Clsl.isString(args[0]))
			throw new ClslIncompatibleArgumentTypeException(0, "strcpy");
		if (!Clsl.isString(args[1]))
			throw new ClslIncompatibleArgumentTypeException(1, "strcpy");

		return ((ClslArray) args[0]).fill((ClslArray) args[1]);
	});

	private static final FunctionalChunk strlen = Clsl.createFunctionalChunk(ValueType.INT, (env, args) -> new ClslInt(assertString(args, "strlen").strlen()));

	private StringH() {
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		env.defineVar("strcat", strcat.access());
		env.defineVar("strcmp", strcmp.access());
		env.defineVar("strcpy", strcpy.access());
		env.defineVar("strlen", strlen.access());
		return null;
	}
}