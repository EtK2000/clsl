package com.etk2000.clsl.compiler;

import com.etk2000.clsl.ClslUtil;
import com.etk2000.clsl.chunk.ExecutableValueChunk;
import com.etk2000.clsl.chunk.FunctionCallChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.op.OpDec;
import com.etk2000.clsl.chunk.op.OpInc;
import com.etk2000.clsl.chunk.op.OpIndex;
import com.etk2000.clsl.chunk.op.OpMember;
import com.etk2000.clsl.chunk.variable.GetVar;
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
			throw new ClslCompilerException(env, "invalid variable name: `" + variableName + '`');
	}

	private static void consumeSemicolon(ClslCompilationEnv env) {
		if (!env.matcher.find())
			throw new ClslCompilerException(env, "error: expected `;`");
		if (!env.matcher.group().equals(";"))
			throw new ClslCompilerException(env, "error: expected `;`");
	}

	static void parseNext(ClslCompilationEnv env) {
		// FIXME: remove this var, replace with `env.currentValueAccess`
		final String expression = env.source.substring(env.indexInSource, env.matcher.start());

		if (!expression.isEmpty()) {
			assetVariableNameIsValid(env, expression);
			env.currentValueAccess = new GetVar(expression);
		}

		switch (env.matcher.group()) {
			// function call
			case "(":
				env.exec.add(new FunctionCallChunk(env.currentValueAccess, ClslCompiler.readArguments(env).toArray(ClslUtil.CHUNK_VALUE)));
				consumeSemicolon(env);
				break;

			// index
			case "[": {
				env.currentValueAccess = new OpIndex(env.currentValueAccess, ClslCompiler.readValueChunk(env, false));

				System.err.println("please finish implementing index access");
				parseNext(env);
				// FIXME: read value until ']'
				break;
			}

			// member access
			case ".": {
				System.err.println("please finish implementing member access");

				env.indexInSource = env.matcher.start() + 1;
				if (!env.matcher.find())
					throw new ClslCompilerException(env, "expected expression");

				env.currentValueAccess = new OpMember(env.currentValueAccess, env.source.substring(env.indexInSource, env.matcher.start()));

				env.indexInSource = env.matcher.start() + 1;
				if (!env.matcher.find())
					throw new ClslCompilerException(env, "expected expression");

				parseNext(env);
				break;
			}

			case "-": {
				int start = env.matcher.start();
				if (!env.matcher.find())
					throw new ClslCompilerException(env, "expected expression");

				switch (env.matcher.group()) {
					// "--"
					case "-":

						// --X
						if (expression.isEmpty()) {
							int varStart = env.matcher.start() + 1;
							if (!env.matcher.find())
								throw new ClslCompilerException(env, "expected expression");

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
					throw new ClslCompilerException(env, "expected expression");

				switch (env.matcher.group()) {
					// "++"
					case "+":

						// ++X
						if (expression.isEmpty()) {
							int varStart = env.matcher.start() + 1;
							if (!env.matcher.find())
								throw new ClslCompilerException(env, "expected expression");

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
					throw new ClslCompilerException(env, "expected expression");

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
					throw new ClslCompilerException(env, "expected expression");

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
					throw new ClslCompilerException(env, "expected expression");

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
					throw new ClslCompilerException(env, "expected expression");

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
					throw new ClslCompilerException(env, "expected expression");

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
					throw new ClslCompilerException(env, "expected expression");

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
					throw new ClslCompilerException(env, "expected expression");

				switch (env.matcher.group()) {
					// "<<"
					case "<": {
						System.err.println("handle group in <<");
						if (!env.matcher.find())
							throw new ClslCompilerException(env, "expected expression");

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
					throw new ClslCompilerException(env, "expected expression");

				switch (env.matcher.group()) {
					// ">>"
					case ">": {
						System.err.println("handle group in >>");
						if (!env.matcher.find())
							throw new ClslCompilerException(env, "expected expression");

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
				throw new ClslCompilerException(env, "expected expression");
		}
	}

	private static ExecutableValueChunk readValueSet(ClslCompilationEnv env, String varName, byte mode) {
		final ValueChunk val = ClslCompiler.readValueChunk(env, false);

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
				throw new ClslCompilerException(env, "invalid mode: " + mode);
		}
	}

	private OperatorParser() {
	}
}