package com.etk2000.clsl.chunk;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.OptimizationEnvironment;
import com.etk2000.clsl.StreamUtils;
import com.etk2000.clsl.exception.include.ClslInvalidHeaderException;
import com.etk2000.clsl.header.LangH;
import com.etk2000.clsl.header.MathH;
import com.etk2000.clsl.header.StdioH;
import com.etk2000.clsl.header.StdlibH;
import com.etk2000.clsl.header.StringH;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IncludeChunk implements ExecutableChunk {
	enum BuiltinHeader {
		lang_h(new LangH()),
		math_h(new MathH()),
		stdlib_h(new StdlibH()),
		stdio_h(new StdioH()),
		string_h(StringH.INSTANCE);

		final ExecutableChunk headerCode;

		BuiltinHeader(ExecutableChunk headerCode) {
			this.headerCode = headerCode;
		}

		static BuiltinHeader from(String header) {
			return valueOf(header.replace('.', '_'));
		}
	}

	private final BuiltinHeader header;

	public IncludeChunk(String header) {
		try {
			this.header = BuiltinHeader.from(header);
		}
		catch (IllegalArgumentException e) {
			throw new ClslInvalidHeaderException(header);
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
	public ReturnChunk execute(ClslRuntimeEnv env) {
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