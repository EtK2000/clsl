package com.etk2000.clsl.compiler;

import com.etk2000.clsl.Clsl;
import com.etk2000.clsl.ClslCode;
import com.etk2000.clsl.ClslUtil;
import com.etk2000.clsl.Group;
import com.etk2000.clsl.StringBuilderPoolable;
import com.etk2000.clsl.ValueType;
import com.etk2000.clsl.chunk.DoWhileChunk;
import com.etk2000.clsl.chunk.ExecutableChunk;
import com.etk2000.clsl.chunk.ForChunk;
import com.etk2000.clsl.chunk.FunctionCallChunk;
import com.etk2000.clsl.chunk.FunctionalChunk;
import com.etk2000.clsl.chunk.IfChunk;
import com.etk2000.clsl.chunk.IncludeChunk;
import com.etk2000.clsl.chunk.IncludeExternalChunk;
import com.etk2000.clsl.chunk.ReturnChunk;
import com.etk2000.clsl.chunk.ValueChunk;
import com.etk2000.clsl.chunk.WhileChunk;
import com.etk2000.clsl.chunk.op.OpAdd;
import com.etk2000.clsl.chunk.op.OpAnd;
import com.etk2000.clsl.chunk.op.OpBinAnd;
import com.etk2000.clsl.chunk.op.OpBinOr;
import com.etk2000.clsl.chunk.op.OpDec;
import com.etk2000.clsl.chunk.op.OpDivide;
import com.etk2000.clsl.chunk.op.OpEquals;
import com.etk2000.clsl.chunk.op.OpInc;
import com.etk2000.clsl.chunk.op.OpLessThan;
import com.etk2000.clsl.chunk.op.OpMember;
import com.etk2000.clsl.chunk.op.OpModulus;
import com.etk2000.clsl.chunk.op.OpMultiply;
import com.etk2000.clsl.chunk.op.OpNot;
import com.etk2000.clsl.chunk.op.OpOr;
import com.etk2000.clsl.chunk.op.OpShiftLeft;
import com.etk2000.clsl.chunk.op.OpShiftRight;
import com.etk2000.clsl.chunk.op.OpSubtract;
import com.etk2000.clsl.chunk.op.OpTernary;
import com.etk2000.clsl.chunk.op.OpXor;
import com.etk2000.clsl.chunk.value.ConstArrayChunk;
import com.etk2000.clsl.chunk.value.ConstCharChunk;
import com.etk2000.clsl.chunk.value.ConstDoubleChunk;
import com.etk2000.clsl.chunk.value.ConstFloatChunk;
import com.etk2000.clsl.chunk.value.ConstIntChunk;
import com.etk2000.clsl.chunk.value.ConstLongChunk;
import com.etk2000.clsl.chunk.variable.GetVar;
import com.etk2000.clsl.chunk.variable.definition.DefineArray;
import com.etk2000.clsl.chunk.variable.definition.DefineChar;
import com.etk2000.clsl.chunk.variable.definition.DefineDouble;
import com.etk2000.clsl.chunk.variable.definition.DefineFloat;
import com.etk2000.clsl.chunk.variable.definition.DefineInt;
import com.etk2000.clsl.chunk.variable.definition.DefineLong;
import com.etk2000.clsl.chunk.variable.definition.DefineStruct;
import com.etk2000.clsl.chunk.variable.set.SetVar;
import com.etk2000.clsl.exception.ClslCompilerException;
import com.etk2000.clsl.value.ClslValue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClslCompiler {
	private static final String BLOCK_CHARS = ";(){}[]<>^&|!=+-/*%.,?:'\"";
	static final Pattern SYNTAX_PATTERN = Pattern.compile("[ " + Pattern.quote(BLOCK_CHARS) + ']');

	@Deprecated
	// switch to readValueChunk
	static ValueChunk buildExpression(String query) {
		try {
			// LOW: change sb length to var name length
			List<Object> ss = new ArrayList<>();
			ss.add(ExpressionType.PARENTHESIS_OPEN);

			try (StringBuilderPoolable sb = new StringBuilderPoolable()) {
				char c;
				boolean op = false;
				for (int i = 0, j, k; i < query.length(); ++i) {
					op = true;
					switch (c = query.charAt(i)) {
						case '"':
							// read whole string
							j = i;
							while (true) {
								j = k = query.indexOf('"', j + 1);
								if (j == -1)
									throw new IllegalArgumentException("missing terminating \" character");
								while (query.charAt(k) == '\\')
									--k;
								if ((j - k) % 2 == 0)
									break;
							}

							op = false;
							sb.append(ClslUtil.unescape(query.substring(i, j + 1)));
							i = j;
							break;
						case '\'': {
							// read whole char
							j = i;
							while (true) {
								j = k = query.indexOf('\'', j + 1);
								if (j == -1)
									throw new IllegalArgumentException("missing terminating ' character");
								while (query.charAt(k) == '\\')
									--k;
								if ((j - k) % 2 == 0)
									break;
							}

							// ensure char is 1 character
							String une = ClslUtil.unescape(query.substring(i, j + 1));
							if (une.isEmpty())
								throw new IllegalArgumentException("empty character constant");
							if (une.length() > 1)
								throw new IllegalArgumentException("multi-character character constant");

							// append
							op = false;
							sb.append(une);
							i = j;
							break;
						}
						case '(':
							ss.add(ExpressionType.PARENTHESIS_OPEN);
							break;
						case ')':
							ss.add(ExpressionType.PARENTHESIS_CLOSE);
							break;
						case ',':
							ss.add(ExpressionType.COMMA);
							break;
						case '%':
							if (query.charAt(i + 1) == '=') {
								ss.add(ExpressionType.MODULUS_EQUAL);
								++i;
							}
							else
								ss.add(ExpressionType.MODULUS);
							break;
						case '/':
							if (query.charAt(i + 1) == '=') {
								ss.add(ExpressionType.DIVIDE_EQUAL);
								++i;
							}
							else
								ss.add(ExpressionType.DIVIDE);
							break;
						case '*':
							if (query.charAt(i + 1) == '=') {
								ss.add(ExpressionType.MULTIPLY_EQUAL);
								++i;
							}
							else
								ss.add(ExpressionType.MULTIPLY);
							break;
						case '+':
							if (query.charAt(i + 1) == '=') {
								ss.add(ExpressionType.ADD_EQUAL);
								++i;
							}
							else if (query.charAt(i + 1) == '+') {
								ss.add(ExpressionType.INCREMENT);
								++i;
							}
							else
								ss.add(ExpressionType.ADD);
							break;
						case '-':
							if (query.charAt(i + 1) == '=') {
								ss.add(ExpressionType.SUBTRACT_EQUAL);
								++i;
							}
							else if (query.charAt(i + 1) == '-') {
								ss.add(ExpressionType.DECREMENT);
								++i;
							}
							else
								ss.add(ExpressionType.SUBTRACT);
							break;
						case '!':
							if (query.charAt(i + 1) == '=') {
								ss.add(ExpressionType.NOT_EQUAL);
								++i;
							}
							else
								ss.add(ExpressionType.NOT);
							break;
						case '<':
							if (query.charAt(i + 1) == '<') {
								if (query.charAt(i + 1) == '=') {
									ss.add(ExpressionType.SHIFT_LEFT_EQUAL);
									i += 2;
								}
								else {
									ss.add(ExpressionType.SHIFT_LEFT);
									++i;
								}
							}
							else if (query.charAt(i + 1) == '=') {
								ss.add(ExpressionType.LESS_THAN_OR_EQUAL);
								++i;
							}
							else
								ss.add(ExpressionType.LESS_THAN);
							break;
						case '>':
							if (query.charAt(i + 1) == '>') {
								if (query.charAt(i + 1) == '=') {
									ss.add(ExpressionType.SHIFT_RIGHT_EQUAL);
									i += 2;
								}
								else {
									ss.add(ExpressionType.SHIFT_RIGHT);
									++i;
								}
							}
							else if (query.charAt(i + 1) == '=') {
								ss.add(ExpressionType.MORE_THAN_OR_EQUAL);
								++i;
							}
							else
								ss.add(ExpressionType.MORE_THAN);
							break;
						case '|':
							if (query.charAt(i + 1) == '|') {
								ss.add(ExpressionType.OR);
								++i;
							}
							else if (query.charAt(i + 1) == '=') {
								ss.add(ExpressionType.BIN_OR_EQUAL);
								++i;
							}
							else
								ss.add(ExpressionType.BIN_OR);
							break;
						case '&':
							if (query.charAt(i + 1) == '&') {
								ss.add(ExpressionType.AND);
								++i;
							}
							else if (query.charAt(i + 1) == '=') {
								ss.add(ExpressionType.BIN_AND_EQUAL);
								++i;
							}
							else
								ss.add(ExpressionType.BIN_AND);
							break;
						case '=':
							if (query.charAt(i + 1) == '=') {
								ss.add(ExpressionType.EQUAL_TO);
								++i;
							}
							else
								ss.add(ExpressionType.SET);
							break;
						case '^':
							if (query.charAt(i + 1) == '=') {
								ss.add(ExpressionType.XOR_EQUAL);
								++i;
							}
							else
								ss.add(ExpressionType.XOR);
							break;
						case '?':
							ss.add(ExpressionType.QUESTION);
							break;
						case ':':
							ss.add(ExpressionType.ELSE);
							break;
						default:
							op = false;
							sb.append(c);
							break;
					}
					if (op && sb.length() > 0) {
						ss.add(ss.size() - 1, sb.toString());
						sb.setLength(0);
					}
				}
				if (sb.length() > 0)
					ss.add(sb.toString());
			}

			ss.add(ExpressionType.PARENTHESIS_CLOSE);
			return getBlock(ss, 0).a;
		}
		catch (Exception e) {
			throw new ClslCompilerException("invalid eval query: " + query + "; " + e.getMessage());
		}
	}

	public static ClslCode compile(String code, boolean allowFunctions) throws ClslCompilerException {
		return new ClslCode(compileInternal(code, allowFunctions));
	}

	public static ClslCode compileAndOptimize(String code, boolean allowFunctions, int passes) {
		final ClslCode res = compile(code, allowFunctions);
		Clsl.optimize(res, passes);
		return res;
	}

	// HIGH: allow variable definition using expressions
	private static List<ExecutableChunk> compileInternal(String code, boolean allowFunctions) throws ClslCompilerException {
		// TODO: use StringBuilder until we need String
		String res = code + '\n';

		// remove line comments
		// TODO: move this into whitespace for loop
		for (int i; (i = res.indexOf("//")) > -1; )
			res = res.substring(0, i) + res.substring(res.indexOf('\n', i));

		// parse preprocessor and #includes here whilst we still have linebreaks
		// TODO: currently assumes at the top of the file
		// TODO: remove this line from further execution
		final List<ExecutableChunk> includesExec = new ArrayList<>();
		for (String line : res.split("\\s*\n\\s*")) {
			if ((line = line.trim()).isEmpty() || line.charAt(0) != '#')
				continue;

			// FIXME: only run preprocessor
			if (line.startsWith("#include")) {
				int s = line.indexOf('<');
				if (s != -1) {
					int e = line.indexOf('>', ++s);
					if (e != -1)
						includesExec.add(new IncludeChunk(line.substring(s, e)));
					else
						throw new ClslCompilerException("cannot compile line: " + line);
				}
				else if ((s = line.indexOf('"')) != -1) {
					int e = line.indexOf('"', ++s);
					if (e != -1)
						includesExec.add(new IncludeExternalChunk(line.substring(s, e)));
					else
						throw new ClslCompilerException("cannot compile line: " + line);
				}
				else
					throw new ClslCompilerException("cannot compile line: " + line);
			}
			else
				System.out.println("ignoring line: " + line);
		}

		// remove whitespaces and switch linebreaks with spaces
		{
			try (StringBuilderPoolable sb = new StringBuilderPoolable(res.length())) {
				byte inq = 0, s = 0;
				int i = 0;
				while (i < res.length() && Character.isWhitespace(res.charAt(i)))
					++i;
				for (; i < res.length(); ++i) {
					// not escaped quote
					if (res.charAt(i) == '\'' && s % 2 == 0) {
						if (inq == 4)
							sb.deleteLast();
						inq ^= 1;
						s = 0;
					}
					else if (res.charAt(i) == '"' && s % 2 == 0) {
						if (inq == 4)
							sb.deleteLast();
						inq ^= 2;
						s = 0;
					}
					else if (inq == 0 || inq == 4 || inq == 8) {
						if (Character.isWhitespace(res.charAt(i))) {
							if (inq == 0)
								inq = 4;
							else
								continue;
						}
						else if (BLOCK_CHARS.indexOf(res.charAt(i)) != -1) {
							if (inq == 4)
								sb.deleteLast();
							inq = 8;
						}
						else if (inq != 0)
							inq = 0;
					}
					else if ((inq == 1 || inq == 2) && res.charAt(i) == '\\')
						s++;

					if (res.charAt(i) == '\n' || res.charAt(i) == '\r')
						sb.append(' ');
					else
						sb.append(res.charAt(i));
				}
				res = sb.toString();
			}
		}

		// remove block comments, has to be here because . doesn't catch \n
		res = res.replaceAll("/\\*.*?\\*/", "");

		final ClslCompilationEnv env = new ClslCompilationEnv(res);
		env.exec.addAll(includesExec);

		// TODO: allow hex, octal, and binary values
		// break into chunks
		boolean _const = false, _unsigned = false;

		while (env.matcher.find()) {
			// FIXME: remove these vars, have them in env
			final String expression = res.substring(env.indexInSource, env.matcher.start()), group = env.matcher.group();

			if (expression.isEmpty() && env.matcher.group().equals(";")) {
				env.indexInSource = env.matcher.start() + 1;
				continue;// NOP, x++;, or something similar that can be ignored
			}

			switch (expression) {
				// FIXME: things to implement correctly
				// case "#define":
				case "#include": {
					boolean builtin = false;
					if (group.equals("<"))
						builtin = true;
					else if (!group.equals("\""))
						throw new ClslCompilerException("#include expects \"FILENAME\" or <FILENAME>", env.indexInSource, res, env.matcher);
					String terminating = builtin ? ">" : "\"";

					env.indexInSource = env.matcher.start() + 1;
					do {
						if (!env.matcher.find())
							throw new ClslCompilerException("missing terminating " + terminating + " character", env.indexInSource, res, env.matcher);
					} while (!env.matcher.group().equals(terminating));
					// FIXME: include code here
					System.err.println("include " + res.substring(env.indexInSource, env.matcher.start()) + (builtin ? " builtin" : ""));
					break;
				}

				// qualifiers
				case "const": {
					// FIXME: read var but set it to const
					System.err.println("validate group: const");
					System.err.println("please implement const!!!");
					_const = true;
					break;
				}
				case "unsigned": {
					// FIXME: read var but set it to unsigned
					_unsigned = true;
					System.err.println("validate group: unsigned");
					System.err.println("please implement unsigned!!!");
					break;
				}

				// variable definitions
				case "char":
					System.err.println("validate group: char");
					readDefinition(ValueType.CHAR, allowFunctions, env.exec, res, env.matcher);
					_const = _unsigned = false;
					break;
				case "double":
					System.err.println("validate group: double");
					readDefinition(ValueType.DOUBLE, allowFunctions, env.exec, res, env.matcher);
					_const = _unsigned = false;
					break;
				case "float":
					System.err.println("validate group: float");
					readDefinition(ValueType.FLOAT, allowFunctions, env.exec, res, env.matcher);
					_const = _unsigned = false;
					break;
				case "int":
					System.err.println("validate group: int");
					readDefinition(ValueType.INT, allowFunctions, env.exec, res, env.matcher);
					_const = _unsigned = false;
					break;
				case "long":
					System.err.println("validate group: long");
					readDefinition(ValueType.LONG, allowFunctions, env.exec, res, env.matcher);
					_const = _unsigned = false;
					break;
				case "struct":
					System.err.println("validate group: struct");
					// LOW: support "<type> <var>" syntax
					readDefinition(ValueType.STRUCT, allowFunctions, env.exec, res, env.matcher);
					_const = _unsigned = false;
					break;
				case "void":
					System.err.println("validate group: void");
					env.indexInSource = env.matcher.start() + 1;
					if (!env.matcher.find())
						throw new ClslCompilerException("expected variable name", env.indexInSource, res, env.matcher);

					if (_unsigned)
						throw new ClslCompilerException("both `unsigned` and `void` in declaration specifiers", env.indexInSource, expression, env.matcher);

					String varName = res.substring(env.indexInSource, env.matcher.start());
					if (env.matcher.group().equals("(")) {
						if (!allowFunctions)
							throw new ClslCompilerException("cannot define a function here", env.indexInSource, res, env.matcher);
						env.exec.add(readFunction(ValueType.VOID, varName, res, env.matcher));
						_const = false;
					}
					else
						throw new ClslCompilerException("variable or field `" + varName + "` declared void", env.indexInSource, res, env.matcher);
					break;

				// flow control
				case "do": {
					switch (group) {
						case " ":
						case ";":
						case "+":
						case "-":
						case "*":
						case "(":
						case "{":
							break;
						default:
							throw new ClslCompilerException("expected expression before `" + group + "` token", env.indexInSource, res, env.matcher);
					}
					ExecutableChunk[] effect = readEffect(res, env.matcher, group.equals(" ") || group.equals("{") ? "" : group);

					if (group.equals("{") && (!env.matcher.find() || !env.matcher.group().equals("}")))
						throw new ClslCompilerException("missing curly bracket before while", env.indexInSource, res, env.matcher);

					env.indexInSource = env.matcher.start() + 1;
					if (!env.matcher.find() || !res.substring(env.indexInSource, env.matcher.start()).equals("while"))
						throw new ClslCompilerException("expected while", env.indexInSource, res, env.matcher);

					env.exec.add(new DoWhileChunk(effect, readValueChunk(env.indexInSource, res, env.matcher, false)));
					if (!env.matcher.find() || !env.matcher.group().equals(";"))
						throw new ClslCompilerException("expected `;`", env.indexInSource, res, env.matcher);

					break;
				}
				case "else":
					System.err.println("validate group: else");
					env.indexInSource = env.matcher.start() + 1;
					if (!env.matcher.find())
						throw new ClslCompilerException("expected expression", env.indexInSource, res, env.matcher);

					if (res.substring(env.indexInSource, env.matcher.start()).equals("if")) {
						if (!(env.exec.get(env.exec.size() - 1) instanceof IfChunk))
							throw new ClslCompilerException("else-if only allowed after if", env.indexInSource, res, env.matcher);

						((IfChunk) env.exec.get(env.exec.size() - 1)).addElse(readCauseEffect(env.indexInSource, res, env.matcher));
					}
					else {
						env.matcher.find(env.indexInSource - 1);
						((IfChunk) env.exec.get(env.exec.size() - 1)).addElse(new Group<>(null, readEffect(res, env.matcher, "")));
					}
					break;
				case "for":
					System.err.println("validate group: for");
					env.exec.add(new ForChunk(readExecutable(res, env.matcher, true), readValueChunk(env.indexInSource, res, env.matcher, false), readExecutable(res, env.matcher, false),
							readEffect(res, env.matcher, "")));
					break;
				case "if": {
					System.err.println("validate group: if");
					Group<ValueChunk, ExecutableChunk[]> g = readCauseEffect(env.indexInSource, res, env.matcher);
					env.exec.add(new IfChunk(g.a, g.b));
					break;
				}
				case "return":
					switch (group) {
						case " ":
						case ";":
						case "+":
						case "-":
						case "*":
						case "(":
							break;
						default:
							throw new ClslCompilerException("expected expression before `" + group + "` token", env.indexInSource, res, env.matcher);
					}
					// FIXME: ensure return is in function and can be casted to
					// return type
					env.exec.add(new ReturnChunk(group.equals(";") ? null : readValueChunk(env.indexInSource, res, env.matcher, false, group.equals(" ") ? "" : group)));
					break;
				case "while": {
					System.err.println("validate group: while");
					Group<ValueChunk, ExecutableChunk[]> g = readCauseEffect(env.indexInSource, res, env.matcher);
					env.exec.add(new WhileChunk(g.a, g.b));
					break;
				}

				// variable access or function call
				default:
					OperatorParser.parseNext(env);
					break;
			}

			env.indexInSource = env.matcher.start() + 1;
		}

		return env.exec;
	}

	// FIXME: finish order of operations
	// FIXME: add support for Set___Var___ operations
	// HIGH: add support for <<= >>= ?: * / + - *= /= += -= &= etc
	private static IntGroup<ValueChunk> getBlock(List<Object> arr, int start) {
		if (arr.get(start) != ExpressionType.PARENTHESIS_OPEN && arr.get(start) != ExpressionType.COMMA)
			throw new ClslCompilerException("expected parentheses; near: " + arr.get(start));

		final List<Object> res = new ArrayList<>();
		final List<ValueChunk> args = new ArrayList<>();
		int index = 1;
		IntGroup<ValueChunk> block;

		while (arr.get(start + index) != ExpressionType.PARENTHESIS_CLOSE && arr.get(start + index) != ExpressionType.COMMA) {
			if (arr.get(start + index) == ExpressionType.PARENTHESIS_OPEN) {
				if (!res.isEmpty() && res.get(res.size() - 1) instanceof GetVar) {// function call

					// parse parameters of any are supplied
					if (arr.get(start + index + 1) != ExpressionType.PARENTHESIS_CLOSE) {
						do {
							args.add((block = getBlock(arr, start + index)).a);
							index += block.b;
						} while (arr.get(start + index) == ExpressionType.COMMA);
					}

					res.add(new FunctionCallChunk(((GetVar) res.remove(res.size() - 1)).name, args.toArray(ClslUtil.CHUNK_VALUE)));
					args.clear();
					++index;
				}
				else {
					res.add((block = getBlock(arr, start + index)).a);
					index += block.b + 1;
				}
			}
			else {
				if (arr.get(start + index) instanceof ExpressionType)
					res.add(arr.get(start + index));
				else
					res.add(toValue((String) arr.get(start + index)));
				++index;
			}
		}

		// first pass (i.e. decrement, increment)
		for (int i = 0; i < res.size(); ++i) {
			if (res.get(i) == ExpressionType.DECREMENT) {
				// x--
				if (i > 0 && res.get(i - 1) instanceof ValueChunk)
					res.set(--i, new OpDec(((GetVar) res.remove(i)).name, true));
					// --x
				else
					res.set(i, new OpDec(((GetVar) res.remove(i + 1)).name, false));
			}
			else if (res.get(i) == ExpressionType.INCREMENT) {
				// x++
				if (i > 0 && res.get(i - 1) instanceof ValueChunk)
					res.set(--i, new OpInc(((GetVar) res.remove(i)).name, true));
					// ++x
				else
					res.set(i, new OpInc(((GetVar) res.remove(i + 1)).name, false));
			}
		}

		// second pass (i.e. not)
		for (int i = 0; i < res.size(); ++i) {
			// !x
			if (res.get(i) == ExpressionType.NOT)
				res.set(--i, new OpNot((ValueChunk) res.remove(i + 1)));
		}

		// third pass (i.e. division, modulus, multiplication)
		for (int i = 0; i < res.size(); ++i) {
			// x / y
			if (res.get(i) == ExpressionType.DIVIDE)
				res.set(--i, new OpDivide((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1)));
			// x % y
			if (res.get(i) == ExpressionType.MODULUS)
				res.set(--i, new OpModulus((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1)));
				// x * y
			else if (res.get(i) == ExpressionType.MULTIPLY)
				res.set(--i, new OpMultiply((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1)));
		}

		// fourth pass (i.e. addition, subtraction)
		for (int i = 0; i < res.size(); ++i) {
			if (res.get(i) == ExpressionType.ADD) {
				// +x
				if (i == 0)
					res.set(0, new OpAdd(new ConstIntChunk(0), (ValueChunk) res.remove(i + 1)));

					// x + y
				else
					res.set(--i, new OpAdd((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1)));
			}

			else if (res.get(i) == ExpressionType.SUBTRACT) {
				// -x
				if (i == 0)
					res.set(0, new OpSubtract(new ConstIntChunk(0), (ValueChunk) res.remove(i + 1)));

					// x - y
				else
					res.set(--i, new OpSubtract((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1)));
			}
		}

		// fifth pass (i.e. shift left, shift right)
		for (int i = 0; i < res.size(); ++i) {
			// x << y
			if (res.get(i) == ExpressionType.SHIFT_LEFT)
				res.set(--i, new OpShiftLeft((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1)));
				// x >> y
			else if (res.get(i) == ExpressionType.SHIFT_RIGHT)
				res.set(--i, new OpShiftRight((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1)));
		}

		// sixth pass
		// (i.e. less, less equals, more, more equals)
		for (int i = 0; i < res.size(); ++i) {
			if (res.get(i) instanceof ExpressionType) {
				// x < y
				if (res.get(i) == ExpressionType.LESS_THAN)
					res.set(--i, new OpLessThan((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1), false));
					// x <= y
				else if (res.get(i) == ExpressionType.LESS_THAN_OR_EQUAL)
					res.set(--i, new OpLessThan((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1), true));
					// x > y
				else if (res.get(i) == ExpressionType.MORE_THAN)
					res.set(--i, new OpLessThan((ValueChunk) res.remove(i + 2), (ValueChunk) res.remove(i), false));
					// x >= y
				else if (res.get(i) == ExpressionType.MORE_THAN_OR_EQUAL)
					res.set(--i, new OpLessThan((ValueChunk) res.remove(i + 2), (ValueChunk) res.remove(i), true));
			}
		}

		// seventh pass (i.e. equals, not equals)
		for (int i = 0; i < res.size(); ++i) {
			if (res.get(i) instanceof ExpressionType) {
				// x == y
				if (res.get(i) == ExpressionType.EQUAL_TO)
					res.set(--i, new OpEquals((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1), false));
					// x != y
				else if (res.get(i) == ExpressionType.NOT_EQUAL)
					res.set(--i, new OpEquals((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1), true));
			}
		}

		// eighth pass (i.e. bin_and)
		for (int i = 0; i < res.size(); ++i) {
			// x & y
			if (res.get(i) == ExpressionType.BIN_AND)
				res.set(--i, new OpBinAnd((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1)));
		}

		// ninth pass (i.e. bin_xor)
		for (int i = 0; i < res.size(); ++i) {
			// x ^ y
			if (res.get(i) == ExpressionType.XOR)
				res.set(--i, new OpXor((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1)));
		}

		// tenth pass (i.e. bin_or)
		for (int i = 0; i < res.size(); ++i) {
			// x | y
			if (res.get(i) == ExpressionType.BIN_OR)
				res.set(--i, new OpBinOr((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1)));
		}

		// eleventh pass (i.e. and)
		for (int i = 0; i < res.size(); ++i) {
			// x && y
			if (res.get(i) == ExpressionType.AND)
				res.set(--i, new OpAnd((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1)));
		}

		// twelfth pass (i.e. or)
		for (int i = 0; i < res.size(); ++i) {
			// x || y
			if (res.get(i) == ExpressionType.OR)
				res.set(--i, new OpOr((ValueChunk) res.remove(i), (ValueChunk) res.remove(i + 1)));
		}

		// FIXME: test this
		// thirteenth pass (i.e ?:)
		for (int i = 0; i < res.size(); ++i) {
			// x ? y : z
			if (res.get(i) == ExpressionType.QUESTION) {
				if (res.get(i + 2) == ExpressionType.ELSE) {
					res.remove(i + 2);
					res.remove(i);
					res.set(--i, new OpTernary((ValueChunk) res.get(i), (ValueChunk) res.remove(i + 1), (ValueChunk) res.remove(i + 1)));
				}
				else
					throw new ClslCompilerException("expected ':' near '?'");
			}
		}

		// fourteenth pass (i.e. assignment)
		for (int i = 0; i < res.size(); ++i) {
			// x = y
			if (res.get(i) == ExpressionType.SET)
				res.set(--i, new SetVar(((GetVar) res.remove(i)).name, (ValueChunk) res.remove(i + 1)));
		}

		if (res.size() != 1)
			throw new ClslCompilerException("invalid cause: " + res);
		return IntGroup.make((ValueChunk) res.get(0), index);
	}

	static List<ValueChunk> readArguments(String res, Matcher m) {
		List<ValueChunk> arr = new ArrayList<>();

		int i = m.start() + 1;
		if (!m.find())
			throw new ClslCompilerException("expected expression", i, res, m);

		int blocks = 1;
		boolean inChr = false, inStr = false;
		ValueChunk wip = null;
		out:
		do {
			switch (m.group()) {
				case ";":
					throw new ClslCompilerException("illegal token", i, res, m);
				case ",":
					if (!inChr && !inStr) {
						if (i == m.start()) {
							arr.add(wip);
							wip = null;
						}
						else
							arr.add(buildExpression(res.substring(i, m.start())));
						i = m.start() + 1;
					}
					break;
				case "'":
					if (!inStr)
						inChr = !inChr;// TODO: allow escaping
					break;
				case "\"":
					if (!inChr)
						inStr = !inStr;// TODO: allow escaping
					break;
				case "(":
					if (!inChr && !inStr) {
						if (BLOCK_CHARS.indexOf(res.charAt(m.start() - 1)) != -1)
							++blocks;// just parenthesis
						else {
							wip = new FunctionCallChunk(res.substring(i, m.start()), readArguments(res, m).toArray(ClslUtil.CHUNK_VALUE));
							i = m.start() + 1;
						}
					}
					break;
				case ")":
					if (!inChr && !inStr) {
						if (--blocks == 0)
							break out;
					}
					break;
			}
		} while (m.find());

		// add the last arg (if it exists)
		if (i != m.start())
			arr.add(buildExpression(res.substring(i, m.start())));
		else if (wip != null)
			arr.add(wip);
		return arr;
	}

	static ValueChunk readValueChunk(int i, String res, Matcher m, boolean untilComma) {
		return readValueChunk(i, res, m, untilComma, "");
	}

	private static ValueChunk readValueChunk(int i, String res, Matcher m, boolean untilComma, String prefix) {
		if (!untilComma) {
			i = m.start() + 1;
			if (!m.find())
				throw new ClslCompilerException("expected expression", i, res, m);
		}

		int blocks = prefix.equals("(") ? 2 : 1;
		out:
		do {
			switch (m.group()) {
				case ";":
					if (blocks > 1)
						throw new ClslCompilerException("illegal token", i, res, m);
					break out;// assume it's a for(;X;) or variable definition
				case "(":
					++blocks;
					break;
				case ")":
					if (--blocks == 0 && !untilComma)
						break out;
					break;
				case ",":
					if (blocks == 1 && untilComma)
						break out;
					break;
			}
		} while (m.find());

		if (i == m.start())
			return null;
		return buildExpression(prefix + res.substring(i, m.start()));
	}

	@Deprecated
	private static Group<ValueChunk, ExecutableChunk[]> readCauseEffect(int i, String res, Matcher m) {
		return new Group<>(readValueChunk(i, res, m, false), readEffect(res, m, ""));
	}

	// TODO: look into how this flows and see how to make this more supportive
	private static void readDefinition(ValueType type, boolean allowFunctions, List<ExecutableChunk> exec, String res, Matcher m) {
		do {
			int i = m.start() + 1;
			if (!m.find())
				throw new ClslCompilerException("expected variable name", i, res, m);

			String varName = res.substring(i, m.start());
			if (m.group().equals("(")) {
				if (!allowFunctions)
					throw new ClslCompilerException("cannot define a function here", i, res, m);
				exec.add(readFunction(type, varName, res, m));
				break;
			}
			if (m.group().equals("[")) {// FIXME: support reading <type>
				// <name>[]=...
				i = m.start() + 1;
				if (!m.find())
					throw new ClslCompilerException("expected value", i, res, m);
				exec.add(new DefineArray(varName, type, (short) Integer.parseInt(res.substring(i, m.start()))));
				break;
			}
			if (m.group().equals("=")) {
				i = m.start() + 1;
				if (!m.find())
					throw new ClslCompilerException("expected value", i, res, m);

				// TODO: test: char c = '.';
				if (type == ValueType.CHAR && m.group().equals("'") && (!m.find() || !m.group().equals("'")))
					throw new ClslCompilerException("invalid character constant", i, res, m);
				else if ((type == ValueType.FLOAT || type == ValueType.LONG) && m.group().equals(".") && !m.find())
					throw new ClslCompilerException("expected decimal", i, res, m);

				// m.group().equals(";") ? toValue(res.substring(i, m.start()))
				// : readValueChunk(i, res, m, true)
				switch (type) {
					case CHAR:
						exec.add(new DefineChar(varName, readValueChunk(i, res, m, true)));
						break;
					case DOUBLE:
						exec.add(new DefineDouble(varName, readValueChunk(i, res, m, true)));
						break;
					case FLOAT:
						exec.add(new DefineFloat(varName, readValueChunk(i, res, m, true)));
						break;
					case INT:
						exec.add(new DefineInt(varName, readValueChunk(i, res, m, true)));
						break;
					case LONG:
						exec.add(new DefineLong(varName, readValueChunk(i, res, m, true)));
						break;
					case STRUCT:
						throw new IllegalStateException("`struct <structName> <name> = {...}` is not implemented yet");
				}
			}
			else {
				switch (type) {
					case CHAR:
						exec.add(new DefineChar(varName, new ConstCharChunk('\0')));
						break;
					case DOUBLE:
						exec.add(new DefineDouble(varName, new ConstDoubleChunk(Double.NaN)));
						break;
					case FLOAT:
						exec.add(new DefineFloat(varName, new ConstFloatChunk(Float.NaN)));
						break;
					case INT:
						exec.add(new DefineInt(varName, new ConstIntChunk(0)));
						break;
					case LONG:
						exec.add(new DefineLong(varName, new ConstLongChunk(0)));
						break;
					case STRUCT: {
						// note that `varName` here refers to the struct type

						// FIXME: if no variable name is present, we might be defining a new struct type
						i = m.start() + 1;
						if (!m.find())
							throw new ClslCompilerException("expected struct variable name", i, res, m);

						exec.add(new DefineStruct(varName, res.substring(i, m.start())));
						break;
					}
				}
			}
		} while (m.group().equals(","));
	}

	// TODO: return List<ExecutableChunk> instead?
	private static ExecutableChunk[] readEffect(String res, Matcher m, String prefix) {
		int i = m.start() + 1;
		if (!m.find())
			throw new ClslCompilerException("expected expression", i, res, m);

		boolean isBlock = m.group().equals("{");

		// if it's a block read it all
		if (isBlock) {
			i = m.start() + 1;
			int blocks = 1;
			out:
			while (m.find()) {
				switch (m.group()) {
					case "{":
						++blocks;
						break;
					case "}":
						if (--blocks == 0)
							break out;
						break;
				}
			}
		}
		// otherwise, read until the semicolon
		else
			while (!m.group().equals(";") && m.find()) ;// TODO: fix for for(;;)

		if (i == m.start())
			return ClslUtil.CHUNK_EXEC;
		return compileInternal(prefix + res.substring(i, isBlock ? m.start() : m.start() + 1), false).toArray(ClslUtil.CHUNK_EXEC);
	}

	private static ExecutableChunk readExecutable(String res, Matcher m, boolean breakAtSemi) {
		int i = m.start() + 1;
		if (!m.find())
			throw new ClslCompilerException("expected expression", i, res, m);

		if (m.group().equals("{"))// if it's a block throw an error
			throw new ClslCompilerException("illegal token", i, res, m);

		// read until the semicolon
		if (breakAtSemi) {
			while (!m.group().equals(";") && m.find()) ;
		}

		// read until close parentheses
		else {
			int blocks = 1;
			out:
			while (m.find()) {
				switch (m.group()) {
					case "(":
						++blocks;
						break;
					case ")":
						if (--blocks == 0)
							break out;
						break;
				}
			}
		}

		try {
			if (i == m.start())
				return null;
		}
		catch (IllegalStateException e) {
			m.find(i);// fix the illegal state
			return null;
		}
		return compileInternal(res.substring(i, m.start() + 1), false).get(0);
	}

	private static ExecutableChunk readFunction(ValueType returnType, String name, String res, Matcher m) {
		int i = m.start() + 1;
		int start = m.start();
		if (!m.find())
			throw new ClslCompilerException("expected expression", i, res, m);

		List<Group<String, ValueType>> parameters = new ArrayList<>();
		if (!m.group().equals(")")) {
			ValueType type;
			String varName;
			m.find(start);
			while (!m.group().equals(")")) {
				i = m.start() + 1;
				if (!m.find())
					throw new ClslCompilerException("expected expression", i, res, m);

				// FIXME: add missing types and array/pointer support
				switch (res.substring(i, m.start())) {
					case "char":
						type = ValueType.CHAR;
						break;
					case "float":
						type = ValueType.FLOAT;
						break;
					case "int":
						type = ValueType.INT;
						break;
					case "struct":// TODO: deal with struct based off components
						type = ValueType.STRUCT;
						break;
					default:
						throw new ClslCompilerException("unsupported parameter type " + res.substring(i, m.start()) + " for function " + name, i, res, m);
				}

				i = m.start() + 1;
				if (!m.find())
					throw new ClslCompilerException("expected expression", i, res, m);

				varName = res.substring(i, m.start());
				if (m.group().equals("[")) {
					if (!m.find() || !m.group().equals("]"))
						throw new ClslCompilerException("expected expression", i, res, m);

					// FIXME: get length of List if it's supplied
					type = ValueType.ARRAY;// FIXME: make List of type

					if (!m.find())
						throw new ClslCompilerException("expected expression", i, res, m);
				}

				parameters.add(new Group<>(varName, type));
			}
		}

		start = m.start();
		if (!m.find())
			throw new ClslCompilerException("expected expression", i, res, m);
		if (m.group().equals(";"))
			throw new ClslCompilerException("bodyless functions not supported yet", i, res, m);
		else if (!m.group().equals("{"))
			throw new ClslCompilerException("expected function body", i, res, m);

		m.find(start);
		return new FunctionalChunk(name, returnType, parameters.toArray(ClslUtil.array()), readEffect(res, m, ""));
	}// FIXME: allow to set from any ValueChunk

	public static ClslValue[] toArguments(String res) {
		Matcher argumentMatcher = SYNTAX_PATTERN.matcher("");
		List<ClslValue> arr = new ArrayList<>();

		// remove whitespaces
		res = res.replaceAll("\\s+", " ");
		for (byte i = 0; i < BLOCK_CHARS.length(); ++i)
			res = res.replaceAll("\\s*" + Pattern.quote(Character.toString(BLOCK_CHARS.charAt(i))) + "\\s*", Character.toString(BLOCK_CHARS.charAt(i)));
		argumentMatcher.reset(res);

		int i = 0;
		if (!argumentMatcher.find())
			throw new ClslCompilerException("expected expression", i, res, argumentMatcher);

		int blocks = 1;
		out:
		do {
			switch (argumentMatcher.group()) {
				case ";":
					throw new ClslCompilerException("illegal token", i, res, argumentMatcher);
				case ",":
					arr.add(toValue(res.substring(i, argumentMatcher.start())).get(null));
					i = argumentMatcher.start() + 1;
					break;
				case "(":
					++blocks;
					break;
				case ")":
					if (--blocks == 0)
						break out;
					break;
			}
		} while (argumentMatcher.find());

		// add the last arg (if it exists)
		if (i != argumentMatcher.start())
			arr.add(toValue(res.substring(i, argumentMatcher.start())).get(null));
		return arr.toArray(ClslUtil.VALUE);
	}

	// TODO: optimize the loading, don't just try and throw exceptions
	@Deprecated
	private static ValueChunk toValue(String str) {
		if (str.length() > 1 && str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"')
			return new ConstArrayChunk(ClslUtil.unescape(str.substring(1, str.length() - 1)));

		if (str.length() > 1 && str.charAt(0) == '\'' && str.charAt(str.length() - 1) == '\'') {
			String c = ClslUtil.unescape(str.substring(1, str.length() - 1));
			if (c.length() != 1)
				throw new ClslCompilerException("invalid char " + str);
			return new ConstCharChunk(c.charAt(0));
		}

		// some kind of number
		// FIXME: support reading fractions as .xxxx[f]
		// FIXME: support reading prefixes: 0x, 0b, 0
		if (str.charAt(0) >= '0' && str.charAt(0) <= '9') {
			if (str.charAt(str.length() - 1) == 'f') {// float suffix
				try {
					return new ConstFloatChunk(Float.parseFloat(str));
				}
				catch (NumberFormatException e) {
					throw new ClslCompilerException("invalid variable or value: " + str);
				}
			}
			if (str.charAt(str.length() - 1) == 'L') {// long suffix
				try {
					return new ConstLongChunk(Long.parseLong(str));
				}
				catch (NumberFormatException e) {
					throw new ClslCompilerException("invalid variable or value: " + str);
				}
			}

			// a whole number
			if (str.indexOf('.') == -1) {
				try {
					return new ConstIntChunk(Integer.parseInt(str));
				}
				catch (NumberFormatException e) {
					try {
						return new ConstLongChunk(Long.parseLong(str));
					}
					catch (NumberFormatException ex) {
						// TODO: attempt to parse as expression
						throw new ClslCompilerException("invalid variable or value: " + str);
					}
				}
			}

			// a fraction, which in C is a double
			try {
				return new ConstDoubleChunk(Double.parseDouble(str));
			}
			catch (NumberFormatException ex) {
				// TODO: attempt to parse as expression
				throw new ClslCompilerException("invalid variable or value: " + str);
			}
		}

		// FIXME: support ConstArrayChunk

		// FIXME: support OpIndex

		// a.b.c ... etc
		{
			String[] owners = str.split("\\.");
			if (ClslUtil.isValidId(owners[0])) {
				ValueChunk res = new GetVar(owners[0]);
				if (owners.length > 1) {
					for (short i = 1; i < owners.length; ++i)
						res = new OpMember(res, owners[i]);
				}
				return res;
			}
		}

		// TODO: attempt to parse as expression
		throw new ClslCompilerException("invalid variable or value: " + str);
	}

	private ClslCompiler() {
	}
}