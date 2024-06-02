package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class ConstLongChunk extends ConstValueChunk {
	private final CLSLLongConst val;

	ConstLongChunk(long val) {
		super(ValueType.LONG);
		this.val = new CLSLLongConst(val);
	}

	ConstLongChunk(InputStream i) throws IOException {
		super(ValueType.LONG);
		val = new CLSLLongConst(StreamUtils.readLong(i));
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, val.val);
	}

	@Override
	public CLSLLong get(CLSLRuntimeEnv env) {
		return val;
	}

	@Override
	public String toString() {
		return Long.toString(val.val) + 'L';
	}
}