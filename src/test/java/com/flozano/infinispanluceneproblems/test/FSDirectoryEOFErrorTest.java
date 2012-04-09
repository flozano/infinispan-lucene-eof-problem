package com.flozano.infinispanluceneproblems.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class FSDirectoryEOFErrorTest extends EOFErrorTest {

	@Override
	protected Directory getDirectory() {
		try {
			return FSDirectory.open(new File("/tmp/lucene_fs"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void cleanDirectory(Directory d) {
		FSDirectory d2 = (FSDirectory) d;
		try {
			FileUtils.deleteDirectory(d2.getDirectory());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected int getConcurrency() {
		return 10;
	}

}
