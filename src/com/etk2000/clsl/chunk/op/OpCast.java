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
	private final ValueChunk val;
	private final ValueType to;

	public OpCast(ValueChunk val, ValueType to) {
		this.val = val;
		this.to = to;
	}

	public OpCast(InputStream i) throws IOException {
		this.val = Clsl.readChunk(i);
		this.to = StreamUtils.read(i, ValueType.class);
	}

	@Override
	public ClslValue get(ClslRuntimeEnv env) {
		return (ClslValue) val.get(env).cast(to);
	}

	@Override
	public ExecutableChunk getExecutablePart(OptimizationEnvironment env) {
		return val.getExecutablePart(env);
	}

	@Override
	public ValueChunk optimize(OptimizationEnvironment env) {
		if (env.isFirstPass) {
			if (val instanceof ConstValueChunk) {
				if (((ConstValueChunk) val).type == to)
					return this;// already the correct type

				switch (to) {
					case CHAR:
						return new ConstCharChunk(val.get(null).toChar());
					case DOUBLE:
						return new ConstDoubleChunk(val.get(null).toDouble());
					case FLOAT:
						return new ConstFloatChunk(val.get(null).toFloat());
					case INT:
						return new ConstIntChunk(val.get(null).toInt());
					case LONG:
						return new ConstLongChunk(val.get(null).toLong());
				}
			}
			else {
				ClslChunk op = val.optimize(env.forValue());
				if (op != val)
					return new OpCast((ValueChunk) op, to).optimize(env);
			}
		}
		return this;
	}

	@Override
	public String toString() {
		return "((" + Clsl.typeName(to) + ") " + val + ')';
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		Clsl.writeChunk(o, val);
		StreamUtils.write(o, to);
	}

	public OpCast withValue(ValueChunk val) {
		return new OpCast(val, to);
	}
}