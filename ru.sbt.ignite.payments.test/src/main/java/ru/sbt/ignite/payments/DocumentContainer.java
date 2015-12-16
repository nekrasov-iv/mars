package ru.sbt.ignite.payments;

import java.io.Serializable;

import com.sbt.iso20022.pacs_008_001_05.Document;

public class DocumentContainer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Document document;
	public String route;
}
