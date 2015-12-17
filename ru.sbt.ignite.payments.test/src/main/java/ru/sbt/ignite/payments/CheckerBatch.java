package ru.sbt.ignite.payments;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.lang.IgniteCallable;

public class CheckerBatch implements IgniteCallable<Map<Integer, Integer>> {
	private static final long serialVersionUID = 1L;

	Ignite ignite = null;
	
	Collection<Integer> list = null;
	
	public CheckerBatch(Ignite ignite) {
		this.ignite = ignite;
	}
	
	public void setList(Collection<Integer> list) {
		this.list = list;
	}

	@Override
	public Map<Integer, Integer> call() throws Exception {
		Map<Integer, Integer> result = new HashMap<>(list.size());
		
		IgniteCache<Integer, DocumentContainer> cache = ignite.cache("CACHE_NAME");
		
		for (int i : list) {
			DocumentContainer dc = cache.localPeek(i);
			
//			System.out.println("check on node " + i);
			
			if (dc != null) {
				// Типа проверка
				//Thread.sleep(10);
				dc.document.getFIToFICstmrCdtTrf().getGrpHdr().setNbOfTxs("0");
			}
			
			result.put(i, 0);
		}
		
		return result;
	}
}
