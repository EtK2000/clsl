package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class ConstIntChunk extends ConstValueChunk {
	private final CLSLIntConst val;

	ConstIntChunk(int val) {
		super(ValueType.INT);
		this.val = new CLSLIntConst(val);
	}

	ConstIntChunk(InputStream i) throws IOException {
		super(ValueType.INT);
		val = new CLSLIntConst(StreamUtils.readInt(i));
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, val.val);
	}

	@Override
	public CLSLInt get(CLSLRuntimeEnv env) {
		return val;
	}

	@Override
	public String toString() {
		return Integer.toString(val.val);
	}
}