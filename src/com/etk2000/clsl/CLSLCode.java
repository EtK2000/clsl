package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class CLSLCode {
	public static final CLSLCode NOP = new CLSLCode(new ArrayList<>());

	// TODO: maybe make ExecutableChunk[]?
	final List<ExecutableChunk> chunks;

	CLSLCode(List<ExecutableChunk> chunks) {
		this.chunks = chunks;
	}

	public CLSLCode(InputStream i) throws IOException {
		short sz = StreamUtils.readByteUnsigned(i);
		chunks = new ArrayList<>(sz);
		for (short j = 0; j < sz; ++j)
			chunks.add(CLSL.readExecutableChunk(i));
	}

	// LOW: look into writing in single operation
	public void write(OutputStream o) throws IOException {
		if (chunks.size() > 255)
			throw new IOException("cannot stream array with more than 255 chunks");
		o.write(chunks.size());
		for (ExecutableChunk c : chunks)
			CLSL.writeChunk(o, c);
	}

	public void execute(CLSLRuntimeEnv env) {
		for (ExecutableChunk chunk : chunks)
			chunk.execute(env);
	}

	@Override
	public String toString() {
		try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
			for (ExecutableChunk chunk : chunks) {
				sb.append(chunk);
				if (!(chunk instanceof IncludeExternChunk || chunk instanceof IncludeChunk))
					sb.append(';');
				sb.append('\n');
			}
			return sb.toString();
		}
	}
}