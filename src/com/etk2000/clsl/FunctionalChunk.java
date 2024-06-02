package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FunctionalChunk extends BlockChunk implements ExecutableValueChunk {
	private final String name;
	public final ValueType returnType;
	private final Group<String, ValueType>[] parameters;
	private final ExecutableChunk[] effect;
	private byte modifiers;

	FunctionalChunk(String name, ValueType returnType, Group<String, ValueType>[] parameters, ExecutableChunk[] effect) {
		if (!CLSL.isValidId(this.name = name))
			throw new CLSL_CompilerException("invalid function name: " + name);

		this.returnType = returnType;
		this.parameters = parameters;
		this.effect = effect;
	}

	// used in CLSL.createFunctionalChunk
	protected FunctionalChunk(ValueType returnType) {
		this.name = null;
		this.returnType = returnType;
		this.parameters = null;
		this.effect = null;
	}

	@SuppressWarnings("unchecked")
	FunctionalChunk(InputStream i) throws IOException {
		if (!CLSL.isValidId(name = StreamUtils.readString(i)))
			throw new CLSL_CompilerException("invalid function name: " + name);

		returnType = StreamUtils.read(i, ValueType.class);
		{
			Group<String, ValueType>[] params = (Group<String, ValueType>[]) new Group<?, ?>[StreamUtils.readByteUnsigned(i)];
			if (params.length == 1) {
				String name = StreamUtils.readString(i);
				if (name == null)
					parameters = null;
				else {
					(parameters = params)[0] = new Group<>(name, StreamUtils.read(i, ValueType.class));
					for (short j = 1; j < parameters.length; ++j)
						parameters[j] = new Group<>(StreamUtils.readString(i), StreamUtils.read(i, ValueType.class));
				}
			}
			else {
				parameters = params;
				for (short j = 0; j < parameters.length; ++j)
					parameters[j] = new Group<>(StreamUtils.readString(i), StreamUtils.read(i, ValueType.class));
			}
		}
		effect = CLSL.readExecutableChunks(i);
		modifiers = StreamUtils.readByte(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		if (parameters.length > 255)
			throw new IOException("cannot stream function with more than 255 parameters");

		StreamUtils.write(o, name);
		StreamUtils.write(o, returnType);

		if (parameters == null) {// varargs
			o.write(1);
			StreamUtils.write(o, (String) null);
		}
		else {
			o.write(parameters.length);
			for (short i = 0; i < parameters.length; ++i) {
				StreamUtils.write(o, parameters[i].a);
				StreamUtils.write(o, parameters[i].b);
			}
		}

		CLSL.writeChunks(o, effect);
		o.write(modifiers);
	}

	// execute the function, assumes args have been setup already
	private CLSLValue exec(CLSLRuntimeEnv env) {
		ReturnChunk ret;
		for (ExecutableChunk e : effect) {
			if (e instanceof ReturnChunk)
				return ((ReturnChunk) e).val.get(env);
			if ((ret = e.execute(env)) != null)
				return ret.val.get(env);
		}

		throw new CLSL_RuntimeException("function " + name + " didn't return a value");
	}

	// execute the function, assumes args have been setup already
	// HIGH: merge this and the above
	private void execVoid(CLSLRuntimeEnv env) {
		for (ExecutableChunk e : effect) {
			if (e instanceof ReturnChunk || e.execute(env) != null)
				return;
		}
	}

	final public CLSLValue call(CLSLRuntimeEnv env) {
		env.pushStack(true);
		try {
			switch (returnType) {
				case ARRAY:
				case CHAR:
				case DOUBLE:
				case FLOAT:
				case INT:
				case LONG:
				case POINTER:
				case STRUCT:
					return exec(env);
				case VOID:
					execVoid(env);
					return null;
			}

			throw new CLSL_RuntimeException("welp, something broke...");
		}
		finally {
			env.popStack(true);
		}
	}

	// FIXME: work with vararg functions (pushing args, etc)
	public CLSLValue call(CLSLRuntimeEnv env, CLSLValue... args) {
		if (parameters != null) {// varargs don't need validations
			if (args.length != parameters.length)
				throw new CLSL_RuntimeException("invalid number of arguments, got " + args.length + " expected " + parameters.length);

			// validate args types
			for (int i = 0; i < args.length; ++i) {
				if (args[i].type.subType != parameters[i].b.subType)
					throw new CLSL_RuntimeException("function not applicable for the arguments");
			}
		}

		env.pushStack(true);
		try {
			if (parameters != null && parameters.length > 0) {
				// push parameters
				for (int i = 0; i < parameters.length; ++i)
					env.defineVar(parameters[i].a, args[i].type == parameters[i].b ? args[i].copy() : (CLSLValue) args[i].cast(parameters[i].b));
			}

			switch (returnType) {
				case ARRAY:
				case CHAR:
				case DOUBLE:
				case FLOAT:
				case INT:
				case LONG:
				case POINTER:
				case STRUCT:
					return exec(env);
				case VOID:
					execVoid(env);
					return null;
			}

			throw new CLSL_RuntimeException("welp, something broke...");
		}
		finally {
			env.popStack(true);
		}
	}

	@Override
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		env.addFunction(name, this);
		return null;
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		throw new UnsupportedOperationException("Cannot call a function like that");
	}

	@Override
	public String toString() {
		try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
			sb.append(CLSL.typeName(returnType)).append(' ').append(name).append('(');
			if (parameters == null)
				sb.append("...");
			else if (parameters.length > 0) {
				for (short i = 0; i < parameters.length; ++i)
					sb.append(CLSL.typeName(parameters[i].b)).append(' ').append(parameters[i].a).append(", ");
				sb.deleteLast(2);// remove extra comma
			}
			sb.append(") {");

			if (effect.length > 0)
				sb.append('\n');
			for (ExecutableChunk chunk : effect)
				sb.append('\t').append(chunk).append(";\n");
			return sb.append("}\n").toString();
		}
	}

	@Override
	public FunctionalChunk getExecutablePart(OptimizationEnvironment env) {
		return optimize(env);
	}

	// FIXME: if MODIF_CONSISTANT is set optimize for it,
	// if it isn't check if it's implied
	@Override
	public FunctionalChunk optimize(OptimizationEnvironment env) {
		ExecutableChunk[] newEffect = optimize(effect, env);
		return effect != newEffect ? new FunctionalChunk(name, returnType, parameters, newEffect) : this;
	}
}