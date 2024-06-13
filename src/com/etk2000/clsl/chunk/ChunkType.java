package com.etk2000.clsl.chunk;

import com.etk2000.clsl.chunk.op.OpAdd;
import com.etk2000.clsl.chunk.op.OpAnd;
import com.etk2000.clsl.chunk.op.OpBinAnd;
import com.etk2000.clsl.chunk.op.OpBinOr;
import com.etk2000.clsl.chunk.op.OpBool;
import com.etk2000.clsl.chunk.op.OpCast;
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
import com.etk2000.clsl.chunk.variable.GetVar;
import com.etk2000.clsl.chunk.variable.definition.DefineArray;
import com.etk2000.clsl.chunk.variable.definition.DefineChar;
import com.etk2000.clsl.chunk.variable.definition.DefineDouble;
import com.etk2000.clsl.chunk.variable.definition.DefineFloat;
import com.etk2000.clsl.chunk.variable.definition.DefineInt;
import com.etk2000.clsl.chunk.variable.definition.DefineLong;
import com.etk2000.clsl.chunk.variable.definition.DefineStruct;
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
import com.etk2000.clsl.functional.ThrowingFunction;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public enum ChunkType {
	INCLUDE(IncludeChunk.class, IncludeChunk::new),
	INCLUDE_EXTERNAL(IncludeExternalChunk.class, IncludeExternalChunk::new),

	//

	CONST_ARRAY(ConstArrayChunk.class, ConstArrayChunk::new),
	CONST_CHAR(ConstCharChunk.class, ConstCharChunk::new),
	CONST_DOUBLE(ConstDoubleChunk.class, ConstDoubleChunk::new),
	CONST_FLOAT(ConstFloatChunk.class, ConstFloatChunk::new),
	CONST_INT(ConstIntChunk.class, ConstIntChunk::new),
	CONST_LONG(ConstLongChunk.class, ConstLongChunk::new),

	//

	DEFINE_ARRAY(DefineArray.class, DefineArray::new),
	DEFINE_CHAR(DefineChar.class, DefineChar::new),
	DEFINE_DOUBLE(DefineDouble.class, DefineDouble::new),
	DEFINE_FLOAT(DefineFloat.class, DefineFloat::new),
	DEFINE_INT(DefineInt.class, DefineInt::new),
	DEFINE_LONG(DefineLong.class, DefineLong::new),
	DEFINE_STRUCT(DefineStruct.class, DefineStruct::new),

	//

	FUNCTIONAL(FunctionalChunk.class, FunctionalChunk::new),
	FUNCTION_CALL(FunctionCallChunk.class, FunctionCallChunk::new),
	RETURN(ReturnChunk.class, ReturnChunk::new),

	//

	CODE_BLOCK(CodeBlockChunk.class, CodeBlockChunk::new),
	DO_WHILE(DoWhileChunk.class, DoWhileChunk::new),
	FOR(ForChunk.class, ForChunk::new),
	IF(IfChunk.class, IfChunk::new),
	WHILE(WhileChunk.class, WhileChunk::new),

	//

	GET_VAR(GetVar.class, GetVar::new),
	SET_VAR(SetVar.class, SetVar::new),
	SET_VAR_ADD(SetVarAdd.class, SetVarAdd::new),
	SET_VAR_BIN_AND(SetVarBinAnd.class, SetVarBinAnd::new),
	SET_VAR_BIN_OR(SetVarBinOr.class, SetVarBinOr::new),
	SET_VAR_DIV(SetVarDiv.class, SetVarDiv::new),
	SET_VAR_MODULUS(SetVarModulus.class, SetVarModulus::new),
	SET_VAR_MUL(SetVarMul.class, SetVarMul::new),
	SET_VAR_SHIFT_LEFT(SetVarShiftLeft.class, SetVarShiftLeft::new),
	SET_VAR_SHIFT_RIGHT(SetVarShiftRight.class, SetVarShiftRight::new),
	SET_VAR_SUB(SetVarSub.class, SetVarSub::new),
	SET_VAR_XOR(SetVarXor.class, SetVarXor::new),

	//

	AND(OpAnd.class, OpAnd::new),
	EQUALS(OpEquals.class, OpEquals::new),
	LESS_THAN(OpLessThan.class, OpLessThan::new),
	NOT(OpNot.class, OpNot::new),
	OR(OpOr.class, OpOr::new),
	TERNARY(OpTernary.class, OpTernary::new),
	XOR(OpXor.class, OpXor::new),

	//

	INDEX(OpIndex.class, OpIndex::new),
	MEMBER(OpMember.class, OpMember::new),

	//

	ADD(OpAdd.class, OpAdd::new),
	BIN_AND(OpBinAnd.class, OpBinAnd::new),
	BIN_OR(OpBinOr.class, OpBinOr::new),
	BOOL(OpBool.class, OpBool::new),
	CAST(OpCast.class, OpCast::new),
	DEC(OpDec.class, OpDec::new),
	DIVIDE(OpDivide.class, OpDivide::new),
	INC(OpInc.class, OpInc::new),
	MODULUS(OpModulus.class, OpModulus::new),
	MULTIPLY(OpMultiply.class, OpMultiply::new),
	SHIFT_LEFT(OpShiftLeft.class, OpShiftLeft::new),
	SHIFT_RIGHT(OpShiftRight.class, OpShiftRight::new),
	SUBTRACT(OpSubtract.class, OpSubtract::new),

	//

	DUO_EXECUTABLE_CHUNK(DuoExecutableChunk.class, DuoExecutableChunk::new),
	TRI_EXECUTABLE_CHUNK(TriExecutableChunk.class, TriExecutableChunk::new);

	private static final Map<Class<? extends ClslChunk>, ChunkType> LOOKUP = new HashMap<>();

	static {
		for (ChunkType type : values())
			LOOKUP.put(type.clazz, type);
	}

	public static ChunkType valueOf(Class<? extends ClslChunk> clazz) {
		return LOOKUP.get(clazz);
	}

	private final Class<? extends ClslChunk> clazz;
	public final ThrowingFunction<InputStream, ClslChunk, IOException> read;

	@SuppressWarnings("unchecked")
	<T extends ClslChunk> ChunkType(Class<T> clazz, ThrowingFunction<InputStream, T, IOException> read) {
		this.clazz = clazz;
		this.read = (ThrowingFunction<InputStream, ClslChunk, IOException>) read;
	}
}