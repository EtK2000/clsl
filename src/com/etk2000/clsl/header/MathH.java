package com.etk2000.clsl.header;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.chunk.FunctionalChunk;
import com.etk2000.clsl.exception.function.ClslIncompatibleArgumentTypeException;
import com.etk2000.clsl.exception.function.ClslInvalidNumberOfArgumentsException;
import com.etk2000.clsl.value.ClslArray;
import com.etk2000.clsl.value.ClslDoubleConst;

// FIXME: implement this:
// double frexp(double x, int *exponent)
public class MathH extends HeaderBase {
	public static final MathH INSTANCE = new MathH();

	private static final FunctionalChunk acos = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.acos(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk asin = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.asin(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk atan = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.atan(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk atan2 = Clsl.createFunctionalChunk(ValueType.DOUBLE, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		return new ClslDoubleConst(Math.atan2(args[0].toDouble(), args[1].toDouble()));
	});

	private static final FunctionalChunk ceil = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.ceil(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk cos = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.cos(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk cosh = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.cosh(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk exp = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.exp(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk fabs = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.abs(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk floor = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.floor(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk fmod = Clsl.createFunctionalChunk(ValueType.DOUBLE, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		return new ClslDoubleConst(args[0].toDouble() % args[1].toDouble());
	});

	// LOW: see if we can optimize this with a single Math call
	private static final FunctionalChunk ldexp = Clsl.createFunctionalChunk(ValueType.DOUBLE, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		return new ClslDoubleConst(args[0].toDouble() * Math.pow(2, args[1].toDouble()));
	});

	private static final FunctionalChunk log = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.log(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk log10 = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.log10(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk modf = Clsl.createFunctionalChunk(ValueType.DOUBLE, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		if (!(args[1] instanceof ClslArray))
			throw new ClslIncompatibleArgumentTypeException(1, "modf");

		// LOW: add a set function that doesn't require wrapping in ClslValue
		long integer = args[0].toLong();
		((ClslArray) args[1]).val[0].set(new ClslDoubleConst(integer));
		return new ClslDoubleConst(args[0].toDouble() - integer);
	});

	private static final FunctionalChunk pow = Clsl.createFunctionalChunk(ValueType.DOUBLE, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		return new ClslDoubleConst(Math.pow(args[0].toDouble(), args[1].toDouble()));
	});

	private static final FunctionalChunk sin = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.sin(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk sinh = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.sinh(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk sqrt = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.sqrt(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk tan = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.tan(assertAndGet(args, 1, 0).toDouble())));

	private static final FunctionalChunk tanh = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Math.tanh(assertAndGet(args, 1, 0).toDouble())));

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

	private MathH() {
	}
}