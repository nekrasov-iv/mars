package ru.sbt.ignite.payments;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgniteCallable;

public class TestQueueCreateCache {
	public static void main(String[] args) throws Exception {
		Ignition.setClientMode(true);
		
		final String CACHE_NAME = "CACHE_NAME";
		
		try (final Ignite ignite = Ignition.start("C:\\Distr\\gridgain-community-fabric-1.4.1\\examples\\config\\example-ignite.xml")) {
            final IgniteCache<Integer, DocumentContainer> cache = ignite.cache(CACHE_NAME);

            DocumentContainer dc = cache.get(3);
            
            System.out.println(dc.document.getFIToFICstmrCdtTrf().getGrpHdr().getMsgId());
            
			ClusterNode node = ignite.cluster().mapKeyToNode(CACHE_NAME, 3);
            
            Integer res = ignite.compute(ignite.cluster().forNode(node)).call(new IgniteCallable<Integer>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Integer call() throws Exception {
					DocumentContainer dc = cache.localPeek(1);
					
					System.out.println("check on node " + dc);
					
					// Типа проверка
					Thread.sleep(10);
					
					return 0;
				}
            });
            
		}
	}

}
