package com.etk2000.clsl.chunk.variable.definition;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ReturnChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.value.ConstArrayChunk;
import com.etk2000.clsl.exception.variable.ClslArrayInstantiationException;
import com.etk2000.clsl.value.ClslArray;
import com.etk2000.clsl.value.ClslArrayConst;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// FIXME: allow definition as var[] = {...}
public class DefineArray extends DefineVar {
	private final ValueType component;
	private final short len;

	public DefineArray(String name, ValueType component, short len) {
		super(ValueType.ARRAY, name, null);
		this.component = component;
		this.len = len;
	}

	public DefineArray(String name, ClslArrayConst val) {
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
	public DefineArray withVal(ValueChunk newVal) {
		// FIXME: support any chunk that returns an array
		if (newVal instanceof ConstArrayChunk)
			return new DefineArray(name, ((ConstArrayChunk) newVal).val);
		throw new ClslArrayInstantiationException();
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		env.defineVar(name, val != null ? val.get(env) : new ClslArray(component, len));
		return null;
	}

	@Override
	public String toString() {
		return Clsl.typeName(component) + ' ' + name + '[' + len + (val == null ? ']' : "] = " + val);
	}

	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		if (val != null)
			return super.optimize(env);

		if (env.isFirstPass) {
			env.unusedVars.add(name);
			return this;
		}
		return env.unusedVars.contains(name) ? null : this;
	}
}