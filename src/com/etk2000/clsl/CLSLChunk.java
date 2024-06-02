package com.etk2000.clsl;

import java.io.IOException;
import java.io.OutputStream;

interface CLSLChunk {
	void transmit(OutputStream o) throws IOException;
	
	// optimize the current chunk if possible
	// note: will remove useless code even if it would get called
	// example: int a = 0 * func(); becomes int a = 0;
	CLSLChunk optimize(OptimizationEnvironment env);
}