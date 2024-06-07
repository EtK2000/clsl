package com.etk2000.clsl.compiler;

enum ExpressionType {
	// math
	ADD, ADD_EQUAL, DIVIDE, DIVIDE_EQUAL, MODULUS, MODULUS_EQUAL, MULTIPLY, MULTIPLY_EQUAL, SUBTRACT, SUBTRACT_EQUAL,

	// ifs
	AND, EQUAL_TO, LESS_THAN, LESS_THAN_OR_EQUAL, MORE_THAN, MORE_THAN_OR_EQUAL, NOT, NOT_EQUAL, OR,

	// bitwise operations
	BIN_AND, BIN_AND_EQUAL, BIN_OR, BIN_OR_EQUAL, SHIFT_LEFT, SHIFT_LEFT_EQUAL, SHIFT_RIGHT, SHIFT_RIGHT_EQUAL, XOR, XOR_EQUAL, //

	// special ops
	DECREMENT, INCREMENT, SET,

	// parentheses
	PARENTHESIS_CLOSE, PARENTHESIS_OPEN,

	// ?:
	QUESTION, ELSE,

	// function arguments
	COMMA
}