package com.etk2000.clsl.chunk.value;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.value.ClslDouble;
import com.etk2000.clsl.value.ClslDoubleConst;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConstDoubleChunk extends ConstValueChunk {
	private final ClslDoubleConst val;

	public ConstDoubleChunk(double val) {
		super(ValueType.DOUBLE);
		this.val = new ClslDoubleConst(val);
	}

	public ConstDoubleChunk(InputStream i) throws IOException {
		super(ValueType.DOUBLE);
		val = new ClslDoubleConst(StreamUtils.readFloat(i));
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, val.val);
	}

	@Override
	public ClslDouble get(ClslRuntimeEnv env) {
		return val;
	}

	@Override
	public String toString() {
		return Double.toString(val.val);
	}
}