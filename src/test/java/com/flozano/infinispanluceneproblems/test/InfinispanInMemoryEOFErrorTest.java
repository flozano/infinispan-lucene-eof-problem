package com.flozano.infinispanluceneproblems.test;

import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.infinispan.lucene.InfinispanDirectory;
import org.infinispan.manager.DefaultCacheManager;

public class InfinispanInMemoryEOFErrorTest extends EOFErrorTest {

	DefaultCacheManager cacheManager;

	@Override
	public void setUp() {
		try {
			cacheManager = new DefaultCacheManager(
					getInfinispanConfigurationFile());
			super.setUp();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	@Override
	protected Directory getDirectory() {
		return new InfinispanDirectory(cacheManager.getCache("indexMetadata"),
				cacheManager.getCache("indexChunks"),
				cacheManager.getCache("indexDistLocks"), "TEST_IDX", 512 * 1024);
	}

	protected String getInfinispanConfigurationFile() {
		return "infinispan-test.xml";
	}

	@Override
	protected void cleanDirectory(Directory d) {
		try {
			d.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void tearDown() {
		super.tearDown();
		cacheManager.stop();
	}

	@Override
	protected int getConcurrency() {
		return 10;
	}

}
