package com.etk2000.clsl;

import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.IncludeChunk;
import com.etk2000.clsl.chunk.IncludeExternChunk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ClslCode {
	public static final ClslCode NOP = new ClslCode(new ArrayList<>());

	// TODO: maybe make ExecutableChunk[]?
	final List<ExecutableChunk> chunks;

	public ClslCode(List<ExecutableChunk> chunks) {
		this.chunks = chunks;
	}

	public ClslCode(InputStream i) throws IOException {
		short sz = StreamUtils.readByteUnsigned(i);
		chunks = new ArrayList<>(sz);
		for (short j = 0; j < sz; ++j)
			chunks.add(Clsl.readExecutableChunk(i));
	}

	// LOW: look into writing in single operation
	public void write(OutputStream o) throws IOException {
		if (chunks.size() > 255)
			throw new IOException("cannot stream array with more than 255 chunks");
		o.write(chunks.size());
		for (ExecutableChunk c : chunks)
			Clsl.writeChunk(o, c);
	}

	public void execute(ClslRuntimeEnv env) {
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