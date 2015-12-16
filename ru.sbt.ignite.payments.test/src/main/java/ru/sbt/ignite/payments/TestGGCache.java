package ru.sbt.ignite.payments;

import java.io.Serializable;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.lang.IgniteFuture;
import org.apache.ignite.lang.IgniteInClosure;
import org.apache.ignite.transactions.Transaction;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;

public class TestGGCache {

	public static void main(String[] args) {
		Ignition.setClientMode(true);
		
		try (Ignite ignite = Ignition.start("C:\\Distr\\gridgain-community-fabric-1.4.1\\examples\\config\\example-ignite.xml")) {
			CacheConfiguration<Integer, Account> cfg = new CacheConfiguration<>();
			
			cfg.setName("demCache");
			cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
			
			IgniteCache<Integer, Account> cache = ignite.getOrCreateCache(cfg);
			
//			cache.put(1, new Account(1, 100));
//			cache.put(2, new Account(2, 200));
//			
//			System.out.println(cache.get(1));
//			System.out.println(cache.get(2));
			
//			IgniteCache<Integer, Account> asyncCache = cache.withAsync();
//			
//			asyncCache.getAndPut(2, new Account(22, 2200));
//			
//			IgniteFuture<Account> fut = asyncCache.future();
//			
//			fut.listen(new IgniteInClosure<IgniteFuture<Account>>() {
//				@Override
//				public void apply(IgniteFuture<Account> f) {
//					System.out.println("Retrieved cache value: " + f.get());
//				}
//			});
//			
//			fut.get();
//			
//			System.out.println(cache.get(2));
			
			try (Transaction tx = ignite.transactions().txStart(TransactionConcurrency.PESSIMISTIC, TransactionIsolation.REPEATABLE_READ)) {
				Account acct = cache.get(1);
				
				acct.update(10);
				
				cache.put(1, acct);
				
				tx.commit();
			}
			
			System.out.println("after transaction " + cache.get(1));
		}
	}

    /**
     * Account.
     */
    private static class Account implements Serializable {
        /** Account ID. */
        private int id;

        /** Account balance. */
        private double balance;

        /**
         * @param id Account ID.
         * @param balance Balance.
         */
        Account(int id, double balance) {
            this.id = id;
            this.balance = balance;
        }

        /**
         * Change balance by specified amount.
         *
         * @param amount Amount to add to balance (may be negative).
         */
        void update(double amount) {
            balance += amount;
        }

        /** {@inheritDoc} */
        @Override public String toString() {
            return "Account [id=" + id + ", balance=$" + balance + ']';
        }
    }
	
}
