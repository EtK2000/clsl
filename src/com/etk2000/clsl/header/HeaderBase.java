package com.etk2000.clsl.header;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslCode;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.FunctionLookupTable;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ReturnChunk;
import com.etk2000.clsl.exception.function.ClslIncompatibleArgumentTypeException;
import com.etk2000.clsl.exception.function.ClslInvalidNumberOfArgumentsException;
import com.etk2000.clsl.value.ClslArray;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

public abstract class HeaderBase implements ExecutableChunk, FunctionLookupTable {
	// helper functions to save repetitive lines of code

	protected static ClslValue assertAndGet(ClslValue[] args, int expected, int resultIndex) {
		if (args.length != expected)
			throw new ClslInvalidNumberOfArgumentsException(args.length, expected);
		return args[resultIndex];
	}

	protected static ClslArray assertString(ClslValue[] args, String funcName) {
		if (args.length != 1)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 1);
		if (!Clsl.isString(args[0]))
			throw new ClslIncompatibleArgumentTypeException(0, funcName);
		return (ClslArray) args[0];
	}

	protected static String assertStringCast(ClslValue[] args, String funcName) {
		if (args.length != 1)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 1);
		if (!Clsl.isString(args[0]))
			throw new ClslIncompatibleArgumentTypeException(0, funcName);
		return (String) args[0].toJava();
	}

	protected static ClslValue assertType(ClslValue[] args, String funcName, ValueType type) {
		if (args.length != 1)
			throw new ClslInvalidNumberOfArgumentsException(args.length, 1);
		if (args[0].type != type)
			throw new ClslIncompatibleArgumentTypeException(0, funcName);
		return args[0];
	}

	final public ClslCode asCode() {
		return new ClslCode(Collections.singletonList(this));
	}

	@Override
	final public void transmit(OutputStream o) throws IOException {
		throw new IllegalAccessError("can't be transmitted");
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		env.addHeader(this);
		return null;
	}

	@Override
	final public HeaderBase optimize(OptimizationEnvironment env) {
		return this;
	}
}