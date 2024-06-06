package com.etk2000.clsl.chunk.value;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.value.ClslInt;
import com.etk2000.clsl.value.ClslIntConst;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class ConstIntChunk extends ConstValueChunk {
	private final ClslIntConst val;

	public ConstIntChunk(int val) {
		super(ValueType.INT);
		this.val = new ClslIntConst(val);
	}

	public ConstIntChunk(InputStream i) throws IOException {
		super(ValueType.INT);
		val = new ClslIntConst(StreamUtils.readInt(i));
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;

		return Objects.equals(val, ((ConstIntChunk) other).val);
	}

	@Override
	public ClslInt get(ClslRuntimeEnv env) {
		return val;
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, val.val);
	}

	@Override
	public String toString() {
		return Integer.toString(val.val);
	}
}