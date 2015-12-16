package com.sbt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXPars extends DefaultHandler {

	// Doctors doc = new Doctors();
	String thisElement = "";
	String thisElementValue = "";
	
	Map <String, String> attributes = null;
	Map <String, SAXValue> result = new HashMap<>();
	
	Stack<String> stack = new Stack<>();

	 public Map <String, SAXValue> getResult() {
		 return result;
	 }

	@Override
	public void startDocument() throws SAXException {
//		System.out.println("Start parse XML...");
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
//		System.out.println("startElement " + qName);
		
		stack.push(qName);
		
		thisElement = qName;
		
		attributes = new HashMap<>();
		
		for (int i = 0; i < atts.getLength(); i++) {
			attributes.put(atts.getLocalName(i), atts.getLocalName(i));
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
//		System.out.println("endElement " + qName);
		
		if (thisElementValue != "") {
			SAXValue sss = new SAXValue();
			sss.value = thisElementValue;
			sss.attributes = attributes;
			result.put(getName(), sss);
		}
		
		stack.pop();
		
		thisElement = "";
		thisElementValue = "";
	}
	
	private String getName() {
		StringBuilder sb = new StringBuilder();
		
		Iterator<String> it = stack.iterator();
		while (it.hasNext()) {
			sb.append(it.next()).append(".");
		}
		
		return sb.toString();
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
//		System.out.println(thisElement + "; start " + start + "; length " + length);
//		String s = new String(ch, start, length);
//		System.out.println(s);
		thisElementValue = new String(ch, start, length);
		
		if (thisElement.equals("id")) {
			// doc.setId(new Integer(new String(ch, start, length)));
		}
		if (thisElement.equals("fam")) {
			// doc.setFam(new String(ch, start, length));
		}
		if (thisElement.equals("name")) {
			// doc.setName(new String(ch, start, length));
		}
		if (thisElement.equals("otc")) {
			// doc.setOtc(new String(ch, start, length));
		}
		if (thisElement.equals("dateb")) {
			// doc.setDateb(new String(ch, start, length));
		}
		if (thisElement.equals("datep")) {
			// doc.setDatep(new String(ch, start, length));
		}
		if (thisElement.equals("datev")) {
			// doc.setDatev(new String(ch, start, length));
		}
		if (thisElement.equals("datebegin")) {
			// doc.setDatebegin(new String(ch, start, length));
		}
		if (thisElement.equals("dateend")) {
			// doc.setDateend(new String(ch, start, length));
		}
		if (thisElement.equals("vdolid")) {
			// doc.setVdolid(new Integer(new String(ch, start, length)));
		}
		if (thisElement.equals("specid")) {
			// doc.setSpecid(new Integer(new String(ch, start, length)));
		}
		if (thisElement.equals("klavid")) {
			// doc.setKlavid(new Integer(new String(ch, start, length)));
		}
		if (thisElement.equals("stav")) {
			// doc.setStav(new Float(new String(ch, start, length)));
		}
		if (thisElement.equals("progid")) {
			// doc.setProgid(new Integer(new String(ch, start, length)));
		}
	}

	@Override
	public void endDocument() {
//		System.out.println("Stop parse XML...");
	}
}
