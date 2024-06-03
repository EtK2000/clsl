package com.etk2000.clsl;

import com.etk2000.clsl.exception.function.ClslIncompatibleArgumentTypeException;
import com.etk2000.clsl.exception.function.ClslInvalidNumberOfArgumentsException;

// FIXME: implement this:
// double frexp(double x, int *exponent)
public class _Header_math_h extends _HeaderBase {
	private static final FunctionalChunk acos = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.acos(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk asin = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.asin(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk atan = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.atan(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk atan2 = CLSL.createFunctionalChunk(ValueType.DOUBLE, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		return new CLSLDoubleConst(Math.atan2(args[0].toDouble(), args[1].toDouble()));
	});

	private static final FunctionalChunk ceil = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.ceil(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk cos = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.cos(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk cosh = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.cosh(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk exp = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.exp(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk fabs = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.abs(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk floor = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.floor(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk fmod = CLSL.createFunctionalChunk(ValueType.DOUBLE, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		return new CLSLDoubleConst(args[0].toDouble() % args[1].toDouble());
	});

	// LOW: see if we can optimize this with a single Math call
	private static final FunctionalChunk ldexp = CLSL.createFunctionalChunk(ValueType.DOUBLE, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		return new CLSLDoubleConst(args[0].toDouble() * Math.pow(2, args[1].toDouble()));
	});

	private static final FunctionalChunk log = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.log(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk log10 = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.log10(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk modf = CLSL.createFunctionalChunk(ValueType.DOUBLE, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		if (!(args[1] instanceof CLSLArray))
			throw new ClslIncompatibleArgumentTypeException(1, "modf");

		// LOW: add a set function that doesn't require wrapping in
		// CLSLValue
		long integer = args[0].toLong();
		((CLSLArray) args[1]).val[0].set(new CLSLDoubleConst(integer));
		return new CLSLDoubleConst(args[0].toDouble() - integer);
	});

	private static final FunctionalChunk pow = CLSL.createFunctionalChunk(ValueType.DOUBLE, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		return new CLSLDoubleConst(Math.pow(args[0].toDouble(), args[1].toDouble()));
	});

	private static final FunctionalChunk sin = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.sin(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk sinh = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.sinh(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk sqrt = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.sqrt(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk tan = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.tan(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk tanh = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Math.tanh(assertAndGet(args, 1, 0).toDouble())));

	@Override
	public FunctionalChunk lookup(String functionName) {
		switch (functionName) {
			case "acos":
				return acos;
			case "asin":
				return asin;
			case "atan":
				return atan;
			case "atan2":
				return atan2;
			case "ceil":
				return ceil;
			case "cos":
				return cos;
			case "cosh":
				return cosh;
			case "exp":
				return exp;
			case "fabs":
				return fabs;
			case "floor":
				return floor;
			case "fmod":
				return fmod;
			case "ldexp":
				return ldexp;
			case "log":
				return log;
			case "log10":
				return log10;
			case "modf":
				return modf;
			case "pow":
				return pow;
			case "sin":
				return sin;
			case "sinh":
				return sinh;
			case "sqrt":
				return sqrt;
			case "tan":
				return tan;
			case "tanh":
				return tanh;
		}
		return null;
	}
}