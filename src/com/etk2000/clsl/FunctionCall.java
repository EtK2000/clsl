package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

class FunctionCall implements ExecutableValueChunk {
	private final String name;
	private final ValueChunk[] args;

	FunctionCall(String name, ValueChunk... args) {
		if (!CLSL.isValidId(this.name = name))
			throw new CLSL_CompilerException("invalid func name: " + name);
		this.args = args;
	}

	FunctionCall(InputStream i) throws IOException {
		if (!CLSL.isValidId(name = StreamUtils.readString(i)))
			throw new CLSL_CompilerException("invalid func name: " + name);
		args = CLSL.readValueChunks(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, name);
		CLSL.writeChunks(o, args);
	}

	@Override
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		get(env);
		return null;
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		FunctionalChunk f = env.lookupFunction(name);
		if (f == null)
			throw new CLSL_RuntimeException(name + " cannot be resolved to a function");

		CLSLValue[] vals = new CLSLValue[args.length];
		for (short i = 0; i < args.length; ++i)
			vals[i] = args[i].get(env);
		
		try {
			return f.call(env, vals);
		}
		catch(CLSL_RuntimeException e) {
			throw new CLSL_RuntimeException(name + '(' + Arrays.toString(vals) + "): " + e.getMessage());
		}
	}

	@Override
	public String toString() {
		try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
			sb.append(name).append('(');
			if (args.length > 0) {
				for (short i = 0; i < args.length; ++i)
					sb.append(args[i]).append(", ");
				sb.deleteLast(2);// remove extra comma
			}
			return sb.append(')').toString();
		}
	}
	
	@Override
	public FunctionCall getExecutablePart(OptimizationEnvironment env) {
		return optimize(env);
	}

	// TODO: look into the underlying function if supplied?
	@Override
	public FunctionCall optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			for (short i = 0; i < args.length; ++i)
				args[i] = (ValueChunk) args[i].optimize(env);
		}
		return this;
	}
}