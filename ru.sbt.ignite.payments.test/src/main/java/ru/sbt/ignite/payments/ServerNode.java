package ru.sbt.ignite.payments;

import org.apache.ignite.Ignition;

public class ServerNode {

	public static void main(String[] args) {
		Ignition.start("config/example-ignite.xml");

	}

}
