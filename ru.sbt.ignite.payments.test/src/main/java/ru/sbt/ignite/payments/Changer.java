package ru.sbt.ignite.payments;

import java.util.SortedSet;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.lang.IgniteCallable;

public class Changer implements IgniteCallable<Integer> {
	private static final long serialVersionUID = 1L;

	IgniteCache<Integer, DocumentContainer> cache = null;
	IgniteCache<String, SortedSet<ClientPosition>> clientPositionCache = null;
	
	Integer i = null;
	
	public Changer(IgniteCache<Integer, DocumentContainer> cache,
			IgniteCache<String, SortedSet<ClientPosition>> clientPositionCache) 
	{
		this.cache = cache;
		this.clientPositionCache = clientPositionCache;
	}
	
	public void setI(Integer i) {
		this.i = i;
	}
	
	@Override
	public Integer call() throws Exception {
		DocumentContainer dc = cache.localPeek(i);
		
//		System.out.println("change on node " + i);
		
		String ks = dc.route;

		//TO Lock Position
		
		SortedSet<ClientPosition> positions = clientPositionCache.get(ks);
		
		for (ClientPosition p : positions) {
//			System.out.println("Position : " + p.position.intValue() + " | " + dc.document.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getIntrBkSttlmAmt().getValue());
			
			if (p.position.compareTo(dc.document.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getIntrBkSttlmAmt().getValue()) < 0) {
//				System.out.println("insufficient position " + i);
				return -1;
			}
		}
		
		for (ClientPosition p : positions) {
			p.position = p.position.subtract(dc.document.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getIntrBkSttlmAmt().getValue());
		}
		
		clientPositionCache.put(ks, positions);
		
		// TODO UnLock Position

		return 0;
	}
}
