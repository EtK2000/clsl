package com.etk2000.clsl.functional;

import com.etk2000.clsl.ClslRuntimeEnv;
import com.etk2000.clsl.value.ClslValue;

@FunctionalInterface
public interface FunctionDataRet {
	ClslValue exec(ClslRuntimeEnv env, ClslValue[] args);
}