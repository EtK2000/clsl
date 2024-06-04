package com.etk2000.clsl.chunk;

import com.etk2000.clsl.chunk.op.OpAdd;
import com.etk2000.clsl.chunk.op.OpAnd;
import com.etk2000.clsl.chunk.op.OpBinAnd;
import com.etk2000.clsl.chunk.op.OpBinOr;
import com.etk2000.clsl.chunk.op.OpDec;
import com.etk2000.clsl.chunk.op.OpDivide;
import com.etk2000.clsl.chunk.op.OpEquals;
import com.etk2000.clsl.chunk.op.OpInc;
import com.etk2000.clsl.chunk.op.OpIndex;
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
import com.etk2000.clsl.chunk.variable.set.SetVarShiftLeft;

public enum ChunkType {
	Include(IncludeChunk.class), IncludeExtern(IncludeExternChunk.class),

	//

	ConstArray(ConstArrayChunk.class), ConstChar(ConstCharChunk.class), ConstDouble(ConstDoubleChunk.class), ConstFloat(ConstFloatChunk.class), ConstInt(
			ConstIntChunk.class), ConstLong(ConstLongChunk.class), //
	Functional(FunctionalChunk.class), FunctionCall(FunctionCallChunk.class), Return(ReturnChunk.class), //
	DefineArray(com.etk2000.clsl.chunk.variable.definition.DefineArray.class), DefineChar(com.etk2000.clsl.chunk.variable.definition.DefineChar.class), DefineDouble(com.etk2000.clsl.chunk.variable.definition.DefineDouble.class), DefineFloat(com.etk2000.clsl.chunk.variable.definition.DefineFloat.class), DefineInt(
			com.etk2000.clsl.chunk.variable.definition.DefineInt.class), DefineLong(com.etk2000.clsl.chunk.variable.definition.DefineLong.class), //

	//

	CodeBlock(CodeBlockChunk.class), //
	DoWhile(DoWhileChunk.class), For(ForChunk.class), If(IfChunk.class), While(WhileChunk.class), //

	//

	GetVar(com.etk2000.clsl.chunk.variable.GetVar.class), SetVar(com.etk2000.clsl.chunk.variable.set.SetVar.class), //
	SetVarAdd(com.etk2000.clsl.chunk.variable.set.SetVarAdd.class), SetVarBinAnd(com.etk2000.clsl.chunk.variable.set.SetVarBinAnd.class), SetVarBinOr(com.etk2000.clsl.chunk.variable.set.SetVarBinOr.class), SetVarDiv(com.etk2000.clsl.chunk.variable.set.SetVarDiv.class), SetVarModulus(
			com.etk2000.clsl.chunk.variable.set.SetVarModulus.class), SetVarMul(com.etk2000.clsl.chunk.variable.set.SetVarMul.class), SetVarShiftLeft(
			com.etk2000.clsl.chunk.variable.set.SetVarShiftLeft.class), SetVarShiftRight(SetVarShiftLeft.class), SetVarSub(com.etk2000.clsl.chunk.variable.set.SetVarSub.class), SetVarXor(com.etk2000.clsl.chunk.variable.set.SetVarXor.class), //

	//

	And(OpAnd.class), Equals(OpEquals.class), Not(OpNot.class), Or(OpOr.class), Xor(OpXor.class), LessThan(OpLessThan.class), Ternary(OpTernary.class),
	//

	Index(OpIndex.class), Member(OpMember.class),

	//

	BinAnd(OpBinAnd.class), BinOr(OpBinOr.class), ShiftLeft(OpShiftLeft.class), ShiftRight(OpShiftRight.class), //
	Add(OpAdd.class), Divide(OpDivide.class), Modulus(OpModulus.class), Multiply(OpMultiply.class), Subtract(OpSubtract.class), //
	Dec(OpDec.class), Inc(OpInc.class),

	//

	DuoExecutableChunk(DuoExecutableChunk.class), TriExecutableChunk(TriExecutableChunk.class);

	public final Class<? extends ClslChunk> clazz;

	ChunkType(Class<? extends ClslChunk> clazz) {
		this.clazz = clazz;
	}
}