package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class ConstArrayChunk extends ConstValueChunk {
	final CLSLArrayConst val;

	ConstArrayChunk(CLSLArrayConst val) {
		super(ValueType.ARRAY);
		this.val = val;
	}

	ConstArrayChunk(String val) {
		super(ValueType.ARRAY);
		this.val = new CLSLArrayConst(val);
	}

	private ConstArrayChunk(char... vals) {
		super(ValueType.ARRAY);
		val = new CLSLArrayConst(ValueType.CHAR, (short) vals.length);
		for (int i = 0; i < vals.length; ++i)
			val.val[i] = new CLSLCharConst(vals[i]);
	}

	private ConstArrayChunk(float... vals) {
		super(ValueType.ARRAY);
		val = new CLSLArrayConst(ValueType.FLOAT, (short) vals.length);
		for (int i = 0; i < vals.length; ++i)
			val.val[i] = new CLSLFloatConst(vals[i]);
	}

	private ConstArrayChunk(int... vals) {
		super(ValueType.ARRAY);
		val = new CLSLArrayConst(ValueType.INT, (short) vals.length);
		for (int i = 0; i < vals.length; ++i)
			val.val[i] = new CLSLIntConst(vals[i]);
	}

	ConstArrayChunk(InputStream i) throws IOException {
		super(ValueType.ARRAY);
		val = new CLSLArrayConst(StreamUtils.read(i, ValueType.class), (StreamUtils.readByteUnsigned(i)));
		switch (val.component) {
			case CHAR:
				for (short j = 0; j < val.val.length; ++j)
					val.val[j] = new CLSLCharConst((char) StreamUtils.readByte(i));
				break;
			case DOUBLE:
				for (short j = 0; j < val.val.length; ++j)
					val.val[j] = new CLSLDoubleConst(StreamUtils.readDouble(i));
				break;
			case FLOAT:
				for (short j = 0; j < val.val.length; ++j)
					val.val[j] = new CLSLFloatConst(StreamUtils.readFloat(i));
				break;
			case INT:
				for (short j = 0; j < val.val.length; ++j)
					val.val[j] = new CLSLIntConst(StreamUtils.readInt(i));
				break;
			case LONG:
				for (short j = 0; j < val.val.length; ++j)
					val.val[j] = new CLSLDoubleConst(StreamUtils.readLong(i));
				break;
		}
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, val.component);
		o.write(val.val.length);
		switch (val.component) {
			case CHAR:
				for (short i = 0; i < val.val.length; ++i)
					o.write(val.val[i].toChar());
				break;
			case DOUBLE:
				for (short i = 0; i < val.val.length; ++i)
					StreamUtils.write(o, val.val[i].toDouble());
				break;
			case FLOAT:
				for (short i = 0; i < val.val.length; ++i)
					StreamUtils.write(o, val.val[i].toFloat());
				break;
			case INT:
				for (short i = 0; i < val.val.length; ++i)
					StreamUtils.write(o, val.val[i].toInt());
				break;
			case LONG:
				for (short i = 0; i < val.val.length; ++i)
					StreamUtils.write(o, val.val[i].toLong());
				break;
		}
	}

	@Override
	public CLSLArray get(CLSLRuntimeEnv env) {
		return val;
	}

	@Override
	public String toString() {
		return val.toString();
	}
}