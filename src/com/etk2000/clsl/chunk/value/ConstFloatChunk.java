package com.etk2000.clsl.chunk.value;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.value.ClslFloat;
import com.etk2000.clsl.value.ClslFloatConst;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConstFloatChunk extends ConstValueChunk {
	private final ClslFloatConst val;

	public ConstFloatChunk(float val) {
		super(ValueType.FLOAT);
		this.val = new ClslFloatConst(val);
	}

	public ConstFloatChunk(InputStream i) throws IOException {
		super(ValueType.FLOAT);
		val = new ClslFloatConst(StreamUtils.readFloat(i));
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, val.val);
	}

	@Override
	public ClslFloat get(ClslRuntimeEnv env) {
		return val;
	}

	@Override
	public String toString() {
		return Float.isFinite(val.val) ? Float.toString(val.val) + 'F' : Float.toString(val.val).toUpperCase();
	}
}