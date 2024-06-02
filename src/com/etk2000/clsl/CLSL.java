package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CLSL {
	private static final String[] INVALID_VAR_NAMES = { "break", "case", "char", "const", "continue", "do", "else", "float", "for", "goto", "if", "int", "NULL",
			"return", "sizeof", "static", "struct", "switch", "typedef", "unsigned", "void", "while" };
	public static boolean doWarn = true;

	/**
	 *
	 *
	 */

	@FunctionalInterface
	public static interface FunctionData {
		void exec(CLSLRuntimeEnv env, CLSLValue[] args);
	}

	@FunctionalInterface
	public static interface FunctionDataRet {
		CLSLValue exec(CLSLRuntimeEnv env, CLSLValue[] args);
	}

	public static FunctionalChunk createFunctionalChunk(FunctionData f) {
		return new FunctionalChunk(ValueType.VOID) {
			@Override
			public void transmit(OutputStream o) throws IOException {
				throw new IOException("cannot send CustomFunctionalChunk");
			}

			@Override
			public CLSLValue call(CLSLRuntimeEnv env, CLSLValue... args) {
				f.exec(env, args);
				return null;
			}

			@Override
			public String toString() {
				try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
					return sb.append(typeName(returnType)).append("(...)").append("<Java body>").toString();
				}
			}
		};
	}

	public static FunctionalChunk createFunctionalChunk(ValueType returnType, FunctionDataRet f) {
		return new FunctionalChunk(returnType) {
			@Override
			public void transmit(OutputStream o) throws IOException {
				throw new IOException("cannot send CustomFunctionalChunk");
			}

			@Override
			public CLSLValue call(CLSLRuntimeEnv env, CLSLValue... args) {
				CLSLValue res = f.exec(env, args);
				if (returnType != ValueType.VOID) {
					if (res == null)
						throw new CLSL_RuntimeException("broken java function: result expected");
					if (res.type != returnType)// TODO: cast?
						throw new CLSL_RuntimeException("broken java function: wrong result type (got " + res.type + ", expected " + returnType + ')');
				}
				else if (res != null)
					throw new CLSL_RuntimeException("broken java function: void result expected");
				return res;
			}

			@Override
			public String toString() {
				try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
					return sb.append(typeName(returnType)).append("(...)").append("<Java body>").toString();
				}
			}
		};
	}

	// TODO: add definitions for arrays and pointers
	// TODO: combine classes that are similar (for example Op and SetVarOp)
	// TODO: use CodeBlockChunk, this is {}, don't pop vars beforehand!

	public static ExecutableChunk readExecutableChunk(InputStream i) throws IOException {
		try {
			return (ExecutableChunk) StreamUtils.read(i, ChunkType.class).c.getDeclaredConstructor(InputStream.class).newInstance(i);
		}
		catch (EnumConstantNotPresentException e) {
			return null;
		}
		catch (ReflectiveOperationException e) {
			throw new IOException(e instanceof InvocationTargetException ? e.getCause() : e);
		}
	}

	public static boolean isString(CLSLValue val) {
		return val instanceof CLSLArray && ((CLSLArray) val).component == ValueType.CHAR;
	}

	public static CLSLValue wrap(Object o) {
		return wrap(o, CLSLStructConstWrapped.DEFAULT_WRAP);
	}

	/**
	 * see {@link CLSLStructConstWrapped} for wrap types
	 */
	// FIXME: add array support
	public static CLSLValue wrap(Object o, byte wrapType) {
		if (o instanceof CLSLValue)
			return (CLSLValue) o;

		// primitives
		if (o instanceof Boolean)
			return new CLSLCharConst(0);
		if (o instanceof Byte)
			return new CLSLCharConst((Byte) o);
		if (o instanceof Character)
			return new CLSLCharConst((Character) o);
		if (o instanceof Double)
			return new CLSLDoubleConst((Double) o);
		if (o instanceof Float)
			return new CLSLFloatConst((Float) o);
		if (o instanceof Integer)
			return new CLSLIntConst((Integer) o);
		if (o instanceof Long)
			return new CLSLLongConst((Long) o);
		if (o instanceof String)
			return new CLSLArrayConst((String) o);
		if (o instanceof Short)
			return new CLSLIntConst((Short) o);// TODO: add short support

		return new CLSLStructConstWrapped<>(o, wrapType);
	}

	static ExecutableChunk[] readExecutableChunks(InputStream i) throws IOException {
		ExecutableChunk[] res = new ExecutableChunk[StreamUtils.readByteUnsigned(i)];
		for (short j = 0; j < res.length; ++j)
			res[j] = CLSL.readExecutableChunk(i);
		return res;
	}

	static ValueChunk readValueChunk(InputStream i) throws IOException {
		try {
			return (ValueChunk) StreamUtils.read(i, ChunkType.class).c.getDeclaredConstructor(InputStream.class).newInstance(i);
		}
		catch (EnumConstantNotPresentException e) {
			return null;
		}
		catch (ReflectiveOperationException e) {
			throw new IOException(e instanceof InvocationTargetException ? e.getCause() : e);
		}
	}

	static ValueChunk[] readValueChunks(InputStream i) throws IOException {
		ValueChunk[] res = new ValueChunk[StreamUtils.readByteUnsigned(i)];
		for (short j = 0; j < res.length; ++j)
			res[j] = readValueChunk(i);
		return res;
	}

	static void writeChunk(OutputStream o, CLSLChunk c) throws IOException {
		final ChunkType[] types = ChunkType.values();
		if (c == null) {
			o.write(types.length);
			return;
		}
		for (ChunkType t : types) {
			if (t.c == c.getClass()) {
				StreamUtils.write(o, t);
				c.transmit(o);
				return;
			}
		}
		throw new IOException("cannot transmit " + c.getClass().getName());
	}

	static void writeChunks(OutputStream o, CLSLChunk[] cs) throws IOException {
		if (cs.length > 255)
			throw new IOException("cannot stream array with more than 255 chunks");
		o.write(cs.length);
		for (CLSLChunk c : cs)
			CLSL.writeChunk(o, c);
	}

	/************/
	/* Compiler */
	/************/

	// TODO: try to avoid memcpy
	static boolean isValidId(String str) {
		if (str.isEmpty())
			return false;

		if (!Character.isJavaIdentifierStart(str.charAt(0)))
			return false;
		for (short i = 1; i < str.length(); ++i) {
			if (!Character.isJavaIdentifierPart(str.charAt(1)))
				return false;
		}
		return !Util.contains(INVALID_VAR_NAMES, str, false);
	}

	static boolean evalBoolean(ValueChunk v, CLSLRuntimeEnv env) {
		return v == null ? true : v.get(env).toBoolean();
	}

	static boolean isOne(CLSLValue v) {
		switch (v.type) {
			case CHAR:
				return v.toChar() == 1;
			case DOUBLE:
				return v.toDouble() == 1;
			case FLOAT:
				return v.toFloat() == 1;
			case INT:
				return v.toInt() == 1;
			case LONG:
				return v.toLong() == 1;
		}
		return false;
	}

	@Deprecated
	static String typeName(ValueType type) {
		switch (type) {
			case ARRAY:
				return "array";// FIXME: add type for array
			case CHAR:
				return "char";
			case DOUBLE:
				return "double";
			case FLOAT:
				return "float";
			case INT:
				return "int";
			case LONG:
				return "long";
			case POINTER:
				return "pointer";// FIXME: add type for pointer
			case STRUCT:
				return "struct";// FIXME: get struct name
			case VOID:
				return "void";
		}
		return "<unknown type>";
	}

	static ConstValueChunk toChunk(CLSLValue value) {
		switch (value.type) {
			case CHAR:
				return new ConstCharChunk(value.toChar());
			case DOUBLE:
				return new ConstDoubleChunk(value.toDouble());
			case FLOAT:
				return new ConstFloatChunk(value.toFloat());
			case INT:
				return new ConstIntChunk(value.toInt());
			case LONG:
				return new ConstLongChunk(value.toLong());
		}
		throw new CLSL_Exception("undefined ConstValueChunk of " + value.type);
	}

	static void append(StringBuilderPoolable sb, ExecutableChunk[] chunks) {
		if (chunks.length == 0)
			sb.append(";\t");
		else if (chunks.length == 1)
			sb.append(chunks[0]).append(";\t");
		else {
			sb.append("{ ");
			for (ExecutableChunk chunk : chunks)
				sb.append('\t').append(chunk).append(';');
			sb.append(" }\t");
		}
	}

	/*
	 *  verified to work:
	 *  	int x [= const] ..., float y [= const] ...
	 *  	do-while, if, while
	 *  	x++, x--, x+=const, x-=const
	 */

	// HIGH: remove logical dead code while compiling (and maybe warn)
	// (i.e. i=2;if(i>2), lines after return, etc)
	// TODO: allow blocks without loops, implement switch-case
	public static void main(String[] args) {
		// buildExpression("i<=(j*=2)");

		String src = "float m(int i, float j) {float q = i;q++;}" + //
				"int main() {" + //
				/**/"int i = 1;\n" + //
				/**/"float a = 2.0 * i, b = 4f + a++;\n" + //
				/**/"if (i > 2 && a < b) {\n" + //
				/**//**/"i = 2;\n" + //
				/**/"}\n" + //
				/**/"else if (i == 1)\n" + //
				/**//**/"b++;\n" + //
				/**/"else\n" + //
				/**//**/"i--;\n" + //
				/**/"while (i < 20) {\n" + //
				/**//**/"i += 2;" + //
				/**//**/"b += 3.1;" + //
				/**/"}\n" + //
				/**/"for (int x = i * a; x < 100; x *= 2) {\n" + //
				/**//**/"a <<= 1;\n" + //
				/**/"}\n" + //
				/**/"do {++i;}while(i < 200);\n" + //
				/**/"gl_ClearColor = 5;\n" + //
				/**/"return (i ? i : b) + 1;\n" + //

				// FIXME: support reading this line:
				/**/"float f = 3 | 5, g;\n" + //
				"}";

		long start = System.nanoTime();
		CLSLCode code = new CLSLCompiler().compile(src, true);
		System.out.println("compiled in " + ((System.nanoTime() - start) / 1000) + " \u03BCs");

		start = System.nanoTime();
		optimize(code, 1);
		System.out.println("optimized in " + ((System.nanoTime() - start) / 1000) + " \u03BCs");

		// setup our execution environment
		DirectoryHeaderFinder headerFinder = new DirectoryHeaderFinder();
		CLSLRuntimeEnv env = new CLSLRuntimeEnv(headerFinder, new MainScopeLookupContainer());

		headerFinder.addDirectory(".");
		env.defineVar("gl_ClearColor", new CLSLFloat());

		start = System.nanoTime();
		code.execute(env);
		env.lookupFunction("main").call(env);

		try {
			env.lookupFunction("main").call(env, new CLSLValue[0]);
		}
		catch (CLSL_RuntimeException e) {
			System.err.println(e.getMessage());
		}
		System.out.println("ran in " + ((System.nanoTime() - start) / 1000) + " \u03BCs");

		System.out.println(env.getVar("gl_ClearColor"));
	}

	// TODO: manage unusedVars better (by block and not just by function)
	// TODO: work with optimizations and global variables
	// TODO: the following examples won't optimize yet:
	// • int a;a=a+1;// then no more usages of a
	// • optimize "a=a+1" and "a+=1" into "++a"
	// • optimize "a=a <op> <val>" into "a <op>= <val>"
	// • {int a;}{float a;/* use a here */}
	// won't currently get optimized due to variables not tracked by blocks
	// • anything after return won't get removed
	// FIXME: don't reallocate OptimizationEnvironment for every chunk!!!
	public static void optimize(CLSLCode func, int passes) {
		List<String> unusedVars = new ArrayList<>();
		for (int pass = 0, i; pass < passes; ++pass) {
			for (i = 0; i < func.chunks.size(); ++i) {
				OptimizationEnvironment env = new OptimizationEnvironment(unusedVars);
				ExecutableChunk chunk = func.chunks.get(i).optimize(env);
				if (chunk != null)
					chunk = chunk.optimize(env.secondPass());
				func.chunks.set(i, chunk);
				unusedVars.clear();
			}

			// remove any null references (i.e. removed lines)
			func.chunks.removeIf(line -> line == null);
		}
	}

	public static CLSLValue execute(String function, CLSLValue[] args, CLSLCode code, CLSLRuntimeEnv env) {
		code.execute(env);

		FunctionalChunk f = env.lookupFunction(function);
		if (f != null)
			return f.call(env, args);
		System.err.println("[clslMgr] couldn't find function: " + function);
		return null;
	}
}