package com.flozano.infinispanluceneproblems.test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import junit.framework.Assert;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class EOFErrorTest {

	protected abstract Directory getDirectory();

	protected Directory dir;

	@Before
	public void setUp() {
		dir = getDirectory();
	}

	@After
	public void tearDown() {
		cleanDirectory(dir);
	}

	@Test
	public void eofErrorTest() throws CorruptIndexException,
			LockObtainFailedException, IOException, InterruptedException {

		final AtomicBoolean failed = new AtomicBoolean(false);
		Thread[] thread = new Thread[getConcurrency()];
		for (int k = 0; k < 4; k++) {
			final int iterationIndex = k;
			final IndexWriterConfig iwc = new IndexWriterConfig(
					Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			final IndexWriter iw = new IndexWriter(dir, iwc);
			for (int i = 0; i < thread.length; i++) {
				final int threadIndex = i;
				thread[i] = new Thread(new Runnable() {
					public void run() {
						for (int j = 0; j < 1000; j++) {
							Document d = getRandomDocument();
							try {
								iw.addDocument(d);
								System.out.print(".");
							} catch (Throwable e) {
								e.printStackTrace();
								failed.set(true);
								throw new RuntimeException(e);
							}
						}
					}
				});
			}
			for (int i = 0; i < thread.length; i++) {
				thread[i].start();
			}
			for (int i = 0; i < thread.length; i++) {
				thread[i].join();
			}
			iw.commit();
			iw.close();
		}
		Assert.assertFalse(failed.get());
	}

	@Test
	public void secondEofIteration() throws CorruptIndexException,
			LockObtainFailedException, IOException, InterruptedException {
		this.eofErrorTest();
	}

	private Document getRandomDocument() {
		Document d = new Document();
		d.add(new Field("id", java.util.UUID.randomUUID().toString(),
				Store.YES, Index.NOT_ANALYZED));
		d.add(new Field("firstName", "John", Store.YES, Index.ANALYZED_NO_NORMS));
		d.add(new Field("lastName", "Doe", Store.YES, Index.ANALYZED_NO_NORMS));
		d.add(new Field("fullName", "John Doe", Store.YES, Index.ANALYZED));
		d.add(new Field("emailAddress", "whatever@blabla.com", Store.YES,
				Index.ANALYZED));
		d.add(new Field("phoneNumber", "+34 961 200 300", Store.YES,
				Index.ANALYZED));
		d.add(new Field("country", "Spain", Store.YES, Index.ANALYZED));
		d.add(new Field("street", "Calle Desengaño", Store.YES, Index.ANALYZED));
		d.add(new Field("number", "14", Store.YES, Index.ANALYZED));
		d.add(new Field(
				"description",
				"Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
				Store.YES, Index.ANALYZED));
		d.add(new Field(
				"anotherDescription",
				"Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?",
				Store.YES, Index.ANALYZED));
		d.add(new Field(
				"evenMore",
				"At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat.",
				Store.YES, Index.ANALYZED));

		return d;
	}

	protected abstract void cleanDirectory(Directory d);

	protected abstract int getConcurrency();
}
