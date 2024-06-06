package com.etk2000.clsl.compiler;

import com.etk2000.clsl.ClslUtil;
import com.etk2000.clsl.chunk.ExecutableValueChunk;
import com.etk2000.clsl.chunk.FunctionCallChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.op.OpDec;
import com.etk2000.clsl.chunk.op.OpInc;
import com.etk2000.clsl.chunk.variable.set.SetVar;
import com.etk2000.clsl.chunk.variable.set.SetVarAdd;
import com.etk2000.clsl.chunk.variable.set.SetVarBinAnd;
import com.etk2000.clsl.chunk.variable.set.SetVarBinOr;
import com.etk2000.clsl.chunk.variable.set.SetVarDiv;
import com.etk2000.clsl.chunk.variable.set.SetVarModulus;
import com.etk2000.clsl.chunk.variable.set.SetVarMul;
import com.etk2000.clsl.chunk.variable.set.SetVarShiftLeft;
import com.etk2000.clsl.chunk.variable.set.SetVarShiftRight;
import com.etk2000.clsl.chunk.variable.set.SetVarSub;
import com.etk2000.clsl.chunk.variable.set.SetVarXor;
import com.etk2000.clsl.exception.ClslCompilerException;

import java.util.regex.Matcher;

class OperatorParser {
	private static final byte MODE_SET = 0,
			MODE_SET_ADD = 1,
			MODE_SET_SUB = 2,
			MODE_SET_MUL = 3,
			MODE_SET_DIV = 4,
			MODE_SET_MOD = 5,
			MODE_SET_XOR = 6,
			MODE_SET_BAND = 7,
			MODE_SET_BOR = 8,
			MODE_SET_BLS = 9,
			MODE_SET_BRS = 10;

	private static void assetVariableNameIsValid(ClslCompilationEnv env, String variableName) {
		if (!ClslUtil.isValidId(variableName))
			throw new ClslCompilerException("invalid variable name: `" + variableName + '`', env.indexInSource, env.source, env.matcher);
	}

	private static void consumeSemicolon(int i, String res, Matcher m) {
		if (!m.find())
			throw new ClslCompilerException("error: expected `;`", i, res, m);
		if (!m.group().equals(";"))
			throw new ClslCompilerException("error: expected `;`", i, res, m);
	}

	static void parseNext(ClslCompilationEnv env) {
		// FIXME: remove this var, have it in env
		final String expression = env.source.substring(env.indexInSource, env.matcher.start());
		if (!expression.isEmpty())
			assetVariableNameIsValid(env, expression);

		switch (env.matcher.group()) {
			// function call
			case "(":
				env.exec.add(new FunctionCallChunk(env.source.substring(env.indexInSource, env.matcher.start()), ClslCompiler.readArguments(env.source, env.matcher).toArray(ClslUtil.CHUNK_VALUE)));
				consumeSemicolon(env.indexInSource, env.source, env.matcher);
				break;

			// index
			case "[":
				System.err.println("please finish implementing index access");
				// FIXME: read value until ']'
				break;

			// member access
			case ".": {
				System.err.println("please finish implementing member access");

				env.indexInSource = env.matcher.start() + 1;
				if (!env.matcher.find())
					throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);

				/*final String memberName = env.source.substring(env.indexInSource, env.matcher.start());

				// new OpMember(new GetVar(expression), env.source.substring(env.index, env.matcher.start()));
				// FIXME: finish OOP, support operations on above

				env.indexInSource = env.matcher.start() + 1;
				/*while (!env.matcher.group().equals(";")) {
					if (!env.matcher.find())
						throw new ClslCompilerException("unexpected EOF", env.indexInSource, env.source, env.matcher);
				}
				env.exec.add(new SetVar(expression, ClslCompiler.buildExpression(env.source.substring(env.indexInSource, env.matcher.start()))));*/
				parseNext(env);
				break;
			}

			case "-": {
				int start = env.matcher.start();
				if (!env.matcher.find())
					throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);

				switch (env.matcher.group()) {
					// "--"
					case "-":

						// --X
						if (expression.isEmpty()) {
							int varStart = env.matcher.start() + 1;
							if (!env.matcher.find())
								throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);

							String var = env.source.substring(varStart, env.matcher.start());
							assetVariableNameIsValid(env, var);
							env.exec.add(new OpDec(var, false));
						}

						// X--
						else
							env.exec.add(new OpDec(expression, true));
						break;

					// "-="
					case "=":
						env.exec.add(readValueSet(env, expression, MODE_SET_SUB));
						break;

					// FIXME: this is a simple subtract
					default:
						env.matcher.find(start);
						break;
				}
				break;
			}

			// "+"
			case "+": {
				int start = env.matcher.start();
				if (!env.matcher.find())
					throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);

				switch (env.matcher.group()) {
					// "++"
					case "+":

						// ++X
						if (expression.isEmpty()) {
							int varStart = env.matcher.start() + 1;
							if (!env.matcher.find())
								throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);

							String var = env.source.substring(varStart, env.matcher.start());
							assetVariableNameIsValid(env, var);
							env.exec.add(new OpInc(var, false));
						}

						// X++
						else
							env.exec.add(new OpInc(expression, true));
						break;

					// "+="
					case "=":
						env.exec.add(readValueSet(env, expression, MODE_SET_ADD));
						break;

					// FIXME: this is a simple add
					default:
						env.matcher.find(start);
						break;
				}
				break;
			}

			// "/"
			case "/": {
				int start = env.matcher.start();
				if (!env.matcher.find())
					throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);

				switch (env.matcher.group()) {
					// "/="
					case "=":
						env.exec.add(readValueSet(env, expression, MODE_SET_DIV));
						break;

					// FIXME: this is a simple div
					default:
						env.matcher.find(start);
						break;
				}
				break;
			}

			// "*"
			case "*": {
				int start = env.matcher.start();
				if (!env.matcher.find())
					throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);

				switch (env.matcher.group()) {
					// "*="
					case "=": {
						env.exec.add(readValueSet(env, expression, MODE_SET_MUL));
						break;
					}

					// FIXME: this is a simple multiply
					default:
						env.matcher.find(start);
						break;
				}
				break;
			}

			// "%"
			case "%": {
				int start = env.matcher.start();
				if (!env.matcher.find())
					throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);

				switch (env.matcher.group()) {
					// "%="
					case "=":
						env.exec.add(readValueSet(env, expression, MODE_SET_MOD));
						break;

					// FIXME: this is a simple modulus
					default:
						env.matcher.find(start);
						break;
				}
				break;
			}

			// "^"
			case "^": {
				int start = env.matcher.start();
				if (!env.matcher.find())
					throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);

				switch (env.matcher.group()) {
					// "^="
					case "=":
						env.exec.add(readValueSet(env, expression, MODE_SET_XOR));
						break;

					// FIXME: this is a simple xor
					default:
						env.matcher.find(start);
						break;
				}
				break;
			}

			// "&"
			case "&": {
				int start = env.matcher.start();
				if (!env.matcher.find())
					throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);

				switch (env.matcher.group()) {
					// "&="
					case "=":
						env.exec.add(readValueSet(env, expression, MODE_SET_BAND));
						break;

					// FIXME: this is a simple binary and
					default:
						env.matcher.find(start);
						break;
				}
				break;
			}

			// "|"
			case "|": {
				int start = env.matcher.start();
				if (!env.matcher.find())
					throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);

				switch (env.matcher.group()) {
					// "|="
					case "=":
						env.exec.add(readValueSet(env, expression, MODE_SET_BOR));
						break;

					// FIXME: this is a simple binary or
					default:
						env.matcher.find(start);
						break;
				}
				break;
			}

			// "<"
			case "<": {
				int start = env.matcher.start();
				if (!env.matcher.find())
					throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);

				switch (env.matcher.group()) {
					// "<<"
					case "<": {
						System.err.println("handle group in <<");
						if (!env.matcher.find())
							throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);

						switch (env.matcher.group()) {
							// <<=
							case "=":
								env.exec.add(readValueSet(env, expression, MODE_SET_BLS));
								break;
							default:
								// FIXME: this is a simple shift
								// left
								env.matcher.find(start);
								break;
						}
						break;
					}

					// FIXME: this is a simple less than or equals
					// "<="
					case "=":
						env.matcher.find(start);
						break;

					// FIXME: this is a simple less than
					default:
						env.matcher.find(start);
						break;
				}
				break;
			}

			// ">"
			case ">": {
				int start = env.matcher.start();
				if (!env.matcher.find())
					throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);

				switch (env.matcher.group()) {
					// ">>"
					case ">": {
						System.err.println("handle group in >>");
						if (!env.matcher.find())
							throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);

						switch (env.matcher.group()) {
							// >>=
							case "=":
								env.exec.add(readValueSet(env, expression, MODE_SET_BRS));
								break;
							default:
								// FIXME: this is a simple right
								// left
								env.matcher.find(start);
								break;
						}
						break;
					}

					// FIXME: this is a simple greater than or
					// equals
					// ">="
					case "=":
						env.matcher.find(start);
						break;

					// FIXME: this is a simple greater than
					default:
						env.matcher.find(start);
						break;
				}
				break;
			}

			// "="
			case "=":
				env.exec.add(readValueSet(env, expression, MODE_SET));
				break;

			default:
				throw new ClslCompilerException("expected expression", env.indexInSource, env.source, env.matcher);
		}
	}

	private static ExecutableValueChunk readValueSet(ClslCompilationEnv env, String varName, byte mode) {
		final ValueChunk val = ClslCompiler.readValueChunk(env.indexInSource, env.source, env.matcher, false);

		switch (mode) {
			case MODE_SET:
				return new SetVar(varName, val);
			case MODE_SET_ADD:
				return new SetVarAdd(varName, val);
			case MODE_SET_BAND:
				return new SetVarBinAnd(varName, val);
			case MODE_SET_BLS:
				return new SetVarShiftLeft(varName, val);
			case MODE_SET_BOR:
				return new SetVarBinOr(varName, val);
			case MODE_SET_BRS:
				return new SetVarShiftRight(varName, val);
			case MODE_SET_DIV:
				return new SetVarDiv(varName, val);
			case MODE_SET_MOD:
				return new SetVarModulus(varName, val);
			case MODE_SET_MUL:
				return new SetVarMul(varName, val);
			case MODE_SET_SUB:
				return new SetVarSub(varName, val);
			case MODE_SET_XOR:
				return new SetVarXor(varName, val);
			default:
				throw new ClslCompilerException("invalid mode: " + mode, env.indexInSource, env.source, env.matcher);
		}
	}

	private OperatorParser() {
	}
}