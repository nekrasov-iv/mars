package ru.sbt.ignite.payments;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class TestFile {

	public static void main(String[] args) {
		A a = new TestFile().new A();
		
		try {
			File file = new File(TestFile.class.getClassLoader().getResource("META-INF/xml/pacs.008.001.05_01.xml").toURI().getPath());
			System.out.println(file.getAbsolutePath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public class A {

	}
}
