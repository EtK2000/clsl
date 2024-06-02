package com.etk2000.clsl;

import java.util.Formatter;

// this library is supposed to be safe so we don't create unsafe functions
// TODO: add a safety toggle so all functions exist?
public class _Header_stdio_h extends _HeaderBase {
	private static final FunctionalChunk printf, sprintf;

	static {
		final StringBuilder formatSb = new StringBuilder();
		@SuppressWarnings("resource") // doesn't need close()
		final Formatter format = new Formatter(formatSb);

		printf = CLSL.createFunctionalChunk(ValueType.INT, (env, args) -> {
			if (args.length == 0)
				throw new CLSL_RuntimeException("too few arguments to function `printf`");
			if (!CLSL.isString(args[0]))
				throw new CLSL_RuntimeException("incompatible type for argument 0 of `printf`");

			try {// TODO: don't create a new array
				Object[] vals = new Object[args.length - 1];
				for (short i = 0; i < vals.length; ++i)
					vals[i] = args[i + 1].toJava();

				synchronized (formatSb) {
					try {
						format.format(((String) args[0].toJava()), vals);
						System.out.println(formatSb);
						return new CLSLIntConst(formatSb.length());
					}
					finally {
						formatSb.setLength(0);
					}
				}
			}
			catch (Exception e) {
				System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
				return new CLSLIntConst(-1);
			}
		});

		sprintf = CLSL.createFunctionalChunk(ValueType.INT, (env, args) -> {
			if (args.length < 2)
				throw new CLSL_RuntimeException("too few arguments to function `sprintf`");
			if (!CLSL.isString(args[0]))
				throw new CLSL_RuntimeException("incompatible type for argument 0 of `sprintf`");
			if (!CLSL.isString(args[1]))
				throw new CLSL_RuntimeException("incompatible type for argument 1 of `sprintf`");

			try {// TODO: don't create a new array
				Object[] vals = new Object[args.length - 2];
				for (short i = 0; i < vals.length; ++i)
					vals[i] = args[i + 2].toJava();

				synchronized (formatSb) {
					try {
						format.format(((String) args[1].toJava()), vals);
						((CLSLArray) args[0]).fill(formatSb);
						return new CLSLIntConst(formatSb.length());
					}
					finally {
						formatSb.setLength(0);
					}
				}
			}
			catch (Exception e) {
				if (e instanceof CLSL_RuntimeException)
					throw e;
				System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
				return new CLSLIntConst(-1);
			}
		});
	}

	@Override
	public FunctionalChunk lookup(String functionName) {
		switch (functionName) {
			case "printf":
				return printf;
			case "sprintf":
				return sprintf;
		}
		return null;
	}
}