package ru.sbt.ignite.payments;

import java.io.File;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;

import com.sbt.iso20022.pacs_008_001_05.Document;

public class TestQueue {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		Ignition.setClientMode(true);
		
		final String CACHE_NAME = "CACHE_NAME";
		
		final String KorrespGammaCache = "KorrespGamma";

		final String AccKorrespGammaCache = "AccKorrespGamma";

		final String ClientPositionCache = "ClientPositionCache";
		
		try (final Ignite ignite = Ignition.start("C:\\Distr\\gridgain-community-fabric-1.4.1\\examples\\config\\example-ignite.xml")) {
            CacheConfiguration<Integer, DocumentContainer> cacheCfg = new CacheConfiguration<>(CACHE_NAME);

            cacheCfg.setCacheMode(CacheMode.PARTITIONED);
            cacheCfg.setBackups(1);
            cacheCfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
            
            final IgniteCache<Integer, DocumentContainer> cache = ignite.getOrCreateCache(cacheCfg);

            
            CacheConfiguration<String, KorrespGamma> cacheCfg2 = new CacheConfiguration<>(KorrespGammaCache);

            cacheCfg2.setCacheMode(CacheMode.REPLICATED);
            cacheCfg2.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
            
            final IgniteCache<String, KorrespGamma> korrespGammaCache = ignite.getOrCreateCache(cacheCfg2);
            
			
            CacheConfiguration<AccKorrespGammaKey, AccKorrespGamma> cacheCfg3 = new CacheConfiguration<>(AccKorrespGammaCache);

            cacheCfg3.setCacheMode(CacheMode.REPLICATED);
            cacheCfg3.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
            
            final IgniteCache<AccKorrespGammaKey, AccKorrespGamma> accKorrespGammaCache = ignite.getOrCreateCache(cacheCfg3);

            
            CacheConfiguration<String, Collection<ClientPosition>> cacheCfg4 = new CacheConfiguration<>(ClientPositionCache);

            cacheCfg4.setCacheMode(CacheMode.PARTITIONED);
            cacheCfg4.setBackups(1);
            cacheCfg4.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
            
            final IgniteCache<String, Collection<ClientPosition>> clientPositionCache = ignite.getOrCreateCache(cacheCfg4);
            
            
            
            final int capacity = 10000;
			
			// Очередь проверок
			final ArrayBlockingQueue<Integer> checkQueue = new ArrayBlockingQueue<>(capacity);

			// Очередь ошибки проверок
			final ArrayBlockingQueue<Integer> checkQueueError = new ArrayBlockingQueue<>(capacity);
			
			// Очередь маршрутизации
			final ArrayBlockingQueue<Integer> routeQueue = new ArrayBlockingQueue<>(capacity);
			
			// Очередь ошибки маршрутизации
			final ArrayBlockingQueue<Integer> routeQueueError = new ArrayBlockingQueue<>(capacity);
			
			// Очередь изменения позиции
			final ArrayBlockingQueue<Integer> changePositionQueue = new ArrayBlockingQueue<>(capacity);
			
			// Очередь ошибки изменения позиции
			final ArrayBlockingQueue<Integer> changePositionQueueError = new ArrayBlockingQueue<>(capacity);
	
			
			
			
			
			final String xml = new String(Files.readAllBytes(Paths.get("C:\\Локальные документы\\PDO\\pacs.008.001.05\\pacs.008.001.05_01.xml")));
//			StringReader reader = new StringReader(xml);
			
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(new File("C:\\Локальные документы\\PDO\\pacs.008.001.05\\pacs.008.001.05.xsd"));
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setSchema(schema);
			
			
			
			
			
			KorrespGamma kg = new KorrespGamma();
			kg.KRBNKSEEK = "044525225";
			kg.KRBICDIRP = "SABRRUMM";
			kg.KRID = 1;
			kg.STATUS = "Д";
			korrespGammaCache.put(kg.KRBNKSEEK, kg);

			kg = new KorrespGamma();
			kg.KRBNKSEEK = "044525226";
			kg.KRBICDIRP = "SABRRUMM";
			kg.KRID = 2;
			kg.STATUS = "Д";
			korrespGammaCache.put(kg.KRBNKSEEK, kg);

			kg = new KorrespGamma();
			kg.KRBNKSEEK = "044525227";
			kg.KRBICDIRP = "SABRRUMM";
			kg.KRID = 3;
			kg.STATUS = "Д";
			korrespGammaCache.put(kg.KRBNKSEEK, kg);
			
			
			
			
			
			AccKorrespGamma akg = new AccKorrespGamma();
			akg.CL_CODE = "1300000000";
			akg.ISO_DIG = "810";
			akg.KORRESP = 1;
			akg.KS = "1234567890";
			akg.KSNOSTRO = "1234567890_NOSTRO1";
			akg.CL_CODEWH = "1300000000";
			AccKorrespGammaKey akgk = new AccKorrespGammaKey();
			akgk.KORRESP = 1;
			akgk.ISO_DIG = "810";
			akgk.CL_CODEWH = "1300000000";
			accKorrespGammaCache.put(akgk, akg);
			
			akg = new AccKorrespGamma();
			akg.CL_CODE = "1400000000";
			akg.ISO_DIG = "810";
			akg.KORRESP = 2;
			akg.KS = "1234567890";
			akg.KSNOSTRO = "1234567890_NOSTRO2";
			akg.CL_CODEWH = "1400000000";
			akgk = new AccKorrespGammaKey();
			akgk.KORRESP = 1;
			akgk.ISO_DIG = "810";
			akgk.CL_CODEWH = "1400000000";
			accKorrespGammaCache.put(akgk, akg);

			akg = new AccKorrespGamma();
			akg.CL_CODE = "1500000000";
			akg.ISO_DIG = "810";
			akg.KORRESP = 3;
			akg.KS = "1234567890";
			akg.KSNOSTRO = "1234567890_NOSTRO3";
			akg.CL_CODEWH = "1500000000";
			akgk = new AccKorrespGammaKey();
			akgk.KORRESP = 1;
			akgk.ISO_DIG = "810";
			akgk.CL_CODEWH = "1500000000";
			accKorrespGammaCache.put(akgk, akg);
			
			
			SortedSet<ClientPosition> cps = new TreeSet<>();
			
			ClientPosition cp = new ClientPosition();
			cp.KS = "1234567890";
			cp.date = new Date(2015, 12, 11);
			cp.position = new BigDecimal("100000000000");
			cps.add(cp);

			cp = new ClientPosition();
			cp.KS = "1234567890";
			cp.date = new Date(2015, 12, 12);
			cp.position = new BigDecimal("200000000000");
			cps.add(cp);

			cp = new ClientPosition();
			cp.KS = "1234567890";
			cp.date = new Date(2015, 12, 13);
			cp.position = new BigDecimal("300000000000");
			cps.add(cp);
			
			clientPositionCache.put("1234567890", cps);
			
			cps = new TreeSet<>();
			
			cp = new ClientPosition();
			cp.KS = "2234567890";
			cp.date = new Date(2015, 12, 11);
			cp.position = new BigDecimal("100");
			cps.add(cp);

			cp = new ClientPosition();
			cp.KS = "2234567890";
			cp.date = new Date(2015, 12, 12);
			cp.position = new BigDecimal("200");
			cps.add(cp);

			cp = new ClientPosition();
			cp.KS = "2234567890";
			cp.date = new Date(2015, 12, 13);
			cp.position = new BigDecimal("300");
			cps.add(cp);
			
			clientPositionCache.put("2234567890", cps);			
			
//			Runnable inConverter = new Runnable() {
//				@Override
//				public void run() {
//					try {
//						int i = 0;
//						
//						while (i++ < capacity) {
////							System.out.println("convert " + i);
//
//							StringReader reader = new StringReader(xml);
//	
//							JAXBElement<Document> jaxbElement = (JAXBElement<Document>) jaxbUnmarshaller.unmarshal(reader);
//							Document document = jaxbElement.getValue();
//							
//							DocumentContainer dc = new DocumentContainer();
//							dc.document = document;
//							
//							cache.put(i, dc);
//							
//							checkQueue.put(i);
//						}
//					} catch (InterruptedException | JAXBException e) {
//						throw new RuntimeException(e);
//					}
//				}
//			};

			int i = 0;
			
			while (i++ < capacity) {
				StringReader reader = new StringReader(xml);

				JAXBElement<Document> jaxbElement = (JAXBElement<Document>) jaxbUnmarshaller.unmarshal(reader);
				Document document = jaxbElement.getValue();
				
				DocumentContainer dc = new DocumentContainer();
				dc.document = document;
				
				cache.put(i, dc);
			}

			Runnable inConverter = new Runnable() {
				@Override
				public void run() {
					try {
						int i = 0;
						
						while (i++ < capacity) {
							checkQueue.put(i);
						}
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			};
			
			final Checker checkerCallable = new Checker(cache);
			
			Runnable checker = new Runnable() {
				@Override
				public void run() {
					int count = 0;
					
					while (true) {
						final Integer i = checkQueue.poll();
						
						if (i != null) {
							count++;
//							System.out.println("check " + i);

							ClusterNode node = ignite.cluster().mapKeyToNode(CACHE_NAME, i);
							
							checkerCallable.setI(i);
							
			                Integer res = ignite.compute(ignite.cluster().forNode(node)).call(checkerCallable);
			                
			                try {
				                if (res < 0) {
									checkQueueError.put(i);
				                } else {
				                	routeQueue.put(i);
				                }
			                } catch (InterruptedException e) {
			                	e.printStackTrace();
			                }
			                
			                if (count == capacity) {
			                	break;
			                }
						}
					}
				}
			};

			final Router routerCallable = new Router(cache, korrespGammaCache, accKorrespGammaCache);
			
			Runnable router = new Runnable() {
				@Override
				public void run() {
					int count = 0;
					
						while (true) {
							final Integer i = routeQueue.poll();
							
							if (i != null) {
								count++;
								
//								System.out.println("route " + i);
	
								ClusterNode node = ignite.cluster().mapKeyToNode(CACHE_NAME, i);
								
								routerCallable.setI(i);
								
				                Integer res = ignite.compute(ignite.cluster().forNode(node)).call(routerCallable);
							
				                try {
					                if (res < 0) {
										routeQueueError.put(i);
					                } else {
					                	changePositionQueue.put(i);
					                }
				                } catch (InterruptedException e) {
				                	// TODO Auto-generated catch block
				                	e.printStackTrace();
				                }
							}
							
			                
			                if (count == capacity) {
			                	break;
			                }
						}
				}
			};
			
			final Changer changerCalable = new Changer(cache, clientPositionCache);
			
			Runnable changer = new Runnable() {
				@Override
				public void run() {					
					int count = 0;
					
					while (true) {
						final Integer i = changePositionQueue.poll();
						
						if (i != null) {
							count++;
							
//							System.out.println("change " + i);

							ClusterNode node = ignite.cluster().mapKeyToNode(CACHE_NAME, i);
							
							changerCalable.setI(i);
							
			                Integer res = ignite.compute(ignite.cluster().forNode(node)).call(changerCalable);

			                try {
				                if (res < 0) {
										changePositionQueueError.put(i);
				                }
			                } catch (InterruptedException e) {
			                	e.printStackTrace();
			                }
						}
						
		                
		                if (count == capacity) {
		                	break;
		                }
					}
				}
			};
			
			for (int x = 0; x < 5; x++) {
				checkQueue.clear();
				checkQueueError.clear();
				routeQueue.clear();
				routeQueueError.clear();
				changePositionQueue.clear();
				changePositionQueueError.clear();
				
				Long start = System.currentTimeMillis();
			
				Thread a = new Thread(inConverter);
				a.start();
				
				Thread b = new Thread(checker);
				b.start();
				
				Thread c = new Thread(router);
				c.start();
				
				Thread d = new Thread(changer);
				d.start();
				
				a.join();
				b.join();
				c.join();
				d.join();
				
				Long finish = System.currentTimeMillis();
				
				System.out.println(finish - start);
			}
		}
	}
}
