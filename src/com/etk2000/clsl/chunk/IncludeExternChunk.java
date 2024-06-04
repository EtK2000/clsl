package com.etk2000.clsl.chunk;

import com.etk2000.clsl.ClslCode;
import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.exception.include.ClslHeaderNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// FIXME: don't make this public, allow ClslCompiler to set ehf
public class IncludeExternChunk implements ExecutableChunk {
	private final String header;

	// FIXME: validate that this name is valid (not an existence check)
	public IncludeExternChunk(String header) {
		this.header = header;
	}

	IncludeExternChunk(InputStream i) throws IOException {
		header = StreamUtils.readString(i);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, header);
	}

	@Override
	public ReturnChunk execute(ClslRuntimeEnv env) {
		ClslCode h = env.headerFinder.find(header);
		if (h == null)
			throw new ClslHeaderNotFoundException(header);

		h.execute(env);
		return null;
	}

	@Override
	public String toString() {
		return "#include \"" + header + '"';
	}

	// TODO: maybe check if any included functions and/or variables are used
	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		return this;
	}
}