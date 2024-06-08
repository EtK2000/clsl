package com.etk2000.clsl.chunk.op;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.chunk.ClslChunk;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.value.ConstCharChunk;
import com.etk2000.clsl.chunk.value.ConstDoubleChunk;
import com.etk2000.clsl.chunk.value.ConstFloatChunk;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.chunk.value.ConstLongChunk;
import com.etk2000.clsl.chunk.value.ConstValueChunk;
import com.etk2000.clsl.value.ClslValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OpCast implements ValueChunk {
	private final ValueChunk var;
	private final ValueType to;

	public OpCast(ValueChunk var, ValueType to) {
		this.var = var;
		this.to = to;
	}

	OpCast(InputStream i) throws IOException {
		var = Clsl.readChunk(i);
		to = StreamUtils.read(i, ValueType.class);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, var);
		StreamUtils.write(o, to);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return (ClslValue) var.get(env).cast(to);
	}

	@Override
	public String toString() {
		return "((" + Clsl.typeName(to) + ") " + var + ')';
	}

	@Override
	public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return var.getExecutablePart(env);
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
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
				ClslChunk op = var.optimize(env.forValue());
				if (op != var)
					return new OpCast((ValueChunk) op, to).optimize(env);
			}
		}
		return this;
	}
}