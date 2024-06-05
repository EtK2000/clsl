package com.etk2000.clsl.chunk.value;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.value.ClslLong;
import com.etk2000.clsl.value.ClslLongConst;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConstLongChunk extends ConstValueChunk {
	private final ClslLongConst val;

	public ConstLongChunk(long val) {
		super(ValueType.LONG);
		this.val = new ClslLongConst(val);
	}

	public ConstLongChunk(InputStream i) throws IOException {
		super(ValueType.LONG);
		val = new ClslLongConst(StreamUtils.readLong(i));
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, val.val);
	}

	@Override
	public ClslLong get(ClslRuntimeEnv env) {
		return val;
	}

	@Override
	public String toString() {
		return Long.toString(val.val) + 'L';
	}
}