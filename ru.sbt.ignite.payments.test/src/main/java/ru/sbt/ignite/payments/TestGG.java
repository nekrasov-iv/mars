package ru.sbt.ignite.payments;

import java.util.Arrays;
import java.util.Collection;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.lang.IgniteClosure;

public class TestGG {

	public static void main(String[] args) throws Exception {
		try (Ignite ignite = Ignition.start("C:\\Distr\\gridgain-community-fabric-1.4.1\\examples\\config\\example-ignite.xml")) {
			ClusterGroup grp = ignite.cluster().forRemotes();
			
			Collection<Integer> res = ignite.compute(grp).apply(new IgniteClosure<String, Integer>() {
				public Integer apply(String word) {
					System.out.println("counting characters in word " + word);
					return word.length();
				}
			}, Arrays.asList("How many characters".split(" ")));
			
			int total = 0;
			
			for (int i : res) {
				total += i;
			}
			
			System.out.println("total " + total);
		}
	}

}
