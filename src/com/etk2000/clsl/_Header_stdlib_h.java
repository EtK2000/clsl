package com.etk2000.clsl;

import java.util.Random;

import com.etk2000.clsl.ValueType.SubType;

// please note: none of the exit or multibyte functions are implemented
// TODO: implement bsearch and qsort functions
public class _Header_stdlib_h extends _HeaderBase {
	@SuppressWarnings("serial")
	private static class CLSL_Abort extends CLSL_RuntimeException {
		static final CLSL_Abort INSTANCE = new CLSL_Abort();

		private CLSL_Abort() {
			super("abort");
		}
	}

	private static class div_t extends CLSLStruct {
		private final CLSLInt quot, rem;

		div_t(int quotient, int remainder) {
			quot = new CLSLInt(quotient);
			rem = new CLSLInt(remainder);
		}

		@Override
		public CLSLValue dot(String member) {
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
			if (obj instanceof CLSLValue) {
				switch (((CLSLValue) obj).type) {
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
						"The operator == is undefined for the argument type(s) " + typeName() + ", " + ((CLSLValue) obj).typeName());
			}
			return false;
		}

		@Override
		public CLSLIntConst sizeof() {
			return new CLSLIntConst(8);
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

	private static class ldiv_t extends CLSLStruct {
		private final CLSLLong quot, rem;

		ldiv_t(long quotient, long remainder) {
			quot = new CLSLLong(quotient);
			rem = new CLSLLong(remainder);
		}

		@Override
		public CLSLValue dot(String member) {
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
			if (obj instanceof CLSLValue) {
				switch (((CLSLValue) obj).type) {
					case ARRAY:
					case CHAR:
					case DOUBLE:
					case FLOAT:
					case INT:
					case LONG:
					case VOID:
						break;
					case STRUCT:
						return obj instanceof ldiv_t ? ((ldiv_t) obj).quot.val == quot.val && ((ldiv_t) obj).rem.val == rem.val : false;
				}
				throw new UnsupportedOperationException(
						"The operator == is undefined for the argument type(s) " + typeName() + ", " + ((CLSLValue) obj).typeName());
			}
			return false;
		}

		@Override
		public CLSLIntConst sizeof() {
			return new CLSLIntConst(16);
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

	private static final FunctionalChunk abort = CLSL.createFunctionalChunk((env, args) -> {
		throw CLSL_Abort.INSTANCE;
	});

	/********/
	/* math */
	/********/

	private static final FunctionalChunk abs = CLSL.createFunctionalChunk(ValueType.INT,
			(env, args) -> new CLSLIntConst(Math.abs(assertAndGet(args, 1, 0).toInt())));

	private static final FunctionalChunk atof = CLSL.createFunctionalChunk(ValueType.DOUBLE,
			(env, args) -> new CLSLDoubleConst(Double.parseDouble(assertStringCast(args, "atof"))));

	private static final FunctionalChunk atoi = CLSL.createFunctionalChunk(ValueType.INT,
			(env, args) -> new CLSLIntConst(Integer.parseInt(assertStringCast(args, "atoi"))));

	private static final FunctionalChunk atol = CLSL.createFunctionalChunk(ValueType.LONG,
			(env, args) -> new CLSLLongConst(Long.parseLong(assertStringCast(args, "atol"))));

	private static final FunctionalChunk div = CLSL.createFunctionalChunk(ValueType.STRUCT, (env, args) -> {
		if (args.length != 2)
			throw new CLSL_RuntimeException("invalid number of arguments, got " + args.length + " expected 2");
		if (args[0].type.subType != SubType.NUMBER)
			throw new CLSL_RuntimeException("incompatible type for argument 0 of `div`");
		if (args[1].type.subType != SubType.NUMBER)
			throw new CLSL_RuntimeException("incompatible type for argument 1 of `div`");

		int a = args[0].toInt(), b = args[1].toInt();
		return new div_t(a / b, a % b);
	});

	// note that this function isn't part of the C specification
	private static final FunctionalChunk itoa = CLSL.createFunctionalChunk(ValueType.ARRAY, (env, args) -> {
		if (args.length != 3)
			throw new CLSL_RuntimeException("invalid number of arguments, got " + args.length + " expected 3");
		if (!CLSL.isString(args[1]))
			throw new CLSL_RuntimeException("incompatible type for argument 1 of `itoa`");

		switch (args[2].toInt()) {
			case 2:
				return ((CLSLArray) args[1]).fill(Integer.toBinaryString(args[0].toInt()));
			case 8:
				return ((CLSLArray) args[1]).fill(Integer.toOctalString(args[0].toInt()));
			case 10:
				return ((CLSLArray) args[1]).fill(Integer.toString(args[0].toInt()));
			case 16:
				return ((CLSLArray) args[1]).fill(Integer.toHexString(args[0].toInt()));
			default:
				return ((CLSLArray) args[1]).fill(Integer.toString(args[0].toInt(), args[2].toInt()));
		}
	});

	private static final FunctionalChunk labs = CLSL.createFunctionalChunk(ValueType.LONG,
			(env, args) -> new CLSLLongConst(Math.abs(assertAndGet(args, 1, 0).toLong())));

	private static final FunctionalChunk ldiv = CLSL.createFunctionalChunk(ValueType.STRUCT, (env, args) -> {
		if (args.length != 2)
			throw new CLSL_RuntimeException("invalid number of arguments, got " + args.length + " expected 2");
		if (args[0].type.subType != SubType.NUMBER)
			throw new CLSL_RuntimeException("incompatible type for argument 0 of `div`");
		if (args[1].type.subType != SubType.NUMBER)
			throw new CLSL_RuntimeException("incompatible type for argument 1 of `div`");

		long a = args[0].toLong(), b = args[1].toLong();
		return new ldiv_t(a / b, a % b);
	});

	/************/
	/* pointers */
	/************/

	private static final FunctionalChunk free = CLSL.createFunctionalChunk((env, args) -> ((CLSLPointer) assertType(args, "free", ValueType.POINTER)).free());

	private static final FunctionalChunk malloc = CLSL.createFunctionalChunk(ValueType.POINTER,
			(env, args) -> new CLSLPointer(ValueType.VOID, (short) assertAndGet(args, 1, 0).toChar()));

	private static final FunctionalChunk rand;

	private static final FunctionalChunk srand;

	static {
		/**********/
		/* random */
		/**********/

		final Random r = new Random(0);

		rand = CLSL.createFunctionalChunk(ValueType.INT, (env, args) -> {
			if (args.length != 0)
				throw new CLSL_RuntimeException("invalid number of arguments, got " + args.length + " expected 0");
			return new CLSLInt(r.nextInt());
		});

		srand = CLSL.createFunctionalChunk((env, args) -> r.setSeed(assertAndGet(args, 1, 0).toInt() & 0xffffffffL));
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