package com.etk2000.clsl;

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
	public CLSLCode find(String header) throws CLSL_CompilerException {
		for (String d : ds) {
			char c = d.charAt(d.length() - 1);
			File f = new File((c != '/' && c != '\\' ? d + '/' : d) + header);
			if (f.exists()) {
				try {
					return new CLSLCompiler().compile(new String(Files.readAllBytes(f.toPath())), true);
				}
				catch (IOException e) {
					e.printStackTrace();
					throw new CLSL_CompilerException("could import header \"" + header + '"');
				}
			}
		}
		throw new CLSL_CompilerException("could find header \"" + header + '"');
	}
}