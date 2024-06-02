package com.etk2000.clsl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class IncludeChunk implements ExecutableChunk {
	static enum BuiltinHeader {
		lang_h(new _Header_lang_h()), math_h(new _Header_math_h()), stdlib_h(new _Header_stdlib_h()), stdio_h(new _Header_stdio_h()), string_h(
				new _Header_string_h());

		final ExecutableChunk headerCode;

		private BuiltinHeader(ExecutableChunk headerCode) {
			this.headerCode = headerCode;
		}

		static BuiltinHeader from(String header) {
			return valueOf(header.replace('.', '_'));
		}
	}

	private final BuiltinHeader header;

	IncludeChunk(String header) {
		try {
			this.header = BuiltinHeader.from(header);
		}
		catch (IllegalArgumentException e) {
			throw new CLSL_CompilerException("invalid header: " + header);
		}
	}

	IncludeChunk(InputStream i) throws IOException {
		header = StreamUtils.read(i, BuiltinHeader.class);
	}

	@Override
	public void transmit(OutputStream o) throws IOException {
		StreamUtils.write(o, header);
	}

	@Override
	public ReturnChunk execute(CLSLRuntimeEnv env) {
		header.headerCode.execute(env);
		return null;
	}

	@Override
	public String toString() {
		return "#include <" + header.name().replace('_', '.') + '>';
	}

	// TODO: maybe check if any included functions and/or variables are used
	@Override
	public ExecutableChunk optimize(OptimizationEnvironment env) {
		return this;
	}
}