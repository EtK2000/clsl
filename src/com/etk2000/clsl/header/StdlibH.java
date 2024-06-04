package com.etk2000.clsl.header;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.ValueType.SubType;
import com.etk2000.clsl.chunk.FunctionalChunk;
import com.etk2000.clsl.exception.ClslRuntimeException;
import com.etk2000.clsl.exception.function.ClslIncompatibleArgumentTypeException;
import com.etk2000.clsl.exception.function.ClslInvalidNumberOfArgumentsException;
import com.etk2000.clsl.value.ClslArray;
import com.etk2000.clsl.value.ClslDoubleConst;
import com.etk2000.clsl.value.ClslInt;
import com.etk2000.clsl.value.ClslIntConst;
import com.etk2000.clsl.value.ClslLStruct;
import com.etk2000.clsl.value.ClslLong;
import com.etk2000.clsl.value.ClslLongConst;
import com.etk2000.clsl.value.ClslPointer;
import com.etk2000.clsl.value.ClslValue;

import java.util.Random;

// please note: none of the exit or multibyte functions are implemented
// TODO: implement bsearch and qsort functions
public class StdlibH extends HeaderBase {
	private static class ClslAbort extends ClslRuntimeException {
		static final ClslAbort INSTANCE = new ClslAbort();

		private ClslAbort() {
			super("abort");
		}
	}

	private static class div_t extends ClslLStruct {
		private final ClslInt quot, rem;

		div_t(int quotient, int remainder) {
			quot = new ClslInt(quotient);
			rem = new ClslInt(remainder);
		}

		@Override
		public ClslValue dot(String member) {
			switch (member) {
				case "quot":
					return quot;
				case "rem":
					return rem;
			}
			throw new UnsupportedOperationException("'struct div_t' has no member named '" + member + '\'');
		}

		@SuppressWarnings("unchecked")
		@Override
		public div_t copy() {
			return new div_t(quot.val, rem.val);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ClslValue) {
				switch (((ClslValue) obj).type) {
					case ARRAY:
					case CHAR:
					case DOUBLE:
					case FLOAT:
					case INT:
					case LONG:
					case VOID:
						break;
					case STRUCT:
						return obj instanceof div_t ? ((div_t) obj).quot.val == quot.val && ((div_t) obj).rem.val == rem.val : false;
				}
				throw new UnsupportedOperationException(
						"The operator == is undefined for the argument type(s) " + typeName() + ", " + ((ClslValue) obj).typeName());
			}
			return false;
		}

		@Override
		public ClslIntConst sizeof() {
			return new ClslIntConst(8);
		}

		@Override
		public String typeName() {
			return "struct div_t";
		}

		@Override
		public String toString() {
			return "struct div_t { long quot = " + quot.val + "; long rem = " + rem.val + ";};";
		}

		@Override
		public div_t toJava() {
			return this;
		}
	}

	private static class ldiv_t extends ClslLStruct {
		private final ClslLong quot, rem;

		ldiv_t(long quotient, long remainder) {
			quot = new ClslLong(quotient);
			rem = new ClslLong(remainder);
		}

		@Override
		public ClslValue dot(String member) {
			switch (member) {
				case "quot":
					return quot;
				case "rem":
					return rem;
			}
			throw new UnsupportedOperationException("'struct ldiv_t' has no member named '" + member + '\'');
		}

		@SuppressWarnings("unchecked")
		@Override
		public ldiv_t copy() {
			return new ldiv_t(quot.val, rem.val);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ClslValue) {
				switch (((ClslValue) obj).type) {
					case ARRAY:
					case CHAR:
					case DOUBLE:
					case FLOAT:
					case INT:
					case LONG:
					case VOID:
						break;
					case STRUCT:
						return obj instanceof ldiv_t && ((ldiv_t) obj).quot.val == quot.val && ((ldiv_t) obj).rem.val == rem.val;
				}
				throw new UnsupportedOperationException(
						"The operator == is undefined for the argument type(s) " + typeName() + ", " + ((ClslValue) obj).typeName());
			}
			return false;
		}

		@Override
		public ClslIntConst sizeof() {
			return new ClslIntConst(16);
		}

		@Override
		public String typeName() {
			return "struct ldiv_t";
		}

		@Override
		public String toString() {
			return "struct ldiv_t { long quot = " + quot.val + "; long rem = " + rem.val + ";};";
		}

		@Override
		public ldiv_t toJava() {
			return this;
		}
	}

	private static final FunctionalChunk abort = Clsl.createFunctionalChunk((env, args) -> {
		throw ClslAbort.INSTANCE;
	});

	/********/
	/* math */
	/********/

	private static final FunctionalChunk abs = Clsl.createFunctionalChunk(ValueType.INT,
			(env, args) -> new ClslIntConst(Math.abs(assertAndGet(args, 1, 0).toInt())));

	private static final FunctionalChunk atof = Clsl.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new ClslDoubleConst(Double.parseDouble(assertStringCast(args, "atof"))));

	private static final FunctionalChunk atoi = Clsl.createFunctionalChunk(ValueType.INT,
			(env, args) -> new ClslIntConst(Integer.parseInt(assertStringCast(args, "atoi"))));

	private static final FunctionalChunk atol = Clsl.createFunctionalChunk(ValueType.LONG,
			(env, args) -> new ClslLongConst(Long.parseLong(assertStringCast(args, "atol"))));

	private static final FunctionalChunk div = Clsl.createFunctionalChunk(ValueType.STRUCT, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		if (args[0].type.subType != SubType.NUMBER)
			throw new ClslIncompatibleArgumentTypeException(0, "div");
		if (args[1].type.subType != SubType.NUMBER)
			throw new ClslIncompatibleArgumentTypeException(1, "div");

		int a = args[0].toInt(), b = args[1].toInt();
		return new div_t(a / b, a % b);
	});

	// note that this function isn't part of the C specification
	private static final FunctionalChunk itoa = Clsl.createFunctionalChunk(ValueType.ARRAY, (env, args) -> {
		if (args.length != 3)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 3);
		if (!Clsl.isString(args[1]))
			throw new ClslIncompatibleArgumentTypeException(1, "itoa");

		switch (args[2].toInt()) {
			case 2:
				return ((ClslArray) args[1]).fill(Integer.toBinaryString(args[0].toInt()));
			case 8:
				return ((ClslArray) args[1]).fill(Integer.toOctalString(args[0].toInt()));
			case 10:
				return ((ClslArray) args[1]).fill(Integer.toString(args[0].toInt()));
			case 16:
				return ((ClslArray) args[1]).fill(Integer.toHexString(args[0].toInt()));
			default:
				return ((ClslArray) args[1]).fill(Integer.toString(args[0].toInt(), args[2].toInt()));
		}
	});

	private static final FunctionalChunk labs = Clsl.createFunctionalChunk(ValueType.LONG,
			(env, args) -> new ClslLongConst(Math.abs(assertAndGet(args, 1, 0).toLong())));

	private static final FunctionalChunk ldiv = Clsl.createFunctionalChunk(ValueType.STRUCT, (env, args) -> {
		if (args.length != 2)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 2);
		if (args[0].type.subType != SubType.NUMBER)
			throw new ClslIncompatibleArgumentTypeException(0, "div");
		if (args[1].type.subType != SubType.NUMBER)
			throw new ClslIncompatibleArgumentTypeException(1, "div");

		long a = args[0].toLong(), b = args[1].toLong();
		return new ldiv_t(a / b, a % b);
	});

	/************/
	/* pointers */
	/************/

	private static final FunctionalChunk free = Clsl.createFunctionalChunk((env, args) -> ((ClslPointer) assertType(args, "free", ValueType.POINTER)).free());

	private static final FunctionalChunk malloc = Clsl.createFunctionalChunk(ValueType.POINTER,
			(env, args) -> new ClslPointer(ValueType.VOID, (short) assertAndGet(args, 1, 0).toChar()));

	private static final FunctionalChunk rand;

	private static final FunctionalChunk srand;

	static {
		/**********/
		/* random */
		/**********/

		final Random r = new Random(0);

		rand = Clsl.createFunctionalChunk(ValueType.INT, (env, args) -> {
			if (args.length != 0)
				throw new ClslInvalidNumberOfArgumentsException(args.length, 0);
			return new ClslInt(r.nextInt());
		});

		srand = Clsl.createFunctionalChunk((env, args) -> r.setSeed(assertAndGet(args, 1, 0).toInt() & 0xffffffffL));
	}

	@Override
	public FunctionalChunk lookup(String functionName) {
		switch (functionName) {
			case "abort":
				return abort;
			case "abs":
				return abs;
			case "atof":
				return atof;
			case "atoi":
				return atoi;
			case "atol":
				return atol;
			case "div":
				return div;
			case "free":
				return free;
			case "itoa":
				return itoa;
			case "labs":
				return labs;
			case "ldiv":
				return ldiv;
			case "malloc":
				return malloc;
			case "rand":
				return rand;
			case "srand":
				return srand;
		}
		return null;
	}
}