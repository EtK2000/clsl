package com.etk2000.clsl;

import com.etk2000.clsl.exception.ClslCompilerException;
import com.etk2000.clsl.exception.ClslHeaderImportFailureException;
import com.etk2000.clsl.exception.include.ClslHeaderMissingAtCompileTimeException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class DirectoryHeaderFinder implements CLSLExternHeaderFinder {
	private final Set<String> ds = new HashSet<>();

	public void addDirectory(String dir) {
		ds.add(dir);
	}

	// FIXME: use CLSLCompiler and allowFunctions from what accessed this
	// please note, this does not do security checks!!!
	@Override
	public CLSLCode find(String header) throws ClslCompilerException {
		for (String d : ds) {
			char c = d.charAt(d.length() - 1);
			File f = new File((c != '/' && c != '\\' ? d + '/' : d) + header);
			if (f.exists()) {
				try {
					return new CLSLCompiler().compile(new String(Files.readAllBytes(f.toPath())), true);
				}
				catch (IOException e) {
					throw new ClslHeaderImportFailureException(header, e);
				}
			}
		}
		throw new ClslHeaderMissingAtCompileTimeException(header);
	}
}