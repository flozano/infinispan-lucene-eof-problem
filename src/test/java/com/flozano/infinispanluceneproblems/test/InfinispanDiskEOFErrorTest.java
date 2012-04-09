package com.flozano.infinispanluceneproblems.test;

public class InfinispanDiskEOFErrorTest extends InfinispanInMemoryEOFErrorTest {
	@Override
	protected String getInfinispanConfigurationFile() {
		return "infinispan-test-jdbm.xml";
	}
}
