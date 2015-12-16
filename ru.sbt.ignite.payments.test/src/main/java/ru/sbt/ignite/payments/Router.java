package ru.sbt.ignite.payments;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.lang.IgniteCallable;

public class Router implements IgniteCallable<Integer> {
	private static final long serialVersionUID = 1L;
	
	IgniteCache<Integer, DocumentContainer> cache = null;
    IgniteCache<String, KorrespGamma> korrespGammaCache = null;
    IgniteCache<AccKorrespGammaKey, AccKorrespGamma> accKorrespGammaCache = null;
	
    Integer i = null;
    
	public Router(IgniteCache<Integer, DocumentContainer> cache,
			IgniteCache<String, KorrespGamma> korrespGammaCache,
			IgniteCache<AccKorrespGammaKey, AccKorrespGamma> accKorrespGammaCache) 
	{
		this.cache = cache;
		this.korrespGammaCache = korrespGammaCache;
		this.accKorrespGammaCache = accKorrespGammaCache;
	}
	
	public void setI(Integer i) {
		this.i = i;
	}
	
	@Override
	public Integer call() throws Exception {
		DocumentContainer dc = cache.localPeek(i);
		
//		System.out.println("route on node " + i);
		
		KorrespGamma kg = korrespGammaCache.localPeek("044525225");
		
		if (kg == null) {
			System.out.println("kg == null " + i);
			return -1;
		}
		
		AccKorrespGammaKey key = new AccKorrespGammaKey();
		key.KORRESP = kg.KRID;
		key.ISO_DIG = "810";
		key.CL_CODEWH = "1300000000";
		
		AccKorrespGamma akg = accKorrespGammaCache.localPeek(key);
		
		if (akg == null) {
			System.out.println("akg == null " + i);
			return -1;
		}
		
		dc.route = akg.KS;

		cache.put(i, dc);
		
		
		return 0;
	}

}
