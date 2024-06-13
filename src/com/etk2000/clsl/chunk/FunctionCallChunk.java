package com.etk2000.clsl.chunk;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StringBuilderPoolable;
import com.etk2000.clsl.exception.ClslRuntimeException;
import com.etk2000.clsl.exception.function.ClslFunctionCallException;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class FunctionCallChunk implements ExecutableValueChunk, VariableAccess {
	private final VariableAccess function;
	private final ValueChunk[] args;

	// FIXME: go ver calls tho this and ensure they support more complicated lookups, example: `a.b[i]()`
	public FunctionCallChunk(VariableAccess function, ValueChunk... args) {
		this.args = args;
		this.function = function;
	}

	public FunctionCallChunk(InputStream i) throws IOException {
		this.function = Clsl.readChunk(i);
		this.args = Clsl.readValueChunks(i);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		get(env);
		return null;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;

		final FunctionCallChunk that = (FunctionCallChunk) other;
		return function.equals(that.function) && Arrays.equals(args, that.args);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		final ClslValue[] vals = new ClslValue[args.length];
		for (short i = 0; i < args.length; ++i)
			vals[i] = args[i].get(env);

		final ClslValue functionAccess;
		try {
			functionAccess = function.get(env);
		}
		catch (ClslRuntimeException e) {
			throw new ClslFunctionCallException(function + " cannot be resolved to a function");
		}

		try {
			return functionAccess.call(env, vals);
		}
		catch (ClslRuntimeException e) {
			throw new ClslRuntimeException(function.toString() + '(' + Arrays.toString(vals) + "): " + e.getMessage());
		}
	}

	@Override
	public FunctionCallChunk getExecutablePart(OptimizationEnvironment env) {
		return optimize(env);
	}

	@Override
	public String getVariableName() {
		return function.getVariableName();
	}

	// TODO: look into the underlying function if supplied?
	@Override
	public FunctionCallChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			for (short i = 0; i < args.length; ++i)
				args[i] = (ValueChunk) args[i].optimize(env);
		}
		return this;
	}

	@Override
	public String toString() {
		try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
			sb.append(function).append('(');
			if (args.length > 0) {
				for (ValueChunk arg : args)
					sb.append(arg).append(", ");
				sb.deleteLast(2);// remove extra comma
			}
			return sb.append(')').toString();
		}
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, function);
		Clsl.writeChunks(o, args);
	}
}