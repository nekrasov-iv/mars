package ru.sbt.ignite.payments;

import java.io.File;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.lang.IgniteFuture;

import com.sbt.iso20022.pacs_008_001_05.Document;

public class TestArrayBatch {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		// test test test
		Ignition.setClientMode(true);

		final String CACHE_NAME = "CACHE_NAME";

		final String KorrespGammaCache = "KorrespGamma";

		final String AccKorrespGammaCache = "AccKorrespGamma";

		final String ClientPositionCache = "ClientPositionCache";

		try (final Ignite ignite = Ignition.start("config/example-ignite.xml")) {
			CacheConfiguration<Integer, DocumentContainer> cacheCfg = new CacheConfiguration<>(CACHE_NAME);

			cacheCfg.setCacheMode(CacheMode.PARTITIONED);
			cacheCfg.setBackups(1);
			cacheCfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);

			final IgniteCache<Integer, DocumentContainer> cache = ignite.getOrCreateCache(cacheCfg);
			CacheConfiguration<String, KorrespGamma> cacheCfg2 = new CacheConfiguration<>(KorrespGammaCache);

			cacheCfg2.setCacheMode(CacheMode.REPLICATED);
			cacheCfg2.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);

			final IgniteCache<String, KorrespGamma> korrespGammaCache = ignite.getOrCreateCache(cacheCfg2);

			CacheConfiguration<AccKorrespGammaKey, AccKorrespGamma> cacheCfg3 = new CacheConfiguration<>(
					AccKorrespGammaCache);

			cacheCfg3.setCacheMode(CacheMode.REPLICATED);
			cacheCfg3.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);

			final IgniteCache<AccKorrespGammaKey, AccKorrespGamma> accKorrespGammaCache = ignite
					.getOrCreateCache(cacheCfg3);

			CacheConfiguration<String, Collection<ClientPosition>> cacheCfg4 = new CacheConfiguration<>(
					ClientPositionCache);

			cacheCfg4.setCacheMode(CacheMode.PARTITIONED);
			cacheCfg4.setBackups(1);
			cacheCfg4.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);

			final IgniteCache<String, Collection<ClientPosition>> clientPositionCache = ignite
					.getOrCreateCache(cacheCfg4);

			final int capacity = 10000;

			// Очередь проверок
			final Integer[] checkQueue = new Integer[capacity];
			final Counter putCheckQueueCounter = new Counter();
			final Counter getCheckQueueCounter = new Counter();

			// Очередь маршрутизации
			final Integer[] routeQueue = new Integer[capacity];
			final Counter putRouteQueueCounter = new Counter();
			final Counter getRouteQueueCounter = new Counter();

			// Очередь изменения позиции
			final Integer[] changePositionQueue = new Integer[capacity];
			final Counter putChangePositionQueueCounter = new Counter();
			final Counter getChangePositionQueueCounter = new Counter();

			File file = new File(TestArrayBatch.class.getClassLoader()
					.getResource("META-INF/xml/pacs.008.001.05_01.xml").toURI().getPath());
			final String xml = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(new File(TestArrayBatch.class.getClassLoader()
					.getResource("META-INF/xml/pacs.008.001.05.xsd").toURI().getPath()));

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

			List<ClientPosition> cps = new ArrayList<>();
			Collections.sort(cps);

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

			cps = new ArrayList<>();
			Collections.sort(cps);

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

			int aaa = 0;

			while (aaa < capacity) {
				StringReader reader = new StringReader(xml);

				@SuppressWarnings("unchecked")
				JAXBElement<Document> jaxbElement = (JAXBElement<Document>) jaxbUnmarshaller.unmarshal(reader);
				Document document = jaxbElement.getValue();

				DocumentContainer dc = new DocumentContainer();
				dc.document = document;

				cache.put(aaa, dc);
				aaa++;
			}

			Runnable inConverter = new Runnable() {
				@Override
				public void run() {
					long t = System.currentTimeMillis();
					try {

						int i = 0;
						while (putCheckQueueCounter.counter < capacity) {
							checkQueue[i] = i;
							i++;

							if (putCheckQueueCounter.counter % 100 == 0) {
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									throw new RuntimeException(e);
								}
							}

							putCheckQueueCounter.counter++;
						}
					} finally {
						t = System.currentTimeMillis() - t;
						System.out.println("converter:" + t);
					}
				}
			};

			Runnable checker = new Runnable() {
				@Override
				public void run() {
					int count = 0;
					long a = 0;
					long t = System.currentTimeMillis();

					while (true) {

						int z = putCheckQueueCounter.counter;

						if (getCheckQueueCounter.counter < z) {

							t = System.currentTimeMillis();

							int batchSize = z - getCheckQueueCounter.counter;

							Integer[] dest = new Integer[batchSize];
							System.arraycopy(checkQueue, getCheckQueueCounter.counter, dest, 0, dest.length);

							count += batchSize;
							getCheckQueueCounter.counter += batchSize;

							Map<ClusterNode, Collection<Integer>> map = ignite.cluster().mapKeysToNodes(CACHE_NAME,
									Arrays.asList(dest));

							Collection<IgniteFuture<?>> futs = new ArrayList<>(map.size());

							for (Map.Entry<ClusterNode, Collection<Integer>> entry : map.entrySet()) {
								CheckerBatch callable = new CheckerBatch(ignite);
								callable.setList(entry.getValue());

								IgniteCompute compute = ignite.compute(ignite.cluster().forNode(entry.getKey()))
										.withAsync();

								compute.call(callable);
								futs.add(compute.future());
							}

							Map<Integer, Integer> result = new HashMap<>(batchSize);
							for (IgniteFuture<?> fut : futs) {
								result.putAll((Map<Integer, Integer>) fut.get());
							}

							for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
								if (entry.getValue() < 0) {
									System.out.println("error check document with ID " + entry.getKey());
								} else {
									routeQueue[putRouteQueueCounter.counter++] = entry.getKey();
								}
							}

							a += System.currentTimeMillis() - t;

							if (count == capacity) {
								break;
							}
						}
					}

					System.out.println("checker:" + a);

				}
			};

			final RouterBatch routerBatcherCallable = new RouterBatch(ignite);

			Runnable router = new Runnable() {
				@Override
				public void run() {
					int count = 0;
					long a = 0;
					long t = System.currentTimeMillis();

					while (true) {

						int z = putRouteQueueCounter.counter;

						if (getRouteQueueCounter.counter < z) {

							t = System.currentTimeMillis();

							int batchSize = z - getRouteQueueCounter.counter;

							Integer[] dest = new Integer[batchSize];
							System.arraycopy(routeQueue, getRouteQueueCounter.counter, dest, 0, dest.length);

							count += batchSize;
							getRouteQueueCounter.counter += batchSize;

							Map<ClusterNode, Collection<Integer>> map = ignite.cluster().mapKeysToNodes(CACHE_NAME,
									Arrays.asList(dest));

							Collection<IgniteFuture<?>> futs = new ArrayList<>(map.size());

							for (Map.Entry<ClusterNode, Collection<Integer>> entry : map.entrySet()) {
								routerBatcherCallable.setList(entry.getValue());

								IgniteCompute compute = ignite.compute(ignite.cluster().forNode(entry.getKey()))
										.withAsync();

								compute.call(routerBatcherCallable);
								futs.add(compute.future());
							}

							Map<Integer, Integer> result = new HashMap<>(batchSize);
							for (IgniteFuture<?> fut : futs) {
								result.putAll((Map<Integer, Integer>) fut.get());
							}

							for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
								if (entry.getValue() < 0) {
									System.out.println("error check document with ID " + entry.getKey());
								} else {
									changePositionQueue[putChangePositionQueueCounter.counter++] = entry.getKey();
								}
							}

							a += System.currentTimeMillis() - t;

							if (count == capacity) {
								break;
							}
						}
					}

					System.out.println("router:" + a);
				}
			};

			Runnable changer = new Runnable() {
				@Override
				public void run() {
					int count = 0;
					long a = 0;
					long t = System.currentTimeMillis();

					while (true) {

						int z = putChangePositionQueueCounter.counter;

						if (getChangePositionQueueCounter.counter < z) {

							// t = System.currentTimeMillis();
							//
							// Map<ClusterNode, List<Integer>> map = new
							// HashMap<>();

							// while (getChangePositionQueueCounter.counter < z)
							// {
							// count++;
							//
							// ClusterNode node =
							// ignite.cluster().mapKeyToNode(CACHE_NAME,
							// changePositionQueue[getChangePositionQueueCounter.counter]);
							//
							// List<Integer> list = map.get(node);
							// if (list == null) {
							// list = new ArrayList<>();
							// map.put(node, list);
							// }
							// list.add(changePositionQueue[getChangePositionQueueCounter.counter]);
							//
							// getChangePositionQueueCounter.counter++;
							// }
							//
							// Collection<IgniteFuture<?>> futs = new
							// ArrayList<>(map.size());

							t = System.currentTimeMillis();

							int batchSize = z - getChangePositionQueueCounter.counter;

							Integer[] dest = new Integer[batchSize];
							System.arraycopy(routeQueue, getChangePositionQueueCounter.counter, dest, 0, dest.length);

							count += batchSize;
							getChangePositionQueueCounter.counter += batchSize;

							Map<ClusterNode, Collection<Integer>> map = ignite.cluster().mapKeysToNodes(CACHE_NAME,
									Arrays.asList(dest));

							Collection<IgniteFuture<?>> futs = new ArrayList<>(map.size());

							for (Map.Entry<ClusterNode, Collection<Integer>> entry : map.entrySet()) {
								ChangerBatch callable = new ChangerBatch(ignite);
								callable.setList(entry.getValue());

								IgniteCompute compute = ignite.compute(ignite.cluster().forNode(entry.getKey()))
										.withAsync();

								compute.call(callable);
								futs.add(compute.future());
							}

							Map<Integer, Integer> result = new HashMap<>(batchSize);

							// Wait for completion of all futures.
							for (IgniteFuture<?> fut : futs) {
								result.putAll((Map<Integer, Integer>) fut.get());
							}

							for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
								if (entry.getValue() < 0) {
									System.out.println("error check document with ID " + entry.getKey());
								} else {
									// // TODO что-то сделать после изменения
									// //
									// changePositionQueue[putChangePositionQueueCounter.counter++]
									// // = entry.getKey();
								}
							}

							a += System.currentTimeMillis() - t;

							if (count == capacity) {
								break;
							}
						}
					}

					System.out.println("changer:" + a);
				}
			};

			for (int x = 0; x < 10; x++) {
				Long start = System.currentTimeMillis();

				putCheckQueueCounter.counter = 0;
				getCheckQueueCounter.counter = 0;

				putRouteQueueCounter.counter = 0;
				getRouteQueueCounter.counter = 0;

				putChangePositionQueueCounter.counter = 0;
				getChangePositionQueueCounter.counter = 0;

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

				System.out.println(x + ": " + (finish - start));
			}
		}
	}
}
