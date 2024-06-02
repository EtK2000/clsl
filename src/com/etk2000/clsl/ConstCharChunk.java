package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class ConstCharChunk extends ConstValueChunk {
	private final CLSLCharConst val;

	ConstCharChunk(char val) {
		super(ValueType.CHAR);
		this.val = new CLSLCharConst(val);
	}

	ConstCharChunk(InputStream i) throws IOException {
		super(ValueType.CHAR);
		val = new CLSLCharConst(StreamUtils.readByte(i));
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		o.write(val.val);
	}

	@Override
	public CLSLChar get(CLSLRuntimeEnv env) {
		return val;
	}

	@Override
	public String toString() {
		return '\'' + Util.escape(Character.toString(val.val)) + '\'';
	}
}