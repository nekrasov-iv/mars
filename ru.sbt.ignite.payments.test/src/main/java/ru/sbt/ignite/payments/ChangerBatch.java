package ru.sbt.ignite.payments;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.lang.IgniteCallable;

public class ChangerBatch implements IgniteCallable<Map<Integer, Integer>> {
	private static final long serialVersionUID = 1L;

	Ignite ignite = null;
	
	Collection<Integer> list = null;
	
	public ChangerBatch(Ignite ignite) {
		this.ignite = ignite;
	}
	
	public void setList(Collection<Integer> list) {
		this.list = list;
	}

	@Override
	public Map<Integer, Integer> call() throws Exception {
		Map<Integer, Integer> result = new HashMap<>(list.size());
		
		IgniteCache<Integer, DocumentContainer> cache = ignite.cache("CACHE_NAME");
		IgniteCache<String, SortedSet<ClientPosition>> clientPositionCache = ignite.cache("ClientPositionCache");
		
		for (int i : list) {
			DocumentContainer dc = cache.localPeek(i);
			
//			System.out.println("change on node " + i);
			
			String ks = dc.route;
	
			//TODO Lock Position
			SortedSet<ClientPosition> positions = clientPositionCache.get(ks);
			
			for (ClientPosition p : positions) {
				if (p.position.compareTo(dc.document.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getIntrBkSttlmAmt().getValue()) < 0) {
					result.put(i, -1);
				}
			}
			
			for (ClientPosition p : positions) {
				p.position = p.position.subtract(dc.document.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getIntrBkSttlmAmt().getValue());
			}
			
			clientPositionCache.put(ks, positions);
			// TODO UnLock Position
			
			result.put(i, 0);
		}		

		return result;
	}
}
