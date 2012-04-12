This project illustrates the problem I am having, as described in:

https://community.jboss.org/thread/197903?tstart=0


It contains three test classes which store some documents in a given index, in three different scenarios:
- Plain old Lucene FSDirectory
- InfinispanDirectory with no persistence/eviciton
- InfinispanDirectory with Jdbm persistence and a low maxEntries in eviction (artificially low so that the problem reproduces earlier).

(APOLOGIES FOR THE QUALITY OF THE CODE - IT'S A LITTLE MESSY...)