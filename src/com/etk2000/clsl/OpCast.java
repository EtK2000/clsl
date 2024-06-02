package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class OpCast implements ValueChunk {
	private final ValueChunk var;
	private final ValueType to;

	OpCast(ValueChunk var, ValueType to) {
		this.var = var;
		this.to = to;
	}

	OpCast(InputStream i) throws IOException {
		var = CLSL.readValueChunk(i);
		to = StreamUtils.read(i, ValueType.class);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		CLSL.writeChunk(o, var);
		StreamUtils.write(o, to);
	}

	@Override
	public CLSLValue get(CLSLRuntimeEnv env) {
		return (CLSLValue) var.get(env).cast(to);
	}

	@Override
	public String toString() {
		return "((" + CLSL.typeName(to) + ") " + var + ')';
	}

	@Override
	public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return var.getExecutablePart(env);
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.firstPass) {
			if (var instanceof ConstValueChunk) {
				if (((ConstValueChunk) var).type == to)
					return this;// already the correct type

				switch (to) {
					case CHAR:
						return new ConstCharChunk(var.get(null).toChar());
					case DOUBLE:
						return new ConstDoubleChunk(var.get(null).toDouble());
					case FLOAT:
						return new ConstFloatChunk(var.get(null).toFloat());
					case INT:
						return new ConstIntChunk(var.get(null).toInt());
					case LONG:
						return new ConstLongChunk(var.get(null).toLong());
				}
			}
			else {
				CLSLChunk op = var.optimize(env.forValue());
				if (op != var)
					return new OpCast((ValueChunk) op, to).optimize(env);
			}
		}
		return this;
	}
}