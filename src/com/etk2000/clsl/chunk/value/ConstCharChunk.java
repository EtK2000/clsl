package com.etk2000.clsl.chunk.value;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.ClslUtil;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.value.ClslChar;
import com.etk2000.clsl.value.ClslCharConst;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConstCharChunk extends ConstValueChunk {
	private final ClslCharConst val;

	public ConstCharChunk(char val) {
		super(ValueType.CHAR);
		this.val = new ClslCharConst(val);
	}

	public ConstCharChunk(InputStream i) throws IOException {
		super(ValueType.CHAR);
		val = new ClslCharConst(StreamUtils.readByte(i));
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		o.write(val.val);
	}

	@Override
	public ClslChar get(ClslRuntimeEnv env) {
		return val;
	}

	@Override
	public String toString() {
		return '\'' + ClslUtil.escape(Character.toString(val.val)) + '\'';
	}
}