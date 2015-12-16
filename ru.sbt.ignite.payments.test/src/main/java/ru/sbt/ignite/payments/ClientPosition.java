package ru.sbt.ignite.payments;

import java.math.BigDecimal;
import java.util.Date;

public class ClientPosition implements Comparable<ClientPosition> {
	public String KS;
	public Date date;
	public BigDecimal position;

	@Override
	public int compareTo(ClientPosition o) {
		if (o == null) {
			return -1;
		}
		
		return date.compareTo(o.date);
	}
	
}
