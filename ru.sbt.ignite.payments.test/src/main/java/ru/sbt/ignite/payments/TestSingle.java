package ru.sbt.ignite.payments;

import java.io.File;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
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

public class TestSingle {
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

            
            CacheConfiguration<String, SortedSet<ClientPosition>> cacheCfg4 = new CacheConfiguration<>(ClientPositionCache);

            cacheCfg4.setCacheMode(CacheMode.PARTITIONED);
            cacheCfg4.setBackups(1);
            cacheCfg4.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
            
            final IgniteCache<String, SortedSet<ClientPosition>> clientPositionCache = ignite.getOrCreateCache(cacheCfg4);
            
            
            
            final int capacity = 10000;
			

            
            
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

			for (int x = 0; x < 5; x++) {
				Long start = System.currentTimeMillis();

				int i = 0;
				
				while (i < capacity) {
					DocumentContainer dc = cache.get(i);
					
					if (dc != null) {
						// Типа проверка
						//Thread.sleep(10);
						dc.document.getFIToFICstmrCdtTrf().getGrpHdr().setNbOfTxs("0");
					}

					kg = korrespGammaCache.get("044525225");
					
					if (kg == null) {
						System.out.println("kg == null " + i);
						break;
					}
					
					AccKorrespGammaKey key = new AccKorrespGammaKey();
					key.KORRESP = kg.KRID;
					key.ISO_DIG = "810";
					key.CL_CODEWH = "1300000000";
					
					akg = accKorrespGammaCache.get(key);
					
					if (akg == null) {
						System.out.println("akg == null " + i);
						break;
					}
					
					dc.route = akg.KS;

					cache.put(i, dc);
					
					
					String ks = dc.route;

					SortedSet<ClientPosition> positions = clientPositionCache.get(ks);
					
					for (ClientPosition p : positions) {
						if (p.position.compareTo(dc.document.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getIntrBkSttlmAmt().getValue()) < 0) {
							System.out.println("insufficient position " + i);
							break;
						}
					}
					
					for (ClientPosition p : positions) {
						p.position = p.position.subtract(dc.document.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0).getIntrBkSttlmAmt().getValue());
					}
					
					clientPositionCache.put(ks, positions);
					
					i++;
				}
				
				Long finish = System.currentTimeMillis();
				
				System.out.println(x + ": " + (finish - start));
			}
		}
	}
}
