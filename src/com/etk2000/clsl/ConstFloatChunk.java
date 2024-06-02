package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class ConstFloatChunk extends ConstValueChunk {
	private final CLSLFloatConst val;

	ConstFloatChunk(float val) {
		super(ValueType.FLOAT);
		this.val = new CLSLFloatConst(val);
	}

	ConstFloatChunk(InputStream i) throws IOException {
		super(ValueType.FLOAT);
		val = new CLSLFloatConst(StreamUtils.readFloat(i));
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, val.val);
	}

	@Override
	public CLSLFloat get(CLSLRuntimeEnv env) {
		return val;
	}

	@Override
	public String toString() {
		return Float.isFinite(val.val) ? Float.toString(val.val) + 'F' : Float.toString(val.val).toUpperCase();
	}
}