package com.etk2000.clsl.compiler;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslUtil;
import com.etk2000.clsl.chunk.ExecutableValueChunk;
import com.etk2000.clsl.chunk.FunctionCallChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.VariableAccess;
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

	// TODO: find a better way to cleanup after the function
	static void parseNext(ClslCompilationEnv env) {
		parseNextInner(env, true);
		if (env.currentValueAccess != null) {
			if (Clsl.doWarn)
				System.out.println("`currentValueAccess` has leaked: " + env.currentValueAccess);
			env.currentValueAccess = null;
		}
	}

	private static void parseNextInner(ClslCompilationEnv env, boolean requireExpressionAfter) {
		// FIXME: remove this var, replace with `env.currentValueAccess`
		final String expression = env.source.substring(env.indexInSource, env.matcher.start());

		if (!expression.isEmpty()) {
			if (!(env.currentValueAccess instanceof OpMember)) {
				assetVariableNameIsValid(env, expression);
				env.currentValueAccess = new GetVar(expression);
			}
		}

		switch (env.matcher.group()) {
			// function call
			case "(":
				env.exec.add(new FunctionCallChunk(env.popCurrentValueAccess(), ClslCompiler.readArguments(env).toArray(ClslUtil.CHUNK_VALUE)));
				consumeSemicolon(env);
				break;

			// index
			case "[":
				// member access
			case ".":
				parseVariableAccessOperator(env, requireExpressionAfter);
				break;

			case "-": {
				int start = env.matcher.start();
				if (!env.matcher.find())
					throw new ClslCompilerException(env, "expected expression");

				switch (env.matcher.group()) {
					// "--"
					case "-":

						// --X
						if (env.currentValueAccess == null) {
							env.indexInSource = env.matcher.start() + 1;
							if (!env.matcher.find())
								throw new ClslCompilerException(env, "expected expression");

							parseVariableAccessOperator(env, false);
							env.exec.add(new OpDec(env.popCurrentValueAccess(), false));
						}

						// X--
						else
							env.exec.add(new OpDec(env.popCurrentValueAccess(), true));
						break;

					// "-="
					case "=":
						env.exec.add(readValueSet(env, env.popCurrentValueAccess(), MODE_SET_SUB));
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
						if (env.currentValueAccess == null) {
							env.indexInSource = env.matcher.start() + 1;
							if (!env.matcher.find())
								throw new ClslCompilerException(env, "expected expression");

							parseVariableAccessOperator(env, false);
							env.exec.add(new OpInc(env.popCurrentValueAccess(), false));
						}

						// X++
						else
							env.exec.add(new OpInc(env.popCurrentValueAccess(), true));
						break;

					// "+="
					case "=":
						env.exec.add(readValueSet(env, env.popCurrentValueAccess(), MODE_SET_ADD));
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
						env.exec.add(readValueSet(env, env.popCurrentValueAccess(), MODE_SET_DIV));
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
						env.exec.add(readValueSet(env, env.popCurrentValueAccess(), MODE_SET_MUL));
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
						env.exec.add(readValueSet(env, env.popCurrentValueAccess(), MODE_SET_MOD));
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
						env.exec.add(readValueSet(env, env.popCurrentValueAccess(), MODE_SET_XOR));
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
						env.exec.add(readValueSet(env, env.popCurrentValueAccess(), MODE_SET_BAND));
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
						env.exec.add(readValueSet(env, env.popCurrentValueAccess(), MODE_SET_BOR));
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
								env.exec.add(readValueSet(env, env.popCurrentValueAccess(), MODE_SET_BLS));
								break;
							default:
								// FIXME: this is a simple shift left
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
								env.exec.add(readValueSet(env, env.popCurrentValueAccess(), MODE_SET_BRS));
								break;
							default:
								// FIXME: this is a simple right
								env.matcher.find(start);
								break;
						}
						break;
					}

					// FIXME: this is a simple greater than or equals
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
				env.exec.add(readValueSet(env, env.popCurrentValueAccess(), MODE_SET));
				break;

			default:
				if (requireExpressionAfter)
					throw new ClslCompilerException(env, "expected expression");
		}
	}

	static VariableAccess parseVariableAccessOperator(String source) {
		final ClslCompilationEnv helperEnv = new ClslCompilationEnv(source + ';');
		helperEnv.matcher.find();
		parseVariableAccessOperator(helperEnv, false);
		return helperEnv.popCurrentValueAccess();
	}

	static void parseVariableAccessOperator(ClslCompilationEnv env, boolean requireExpressionAfter) {
		// FIXME: remove this var, replace with `env.currentValueAccess`
		final String expression = env.source.substring(env.indexInSource, env.matcher.start());

		if (!expression.isEmpty()) {
			if (!(env.currentValueAccess instanceof OpMember)) {
				assetVariableNameIsValid(env, expression);
				env.currentValueAccess = new GetVar(expression);
			}
		}

		switch (env.matcher.group()) {
			// index
			case "[": {
				env.indexInSource = env.matcher.start() + 1;

				if (!env.matcher.find())
					throw new ClslCompilerException(env, "expected expression");

				env.currentValueAccess = new OpIndex(env.popCurrentValueAccess(), ClslCompiler.readChunk(env, ReadUntilToken.CLOSE_SQUARE_BRACKET));
				env.indexInSource = env.matcher.start() + 1;

				if (!env.matcher.find())
					throw new ClslCompilerException(env, "expected expression");

				parseNextInner(env, requireExpressionAfter);
				break;
			}

			// member access
			case ".": {
				env.indexInSource = env.matcher.start() + 1;
				if (!env.matcher.find())
					throw new ClslCompilerException(env, "expected expression");

				env.currentValueAccess = new OpMember(env.popCurrentValueAccess(), env.source.substring(env.indexInSource, env.matcher.start()));
				parseNextInner(env, requireExpressionAfter);
				break;
			}

			case ";":
				if (requireExpressionAfter)
					throw new ClslCompilerException(env, "expected expression");
		}
	}

	private static ExecutableValueChunk readValueSet(ClslCompilationEnv env, VariableAccess variableAccess, byte mode) {
		final ValueChunk val = ClslCompiler.readChunk(env, null);

		switch (mode) {
			case MODE_SET:
				return new SetVar(variableAccess, val);
			case MODE_SET_ADD:
				return new SetVarAdd(variableAccess, val);
			case MODE_SET_BAND:
				return new SetVarBinAnd(variableAccess, val);
			case MODE_SET_BLS:
				return new SetVarShiftLeft(variableAccess, val);
			case MODE_SET_BOR:
				return new SetVarBinOr(variableAccess, val);
			case MODE_SET_BRS:
				return new SetVarShiftRight(variableAccess, val);
			case MODE_SET_DIV:
				return new SetVarDiv(variableAccess, val);
			case MODE_SET_MOD:
				return new SetVarModulus(variableAccess, val);
			case MODE_SET_MUL:
				return new SetVarMul(variableAccess, val);
			case MODE_SET_SUB:
				return new SetVarSub(variableAccess, val);
			case MODE_SET_XOR:
				return new SetVarXor(variableAccess, val);
			default:
				throw new ClslCompilerException(env, "invalid mode: " + mode);
		}
	}

	private OperatorParser() {
	}
}