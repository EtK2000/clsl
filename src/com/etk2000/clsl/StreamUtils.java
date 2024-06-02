package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

class StreamUtils {
	private static final byte[] NULL_STRING = { 1, 0 };
	private static final byte[] helper4 = new byte[4], helper8 = new byte[8];

	private static void assertLen(int len) throws IOException {
		if (len > 255)
			throw new IOException(len + " > MAX_ARRAY");
	}

	public static <E extends Enum<?>> E read(InputStream i, Class<E> e) throws IOException {
		final E[] constants = e.getEnumConstants();
		if (constants.length > 255)
			throw new IOException("not implemented");

		short ordinal = StreamUtils.readByteUnsigned(i);
		if (ordinal >= constants.length)
			throw new EnumConstantNotPresentException(e, e.getName() + ": " + ordinal + " >= " + constants.length);

		return constants[ordinal];
	}

	public static byte readByte(InputStream i) throws IOException {
		int res = i.read();
		if (res == -1)
			throw new IOException("stream ended");
		return (byte) res;
	}

	public static short readByteUnsigned(InputStream i) throws IOException {
		short res = (short) i.read();
		if (res == -1)
			throw new IOException("stream ended");
		return res;
	}

	public static double readDouble(InputStream i) throws IOException {
		return Double.longBitsToDouble(readLong(i));
	}

	public static float readFloat(InputStream i) throws IOException {
		return Float.intBitsToFloat(readInt(i));
	}

	public static int readInt(InputStream i) throws IOException {
		return readByte(i) << 24 | readByteUnsigned(i) << 16 | readByteUnsigned(i) << 8 | readByteUnsigned(i);
	}

	// LOW: convert to the format everything else uses
	public static long readLong(InputStream i) throws IOException {
		long res = 0;
		res += (long) readByteUnsigned(i) << 56;
		res += (long) readByteUnsigned(i) << 48;
		res += (long) readByteUnsigned(i) << 40;
		res += (long) readByteUnsigned(i) << 32;
		res += (long) readByteUnsigned(i) << 24;
		res += (long) readByteUnsigned(i) << 16;
		res += (long) readByteUnsigned(i) << 8;
		res += readByteUnsigned(i);
		return res;
		//return (long) (readByte(i) << 56) | (long) (readByteUnsigned(i) << 48) | readByteUnsigned(i) << 40 | readByteUnsigned(i) << 32 | readByteUnsigned(i) << 24
		//		| readByteUnsigned(i) << 16 | readByteUnsigned(i) << 8 | readByteUnsigned(i);
	}

	// TODO: maybe have length 0 as null and not ""
	public static String readString(InputStream i) throws IOException {
		final byte[] bs = new byte[readByteUnsigned(i)];
		i.read(bs);
		if (bs.length == 0)
			return "";
		if (bs.length == 1 && bs[0] == 0)
			return null;
		return new String(bs, StandardCharsets.UTF_8);
	}

	public static void write(OutputStream o, double d) throws IOException {
		write(o, Double.doubleToLongBits(d));
	}

	public static void write(OutputStream o, Enum<?> e) throws IOException {
		if (e.getClass().getEnumConstants().length > 255)
			throw new IOException("not implemented");
		o.write(e.ordinal());
	}

	public static void write(OutputStream o, float f) throws IOException {
		write(o, Float.floatToIntBits(f));
	}

	public static void write(OutputStream o, int i) throws IOException {
		synchronized (helper4) {
			helper4[0] = (byte) (i >> 24);
			helper4[1] = (byte) (i >> 16);
			helper4[2] = (byte) (i >> 8);
			helper4[3] = (byte) i;
			o.write(helper4);
		}
	}

	public static void write(OutputStream o, long l) throws IOException {
		synchronized (helper8) {
			helper8[0] = (byte) (l >> 56);
			helper8[1] = (byte) (l >> 48);
			helper8[2] = (byte) (l >> 40);
			helper8[3] = (byte) (l >> 32);
			helper8[4] = (byte) (l >> 24);
			helper8[5] = (byte) (l >> 16);
			helper8[6] = (byte) (l >> 8);
			helper8[7] = (byte) l;
			o.write(helper8);
		}
	}

	// TODO: maybe have length 0 as null and not ""
	public static void write(OutputStream o, String s) throws IOException {
		if (s == null)
			o.write(NULL_STRING);
		else {
			final byte[] bs = s.getBytes(StandardCharsets.UTF_8);
			assertLen(bs.length);
			o.write(bs.length);
			o.write(bs);
		}
	}
}