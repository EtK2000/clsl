package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class ConstDoubleChunk extends ConstValueChunk {
	private final CLSLDoubleConst val;

	ConstDoubleChunk(double val) {
		super(ValueType.DOUBLE);
		this.val = new CLSLDoubleConst(val);
	}

	ConstDoubleChunk(InputStream i) throws IOException {
		super(ValueType.DOUBLE);
		val = new CLSLDoubleConst(StreamUtils.readFloat(i));
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, val.val);
	}

	@Override
	public CLSLDouble get(CLSLRuntimeEnv env) {
		return val;
	}

	@Override
	public String toString() {
		return Double.toString(val.val);
	}
}