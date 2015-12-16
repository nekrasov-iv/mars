package com.sbt;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.sbt.iso20022.pacs_008_001_05.Document;

public class TestUnmarshall2 {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
		final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		String xml = new String(Files.readAllBytes(Paths.get("C:\\Локальные документы\\PDO\\pacs.008.001.05\\pacs.008.001.05_01.xml")));
			
			
		int i = 0;
		int j = 0;

		String s = null;

		StringReader reader = new StringReader(xml);

		// <= 10 мс на 10000
		// serialized document 6055
		// inmemory

		while (i++ < 10) {
			long t = System.currentTimeMillis();

			reader = new StringReader(xml);

			Document document = null;

			try {
				JAXBElement<Document> jaxbElement = (JAXBElement<Document>) jaxbUnmarshaller.unmarshal(reader);
				document = jaxbElement.getValue();

				while (j++ < 10000) {
					Document document2 = document;

					if (s != null) {
						s = document2.getFIToFICstmrCdtTrf().toString();
					} else {
						s = document2.getFIToFICstmrCdtTrf().toString();
					}
				}
			} catch (JAXBException e) {
				e.printStackTrace();
			}

			t = System.currentTimeMillis() - t;

			System.out.println("i: " + i + " t:" + t);

			System.out.println("s: " + s);

			j = 0;
		}
	}
}
