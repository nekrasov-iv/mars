package ru.sbt.ignite.payments;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.lang.IgniteCallable;

public class Checker implements IgniteCallable<Integer> {
	private static final long serialVersionUID = 1L;

	IgniteCache<Integer, DocumentContainer> cache = null;
	
	Integer i = null;
	
	public Checker(IgniteCache<Integer, DocumentContainer> cache) {
		this.cache = cache;
	}
	
	public void setI(Integer i) {
		this.i = i;
	}

	@Override
	public Integer call() throws Exception {
		DocumentContainer dc = cache.localPeek(i);
		
//		System.out.println("check on node " + i);
		
		if (dc != null) {
			// Типа проверка
			//Thread.sleep(10);
			dc.document.getFIToFICstmrCdtTrf().getGrpHdr().setNbOfTxs("0");
		}
		
		return 0;
	}
}
