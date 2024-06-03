package com.etk2000.clsl;

import com.etk2000.clsl.exception.function.ClslIncompatibleArgumentTypeException;
import com.etk2000.clsl.exception.function.ClslInvalidNumberOfArgumentsException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public abstract class _HeaderBase implements ExecutableChunk, FunctionLookupTable {
	// helper functions to save repetitive lines of code

	protected static CLSLValue assertAndGet(CLSLValue[] args, int expected, int resultIndex) {
		if (args.length != expected)
			throw new ClslInvalidNumberOfArgumentsException(args.length, expected);
		return args[resultIndex];
	}

	protected static CLSLArray assertString(CLSLValue[] args, String funcName) {
		if (args.length != 1)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 1);
		if (!CLSL.isString(args[0]))
			throw new ClslIncompatibleArgumentTypeException(0, funcName);
		return (CLSLArray) args[0];
	}

	protected static String assertStringCast(CLSLValue[] args, String funcName) {
		if (args.length != 1)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 1);
		if (!CLSL.isString(args[0]))
			throw new ClslIncompatibleArgumentTypeException(0, funcName);
		return (String) args[0].toJava();
	}

	protected static CLSLValue assertType(CLSLValue[] args, String funcName, ValueType type) {
		if (args.length != 1)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 1);
		if (args[0].type != type)
			throw new ClslIncompatibleArgumentTypeException(0, funcName);
		return args[0];
	}

	final public CLSLCode asCode() {
		return new CLSLCode(Arrays.asList(this));
	}

	@Override
	final public void transmit(OutputStream o) throws IOException {
		throw new IllegalAccessError("can't be transmitted");
	}

	@Override
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		env.addHeader(this);
		return null;
	}

	@Override
	final public _HeaderBase optimize(OptimizationEnvironment env) {
		return this;
	}
}