package com.etk2000.clsl.chunk;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.ClslUtil;
import com.etk2000.clsl.Group;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.StringBuilderPoolable;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.exception.function.ClslFunctionCallException;
import com.etk2000.clsl.exception.function.ClslFunctionDidntReturnAValueException;
import com.etk2000.clsl.exception.function.ClslFunctionNotApplicableForArgumentsException;
import com.etk2000.clsl.exception.function.ClslInvalidFunctionNameException;
import com.etk2000.clsl.exception.function.ClslInvalidNumberOfArgumentsException;
import com.etk2000.clsl.value.ClslFunctionAccess;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FunctionalChunk extends BlockChunk implements ExecutableValueChunk {
	private final String name;
	public final ValueType returnType;
	private final Group<String, ValueType>[] parameters;
	private final ExecutableChunk[] effect;
	private byte modifiers;

	public FunctionalChunk(String name, ValueType returnType, Group<String, ValueType>[] parameters, ExecutableChunk[] effect) {
		if (!ClslUtil.isValidId(this.name = name))
			throw new ClslInvalidFunctionNameException(name);

		this.returnType = returnType;
		this.parameters = parameters;
		this.effect = effect;
	}

	// used in Clsl.createFunctionalChunk
	protected FunctionalChunk(ValueType returnType) {
		this.name = null;
		this.returnType = returnType;
		this.parameters = null;
		this.effect = null;
	}

	@SuppressWarnings("unchecked")
	public FunctionalChunk(InputStream i) throws IOException {
		if (!ClslUtil.isValidId(name = StreamUtils.readString(i)))
			throw new ClslInvalidFunctionNameException(name);

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
		effect = Clsl.readExecutableChunks(i);
		modifiers = StreamUtils.readByte(i);
	}

	public ClslFunctionAccess access() {
		return new ClslFunctionAccess(this);
	}

	public final ClslValue call(ClslRuntimeEnv env) {
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

			throw new ClslFunctionCallException("welp, something broke...");
		}
		finally {
			env.popStack(true);
		}
	}

	// FIXME: work with vararg functions (pushing args, etc)
	public ClslValue call(ClslRuntimeEnv env, ClslValue... args) {
		if (parameters != null) {// varargs don't need validations
			if (args.length != parameters.length)
				throw new ClslInvalidNumberOfArgumentsException(args.length, parameters.length);

			// validate args types
			for (int i = 0; i < args.length; ++i) {
				if (args[i].type.subType != parameters[i].b.subType)
					throw new ClslFunctionNotApplicableForArgumentsException();
			}
		}

		env.pushStack(true);
		try {
			if (parameters != null && parameters.length > 0) {
				// push parameters
				for (int i = 0; i < parameters.length; ++i)
					env.defineVar(parameters[i].a, args[i].type == parameters[i].b ? args[i].copy() : (ClslValue) args[i].cast(parameters[i].b));
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

			throw new ClslFunctionCallException("welp, something broke...");
		}
		finally {
			env.popStack(true);
		}
	}

	// execute the function, assumes args have been setup already
	private ClslValue exec(ClslRuntimeEnv env) {
		ReturnChunk ret;
		for (ExecutableChunk e : effect) {
			if (e instanceof ReturnChunk)
				return ((ReturnChunk) e).val.get(env);
			if ((ret = e.execute(env)) != null)
				return ret.val.get(env);
		}

		throw new ClslFunctionDidntReturnAValueException(name);
	}

	// execute the function, assumes args have been setup already
	// HIGH: merge this and the above
	private void execVoid(ClslRuntimeEnv env) {
		for (ExecutableChunk e : effect) {
			if (e instanceof ReturnChunk || e.execute(env) != null)
				return;
		}
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		env.defineVar(name, access());
		return null;
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		throw new UnsupportedOperationException("Cannot call a function like that");
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

	@Override
	public String toString() {
		try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
			sb.append(Clsl.typeName(returnType)).append(' ').append(name).append('(');
			if (parameters == null)
				sb.append("...");
			else if (parameters.length > 0) {
				for (short i = 0; i < parameters.length; ++i)
					sb.append(Clsl.typeName(parameters[i].b)).
							append(' ').append(parameters[i].a).append(", ");
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

		Clsl.writeChunks(o, effect);
		o.write(modifiers);
	}
}