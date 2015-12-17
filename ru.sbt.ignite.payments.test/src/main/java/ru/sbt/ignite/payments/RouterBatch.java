package ru.sbt.ignite.payments;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.lang.IgniteCallable;

public class RouterBatch implements IgniteCallable<Map<Integer, Integer>> {
	private static final long serialVersionUID = 1L;
	
	Ignite ignite = null;
	
	Collection<Integer> list = null;
	
	public RouterBatch(Ignite ignite) {
		this.ignite = ignite;
	}
	
	public void setList(Collection<Integer> list) {
		this.list = list;
	}

	@Override
	public Map<Integer, Integer> call() throws Exception {
		Map<Integer, Integer> result = new HashMap<>(list.size());
		
		IgniteCache<Integer, DocumentContainer> cache = ignite.cache("CACHE_NAME");
	    IgniteCache<String, KorrespGamma> korrespGammaCache = ignite.cache("KorrespGamma");
	    IgniteCache<AccKorrespGammaKey, AccKorrespGamma> accKorrespGammaCache = ignite.cache("AccKorrespGamma");
		
		for (int i : list) {
			DocumentContainer dc = cache.localPeek(i);
			
//			System.out.println("route on node " + i);
			
			KorrespGamma kg = korrespGammaCache.localPeek("044525225");
			
			if (kg == null) {
				System.out.println("kg == null " + i);
				result.put(i, -1);
			}
			
			AccKorrespGammaKey key = new AccKorrespGammaKey();
			key.KORRESP = kg.KRID;
			key.ISO_DIG = "810";
			key.CL_CODEWH = "1300000000";
			
			AccKorrespGamma akg = accKorrespGammaCache.localPeek(key);
			
			if (akg == null) {
				System.out.println("akg == null " + i);
				result.put(i, -1);
			}
			
			dc.route = akg.KS;
	
			cache.put(i, dc);
		
			result.put(i, 0);
		}
		
		return result;
	}

}
