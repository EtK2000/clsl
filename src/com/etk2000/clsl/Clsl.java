package com.etk2000.clsl;

import com.etk2000.clsl.chunk.ChunkType;
import com.etk2000.clsl.chunk.ClslChunk;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.FunctionalChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.value.ConstCharChunk;
import com.etk2000.clsl.chunk.value.ConstDoubleChunk;
import com.etk2000.clsl.chunk.value.ConstFloatChunk;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.chunk.value.ConstLongChunk;
import com.etk2000.clsl.chunk.value.ConstValueChunk;
import com.etk2000.clsl.compiler.ClslCompiler;
import com.etk2000.clsl.exception.ClslException;
import com.etk2000.clsl.exception.function.ClslBrokenJavaFunctionException;
import com.etk2000.clsl.header.DirectoryHeaderFinder;
import com.etk2000.clsl.value.ClslArray;
import com.etk2000.clsl.value.ClslArrayConst;
import com.etk2000.clsl.value.ClslCharConst;
import com.etk2000.clsl.value.ClslDoubleConst;
import com.etk2000.clsl.value.ClslFloat;
import com.etk2000.clsl.value.ClslFloatConst;
import com.etk2000.clsl.value.ClslIntConst;
import com.etk2000.clsl.value.ClslLongConst;
import com.etk2000.clsl.value.ClslStructConstWrapped;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Clsl {
	public static boolean doWarn = true;

	/**
	 *
	 */

	@FunctionalInterface
	public interface FunctionData {
		void exec(ClslRuntimeEnv env, ClslValue[] args);
	}

	@FunctionalInterface
	public interface FunctionDataRet {
		ClslValue exec(ClslRuntimeEnv env, ClslValue[] args);
	}

	public static FunctionalChunk createFunctionalChunk(FunctionData f) {
		return new FunctionalChunk(ValueType.VOID) {
			@Override
			public void transmit(OutputStream o) throws IOException {
				throw new IOException("cannot send CustomFunctionalChunk");
			}

			@Override
			public ClslValue call(ClslRuntimeEnv env, ClslValue... args) {
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
			public ClslValue call(ClslRuntimeEnv env, ClslValue... args) {
				ClslValue res = f.exec(env, args);
				if (returnType != ValueType.VOID) {
					if (res == null)
						throw new ClslBrokenJavaFunctionException("result expected");
					if (res.type != returnType)// TODO: cast?
						throw new ClslBrokenJavaFunctionException("wrong result type (got " + res.type + ", expected " + returnType + ')');
				}
				else if (res != null)
					throw new ClslBrokenJavaFunctionException("void result expected");
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

	public static boolean isString(ClslValue val) {
		return val instanceof ClslArray && ((ClslArray) val).component == ValueType.CHAR;
	}

	public static ClslValue wrap(Object o) {
		return wrap(o, ClslStructConstWrapped.DEFAULT_WRAP);
	}

	/**
	 * see {@link ClslStructConstWrapped} for wrap types
	 */
	// FIXME: add array support
	public static ClslValue wrap(Object o, byte wrapType) {
		if (o == null)
			return null;
		if (o instanceof ClslValue)
			return (ClslValue) o;

		// primitives
		if (o instanceof Boolean)
			return new ClslCharConst(0);
		if (o instanceof Byte)
			return new ClslCharConst((Byte) o);
		if (o instanceof Character)
			return new ClslCharConst((Character) o);
		if (o instanceof Double)
			return new ClslDoubleConst((Double) o);
		if (o instanceof Float)
			return new ClslFloatConst((Float) o);
		if (o instanceof Integer)
			return new ClslIntConst((Integer) o);
		if (o instanceof Long)
			return new ClslLongConst((Long) o);
		if (o instanceof String)
			return new ClslArrayConst((String) o);
		if (o instanceof Short)
			return new ClslIntConst((Short) o);// TODO: add short support

		return new ClslStructConstWrapped<>(o, wrapType);
	}

	@SuppressWarnings("unchecked")
	public static <T extends ClslChunk> T readChunk(InputStream i) throws IOException {
		try {
			return (T) StreamUtils.read(i, ChunkType.class).read.apply(i);
		}
		catch (EnumConstantNotPresentException e) {
			return null;
		}
	}

	public static ExecutableChunk[] readExecutableChunks(InputStream i) throws IOException {
		final ExecutableChunk[] res = new ExecutableChunk[StreamUtils.readByteUnsigned(i)];
		for (short j = 0; j < res.length; ++j)
			res[j] = Clsl.readChunk(i);
		return res;
	}

	public static ValueChunk[] readValueChunks(InputStream i) throws IOException {
		final ValueChunk[] res = new ValueChunk[StreamUtils.readByteUnsigned(i)];
		for (short j = 0; j < res.length; ++j)
			res[j] = readChunk(i);
		return res;
	}

	public static void writeChunk(OutputStream o, ClslChunk c) throws IOException {
		if (c == null)
			o.write(ChunkType.values().length);

		else {
			StreamUtils.write(o, ChunkType.valueOf(c.getClass()));
			c.transmit(o);
		}
	}

	public static void writeChunks(OutputStream o, ClslChunk[] cs) throws IOException {
		if (cs.length > 255)
			throw new IOException("cannot stream array with more than 255 chunks");
		o.write(cs.length);
		for (ClslChunk c : cs)
			Clsl.writeChunk(o, c);
	}

	/************/
	/* Compiler */

	/***********/

	public static boolean evalBoolean(ValueChunk v, ClslRuntimeEnv env) {
		return v == null || v.get(env).toBoolean();
	}

	public static boolean isOne(ClslValue v) {
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
	public static String typeName(ValueType type) {
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

	public static ConstValueChunk toChunk(ClslValue value) {
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
		throw new ClslException("undefined ConstValueChunk of " + value.type);
	}

	public static void append(StringBuilderPoolable sb, ExecutableChunk[] chunks) {
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
				///**//**/"a <<= 1;\n" + //
				/**/"}\n" + //
				/**/"do {++i;}while(i < 200);\n" + //
				/**/"gl_ClearColor = 5;\n" + //
				/**/"return (i ? i : b) + 1;\n" + //

				// FIXME: support reading this line:
				/**/"float f = 3 | 5, g;\n" + //
				"}";

		long start = System.nanoTime();
		ClslCode code = ClslCompiler.compile(src, true);
		System.out.println("compiled in " + ((System.nanoTime() - start) / 1000) + " \u03BCs");

		start = System.nanoTime();
		optimize(code, 1);
		System.out.println("optimized in " + ((System.nanoTime() - start) / 1000) + " \u03BCs");

		// setup our execution environment
		DirectoryHeaderFinder headerFinder = new DirectoryHeaderFinder();
		ClslRuntimeEnv env = new ClslRuntimeEnv(headerFinder);

		headerFinder.addDirectory(".");
		env.defineVar("gl_ClearColor", new ClslFloat());

		start = System.nanoTime();
		code.execute(env);
		env.getVar("main").call(env);

		System.out.println("ran in " + ((System.nanoTime() - start) / 1000) + " μs");

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
	public static void optimize(ClslCode func, int passes) {
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
			func.chunks.removeIf(Objects::isNull);
		}
	}

	public static ClslValue execute(String function, ClslValue[] args, ClslCode code, ClslRuntimeEnv env) {
		code.execute(env);
		return env.getVar(function).call(env, args);
	}
}