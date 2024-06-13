package com.etk2000.clsl.stack;


import com.etk2000.clsl.value.ClslValue;

import java.util.Map;

public interface StackFrame {
	void defineVar(String name, ClslValue value);

	ClslValue getVar(String name);

	Map<String, ClslValue> getVars();
}