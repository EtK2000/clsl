package com.etk2000.clsl.chunk.value;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.value.ClslArray;
import com.etk2000.clsl.value.ClslArrayConst;
import com.etk2000.clsl.value.ClslCharConst;
import com.etk2000.clsl.value.ClslDoubleConst;
import com.etk2000.clsl.value.ClslFloatConst;
import com.etk2000.clsl.value.ClslIntConst;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConstArrayChunk extends ConstValueChunk {
	public final ClslArrayConst val;

	public ConstArrayChunk(ClslArrayConst val) {
		super(ValueType.ARRAY);
		this.val = val;
	}

	public ConstArrayChunk(String val) {
		super(ValueType.ARRAY);
		this.val = new ClslArrayConst(val);
	}

	public ConstArrayChunk(char... vals) {
		super(ValueType.ARRAY);
		val = new ClslArrayConst(ValueType.CHAR, (short) vals.length);
		for (int i = 0; i < vals.length; ++i)
			val.val[i] = new ClslCharConst(vals[i]);
	}

	public ConstArrayChunk(float... vals) {
		super(ValueType.ARRAY);
		val = new ClslArrayConst(ValueType.FLOAT, (short) vals.length);
		for (int i = 0; i < vals.length; ++i)
			val.val[i] = new ClslFloatConst(vals[i]);
	}

	public ConstArrayChunk(int... vals) {
		super(ValueType.ARRAY);
		val = new ClslArrayConst(ValueType.INT, (short) vals.length);
		for (int i = 0; i < vals.length; ++i)
			val.val[i] = ClslIntConst.of(vals[i]);
	}

	public ConstArrayChunk(InputStream i) throws IOException {
		super(ValueType.ARRAY);
		val = new ClslArrayConst(StreamUtils.read(i, ValueType.class), (StreamUtils.readByteUnsigned(i)));
		switch (val.component) {
			case CHAR:
				for (short j = 0; j < val.val.length; ++j)
					val.val[j] = new ClslCharConst((char) StreamUtils.readByte(i));
				break;
			case DOUBLE:
				for (short j = 0; j < val.val.length; ++j)
					val.val[j] = new ClslDoubleConst(StreamUtils.readDouble(i));
				break;
			case FLOAT:
				for (short j = 0; j < val.val.length; ++j)
					val.val[j] = new ClslFloatConst(StreamUtils.readFloat(i));
				break;
			case INT:
				for (short j = 0; j < val.val.length; ++j)
					val.val[j] = ClslIntConst.of(StreamUtils.readInt(i));
				break;
			case LONG:
				for (short j = 0; j < val.val.length; ++j)
					val.val[j] = new ClslDoubleConst(StreamUtils.readLong(i));
				break;
		}
	}

	@Override
	public ClslArray get(ClslRuntimeEnv env) {
		return val;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;

		return val.equals(((ConstArrayChunk) other).val);
	}

	@Override
	public String toString() {
		return val.toString();
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
}