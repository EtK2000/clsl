package com.etk2000.clsl.chunk;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.ClslUtil;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.StringBuilderPoolable;
import com.etk2000.clsl.exception.ClslRuntimeException;
import com.etk2000.clsl.exception.function.ClslFunctionCallException;
import com.etk2000.clsl.exception.function.ClslInvalidFunctionNameException;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class FunctionCallChunk implements ExecutableValueChunk {
	private final String name;
	private final ValueChunk[] args;

	public FunctionCallChunk(String name, ValueChunk... args) {
		if (!ClslUtil.isValidId(this.name = name))
			throw new ClslInvalidFunctionNameException("invalid func name: " + name);
		this.args = args;
	}

	FunctionCallChunk(InputStream i) throws IOException {
		if (!ClslUtil.isValidId(name = StreamUtils.readString(i)))
			throw new ClslInvalidFunctionNameException("invalid func name: " + name);
		args = Clsl.readValueChunks(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, name);
		Clsl.writeChunks(o, args);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		get(env);
		return null;
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		FunctionalChunk f = env.lookupFunction(name);
		if (f == null)
			throw new ClslFunctionCallException(name + " cannot be resolved to a function");

		ClslValue[] vals = new ClslValue[args.length];
		for (short i = 0; i < args.length; ++i)
			vals[i] = args[i].get(env);

		try {
			return f.call(env, vals);
		}
		catch (ClslRuntimeException e) {
			throw new ClslRuntimeException(name + '(' + Arrays.toString(vals) + "): " + e.getMessage());
		}
	}

	@Override
	public String toString() {
		try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
			sb.append(name).append('(');
			if (args.length > 0) {
				for (ValueChunk arg : args)
					sb.append(arg).append(", ");
				sb.deleteLast(2);// remove extra comma
			}
			return sb.append(')').toString();
		}
	}

	@Override
	public FunctionCallChunk getExecutablePart(OptimizationEnvironment env) {
		return optimize(env);
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
}