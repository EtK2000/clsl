package com.etk2000.clsl;

import com.etk2000.clsl.exception.variable.ClslArrayInstantiationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// FIXME: allow definition as var[] = {...}
class DefineArray extends DefineVar {
	private final ValueType component;
	private final short len;

	DefineArray(String name, ValueType component, short len) {
		super(ValueType.ARRAY, name, null);
		this.component = component;
		this.len = len;
	}

	DefineArray(String name, CLSLArrayConst val) {
		super(ValueType.ARRAY, name, new ConstArrayChunk(val));
		this.component = val.component;
		this.len = (short) val.val.length;
	}

	DefineArray(InputStream i) throws IOException {
		super(ValueType.ARRAY, i);
		if (val == null) {
			component = StreamUtils.read(i, ValueType.class);
			len = StreamUtils.readByteUnsigned(i);
		}

		// FIXME: support any chunk that returns an array
		else if (val instanceof ConstArrayChunk) {
			component = ((ConstArrayChunk) val).val.component;
			len = (short) ((ConstArrayChunk) val).val.val.length;
		}
		else
			throw new ClslArrayInstantiationException();
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		super.transmit(o);
		StreamUtils.write(o, component);
		o.write(len);
	}

	@Override
	DefineArray withVal(ValueChunk newVal) {
		// FIXME: support any chunk that returns an array
		if (newVal instanceof ConstArrayChunk)
			return new DefineArray(name, ((ConstArrayChunk) newVal).val);
		throw new ClslArrayInstantiationException();
	}

	@Override
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		env.defineVar(name, val != null ? val.get(env) : new CLSLArray(component, len));
		return null;
	}

	@Override
	public String toString() {
		return CLSL.typeName(component) + ' ' + name + '[' + len + (val == null ? ']' : "] = " + val);
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (val != null)
			return super.optimize(env);

		if (env.firstPass) {
			env.unusedVars.add(name);
			return this;
		}
		return env.unusedVars.contains(name) ? null : this;
	}
}