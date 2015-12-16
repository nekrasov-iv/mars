package com.sbt;

import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.InputSource;

import com.sbt.iso20022.pacs_008_001_05.Document;

public class TestUnmarshall {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		int i = 0;
		int j = 0;

		String s = null;

		final String xml = new String(Files.readAllBytes(Paths.get("C:\\Локальные документы\\PDO\\pacs.008.001.05\\pacs.008.001.05_01.xml")));
		StringReader reader = new StringReader(xml);
		
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sf.newSchema(new File("C:\\Локальные документы\\PDO\\pacs.008.001.05\\pacs.008.001.05.xsd"));

//		Validator validator = schema.newValidator();
//		validator.validate(new StreamSource(new StringReader(xml)));
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(schema);
		jaxbUnmarshaller.setEventHandler(new MyValidationEventHandler());

		// <= 400 мс на 10000 unmarshal Без валидации
		// <= 1800 мс на 10000 unmarshal С валидацией для документов без ошибок 20 000 000
		// <= 2200 мс на 10000 unmarshal С валидацией для документов с ошибоками 16 000 000
		// serialized String 1799
		// inmemory String 3624

		while (i++ < 10) {
			long t = System.currentTimeMillis();

			Document document = null;

			while (j++ < 10000) {
//			while (j++ < 1) {

				reader = new StringReader(xml);

				JAXBElement<Document> jaxbElement = (JAXBElement<Document>) jaxbUnmarshaller.unmarshal(reader);
				document = jaxbElement.getValue();

				if (s != null) {
					s = document.getFIToFICstmrCdtTrf().toString();
				} else {
					s = document.getFIToFICstmrCdtTrf().toString();
				}

			}

			t = System.currentTimeMillis() - t;

			System.out.println("i: " + i + " t:" + t);

			System.out.println("s: " + s);

			j = 0;
		}
	}

}
