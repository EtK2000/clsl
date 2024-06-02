package com.etk2000.clsl;

enum ChunkType {
	IncludeChunk(IncludeChunk.class), IncludeExternChunk(IncludeExternChunk.class),

	//

	ConstArray(ConstArrayChunk.class), ConstChar(ConstCharChunk.class), ConstDouble(ConstDoubleChunk.class), ConstFloat(ConstFloatChunk.class), ConstInt(
			ConstIntChunk.class), ConstLong(ConstLongChunk.class), //
	Functional(FunctionalChunk.class), FunctionCall(FunctionCall.class), Return(ReturnChunk.class), //
	DefineArray(DefineArray.class), DefineChar(DefineChar.class), DefineDouble(DefineDouble.class), DefineFloat(DefineFloat.class), DefineInt(
			DefineInt.class), DefineLong(DefineLong.class), //

	//

	CodeBlock(CodeBlockChunk.class), //
	DoWhile(DoWhileChunk.class), For(ForChunk.class), If(IfChunk.class), While(WhileChunk.class), //

	//

	GetVar(GetVar.class), SetVar(SetVar.class), //
	SetVarAdd(SetVarAdd.class), SetVarBinAnd(SetVarBinAnd.class), SetVarBinOr(SetVarBinOr.class), SetVarDiv(SetVarDiv.class), SetVarModulus(
			SetVarModulus.class), SetVarMul(SetVarMul.class), SetVarShiftLeft(
					SetVarShiftLeft.class), SetVarShiftRight(SetVarShiftLeft.class), SetVarSub(SetVarSub.class), SetVarXor(SetVarXor.class), //

	//

	And(OpAnd.class), Equals(OpEquals.class), Not(OpNot.class), Or(OpOr.class), Xor(OpXor.class), OpLessThan(OpLessThan.class), OpTernary(OpTernary.class),
	//

	Index(OpIndex.class), Member(OpMember.class),

	//

	BinAnd(OpBinAnd.class), BinOr(OpBinOr.class), ShiftLeft(OpShiftLeft.class), ShiftRight(OpShiftRight.class), //
	Add(OpAdd.class), Divide(OpDivide.class), Modulus(OpModulus.class), Multiply(OpMultiply.class), Subtract(OpSubtract.class), //
	Dec(OpDec.class), Inc(OpInc.class),

	//

	DuoExecutableChunk(DuoExecutableChunk.class), TriExecutableChunk(TriExecutableChunk.class);

	final Class<? extends CLSLChunk> c;

	private ChunkType(Class<? extends CLSLChunk> c) {
		this.c = c;
	}
}