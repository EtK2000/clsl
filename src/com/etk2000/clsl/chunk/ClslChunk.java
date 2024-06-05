package com.etk2000.clsl.chunk;

import com.etk2000.clsl.OptimizationEnvironment;

import java.io.IOException;
import java.io.OutputStream;

public interface ClslChunk {
	// optimize the current chunk if possible
	// note: will remove useless code even if it would get called
	// example: int a = 0 * func(); becomes int a = 0;
	ClslChunk optimize(OptimizationEnvironment env);

	void transmit(OutputStream o) throws IOException;
}