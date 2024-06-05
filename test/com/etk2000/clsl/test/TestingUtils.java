package com.etk2000.clsl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.etk2000.clsl.ThrowingFunction;
import com.etk2000.clsl.chunk.ClslChunk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class TestingUtils {
	public static void assertOutput(String expected, Runnable run) {
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		final PrintStream stdout = System.out;
		System.setOut(new PrintStream(buffer));
		try {
			run.run();
			assertEquals(expected, buffer.toString());
		}
		finally {
			System.setOut(stdout);
		}
	}

	public static <T extends ClslChunk> T transmitAndReceive(
			T original,
			ThrowingFunction<InputStream, T, IOException> read
	) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		original.transmit(baos);
		return read.apply(new ByteArrayInputStream(baos.toByteArray()));
	}

	private TestingUtils() {
	}
}