package com.sbt;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.sbt.iso20022.pacs_008_001_05.ActiveCurrencyAndAmount;
import com.sbt.iso20022.pacs_008_001_05.ActiveOrHistoricCurrencyAndAmount;
import com.sbt.iso20022.pacs_008_001_05.CreditTransferTransaction19;
import com.sbt.iso20022.pacs_008_001_05.Document;
import com.sbt.iso20022.pacs_008_001_05.FIToFICustomerCreditTransferV05;
import com.sbt.iso20022.pacs_008_001_05.GroupHeader49;
import com.sbt.iso20022.pacs_008_001_05.PartyIdentification43;
import com.sbt.iso20022.pacs_008_001_05.PaymentIdentification3;
import com.sbt.iso20022.pacs_008_001_05.PostalAddress6;
import com.sbt.iso20022.pacs_008_001_05.SettlementInstruction1;
import com.sbt.iso20022.pacs_008_001_05.SupplementaryData1;

public class TestUnmarshall3 {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		int i = 0;
		int j = 0;
		
		String s = null;
		boolean b = false;
		
		String xml = new String(Files.readAllBytes(Paths.get("C:\\Локальные документы\\PDO\\pacs.008.001.05\\pacs.008.001.05_01.xml")));
		StringReader reader = new StringReader(xml);
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		
		// <= 20 мс на 10000
		// serialized document 6055
		
		while (i++ < 10) {
			long t = System.currentTimeMillis();

			reader = new StringReader(xml);
			
			Document document = null;
			
			try {
				JAXBElement<Document> jaxbElement = (JAXBElement<Document>) jaxbUnmarshaller.unmarshal(reader);
				document = jaxbElement.getValue();
				
				
				while (j++ < 10000) {
						Document document2 = new Document();
						
						FIToFICustomerCreditTransferV05 fiToFICustomerCreditTransferV05 = new FIToFICustomerCreditTransferV05();
						
						GroupHeader49 grpHdr = new GroupHeader49();
						grpHdr.setMsgId(document.getFIToFICstmrCdtTrf().getGrpHdr().getMsgId());
						grpHdr.setCreDtTm(document.getFIToFICstmrCdtTrf().getGrpHdr().getCreDtTm());
						grpHdr.setNbOfTxs(document.getFIToFICstmrCdtTrf().getGrpHdr().getNbOfTxs());
						
						SettlementInstruction1 sttlmInf = new SettlementInstruction1();
						sttlmInf.setSttlmMtd(document.getFIToFICstmrCdtTrf().getGrpHdr().getSttlmInf().getSttlmMtd());
						
						grpHdr.setSttlmInf(sttlmInf);
						
						fiToFICustomerCreditTransferV05.setGrpHdr(grpHdr);
						fiToFICustomerCreditTransferV05.getCdtTrfTxInf().add(createTransaction(document.getFIToFICstmrCdtTrf().getCdtTrfTxInf().get(0)));
						fiToFICustomerCreditTransferV05.getSplmtryData().add(createSuppl());
						
						document2.setFIToFICstmrCdtTrf(fiToFICustomerCreditTransferV05);
						
						for (int a = 0; a < 20; a++) {
							b = validate(document2.getFIToFICstmrCdtTrf().getGrpHdr().getMsgId() + String.valueOf(a*100));
						}
						
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
			System.out.println("b: " + b);
			
			j = 0;
		}
	}
	
	protected static boolean validate(String s) {
		if (s == null || s.length() <= 0) {
			return false;
		}
		return true;
	}
	
	protected static CreditTransferTransaction19 createTransaction(CreditTransferTransaction19 in) {
		CreditTransferTransaction19 trans = new CreditTransferTransaction19();
		
		PaymentIdentification3 pmtId = new PaymentIdentification3();
		pmtId.setEndToEndId(in.getPmtId().getEndToEndId());
		pmtId.setTxId(in.getPmtId().getTxId());
		
		trans.setPmtId(pmtId);
		
		ActiveCurrencyAndAmount intrBkSttlmAmt = new ActiveCurrencyAndAmount();
		intrBkSttlmAmt.setCcy(in.getIntrBkSttlmAmt().getCcy());
		intrBkSttlmAmt.setValue(in.getIntrBkSttlmAmt().getValue());
		
		trans.setIntrBkSttlmAmt(intrBkSttlmAmt);
		
		trans.setIntrBkSttlmDt(in.getIntrBkSttlmDt());
		
		ActiveOrHistoricCurrencyAndAmount instdAmt = new ActiveOrHistoricCurrencyAndAmount();
		instdAmt.setCcy(in.getInstdAmt().getCcy());
		instdAmt.setValue(in.getInstdAmt().getValue());
		
		trans.setInstdAmt(instdAmt);
		
		trans.setChrgBr(in.getChrgBr());
		
		PartyIdentification43 dbtr = new PartyIdentification43();
		dbtr.setId(in.getDbtr().getId());
		dbtr.setNm(in.getDbtr().getNm());
		
		PostalAddress6 dbtradr = new PostalAddress6();
		
		dbtradr.setStrtNm(in.getDbtr().getPstlAdr().getStrtNm());
		dbtradr.setBldgNb(in.getDbtr().getPstlAdr().getBldgNb());
		dbtradr.setPstCd(in.getDbtr().getPstlAdr().getPstCd());
		dbtradr.setTwnNm(in.getDbtr().getPstlAdr().getTwnNm());
		dbtradr.setCtry(in.getDbtr().getPstlAdr().getCtry());
		
		dbtr.setPstlAdr(dbtradr);
		

		PartyIdentification43 cdtr = new PartyIdentification43();
		cdtr.setId(in.getCdtr().getId());
		cdtr.setNm(in.getCdtr().getNm());
		
		
		PostalAddress6 cdtradr = new PostalAddress6();
		
		cdtradr.setStrtNm(in.getCdtr().getPstlAdr().getStrtNm());
		cdtradr.setBldgNb(in.getCdtr().getPstlAdr().getBldgNb());
		cdtradr.setPstCd(in.getCdtr().getPstlAdr().getPstCd());
		cdtradr.setTwnNm(in.getCdtr().getPstlAdr().getTwnNm());
		cdtradr.setCtry(in.getCdtr().getPstlAdr().getCtry());
		
		cdtr.setPstlAdr(cdtradr);
		
		
		trans.setDbtr(dbtr);
		trans.setCdtr(cdtr);
		
		return trans;
	}
	
	protected static SupplementaryData1 createSuppl() {
		SupplementaryData1 suppl = new SupplementaryData1();
		
		return suppl;
	}
}
