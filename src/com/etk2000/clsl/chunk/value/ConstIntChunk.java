package com.etk2000.clsl.chunk.value;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.value.ClslInt;
import com.etk2000.clsl.value.ClslIntConst;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConstIntChunk extends ConstValueChunk {
	private final ClslIntConst val;

	public ConstIntChunk(int val) {
		super(ValueType.INT);
		this.val = new ClslIntConst(val);
	}

	ConstIntChunk(InputStream i) throws IOException {
		super(ValueType.INT);
		val = new ClslIntConst(StreamUtils.readInt(i));
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, val.val);
	}

	@Override
	public ClslInt get(ClslRuntimeEnv env) {
		return val;
	}

	@Override
	public String toString() {
		return Integer.toString(val.val);
	}
}