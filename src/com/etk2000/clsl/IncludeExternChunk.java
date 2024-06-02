package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// FIXME: don't make this public, allow CLSLCompiler to set ehf
class IncludeExternChunk implements ExecutableChunk {
	private final String header;

	// FIXME: validate that this name is valid (not an existence check)
	IncludeExternChunk(String header) {
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
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		CLSLCode h = env.headerFinder.find(header);
		if (h == null)
			throw new CLSL_RuntimeException(header + ": Could not find header");
			
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