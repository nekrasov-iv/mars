package com.sbt;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

public class TestUnmarshall4 {
	public static void main(String[] args) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
//		SAXPars saxp = new SAXPars();
		SAXPars2 saxp = new SAXPars2();

		String xml = new String(
				Files.readAllBytes(Paths.get("C:\\Локальные документы\\PDO\\pacs.008.001.05\\pacs.008.001.05_01.xml")));

		int i = 0;
		int j = 0;

		String s = null;

		// saxpars <= 700 мс на 10000
		// saxpars2 <= 300 мс на 10000
		// serialized map 4273

		while (i++ < 10) {
			long t = System.currentTimeMillis();

			while (j++ < 10000) {
				parser.parse(new InputSource(new StringReader(xml)), saxp);

				if (s != null) {
					s = saxp.getResult().toString();
				} else {
					s = saxp.getResult().toString();
				}
			}

			t = System.currentTimeMillis() - t;

			System.out.println("i: " + i + " t:" + t);

			System.out.println("s: " + s);

			j = 0;
		}

		// for (Map.Entry<String, SAXValue> s : saxp.getResult().entrySet() ) {
		// System.out.println(s.getKey() + ":" + s.getValue().value);
		// }
	}
}
