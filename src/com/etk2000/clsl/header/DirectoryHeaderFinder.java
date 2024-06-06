package com.etk2000.clsl.header;

import com.etk2000.clsl.ClslCode;
import com.etk2000.clsl.compiler.ClslCompiler;
import com.etk2000.clsl.exception.ClslHeaderImportFailureException;
import com.etk2000.clsl.exception.ClslRuntimeException;
import com.etk2000.clsl.exception.include.ClslHeaderNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class DirectoryHeaderFinder implements ClslExternHeaderFinder {
	private final Set<String> ds = new HashSet<>();

	public void addDirectory(String dir) {
		ds.add(dir);
	}

	// FIXME: use ClslCompiler and allowFunctions from what accessed this
	// please note, this does not do security checks!!!
	@Override
	public ClslCode find(String header) throws ClslRuntimeException {
		for (String d : ds) {
			char c = d.charAt(d.length() - 1);
			File f = new File((c != '/' && c != '\\' ? d + '/' : d) + header);
			if (f.exists()) {
				try {
					return ClslCompiler.compile(new String(Files.readAllBytes(f.toPath())), true);
				}
				catch (IOException e) {
					throw new ClslHeaderImportFailureException(header, e);
				}
			}
		}
		throw new ClslHeaderNotFoundException(header);
	}
}