package com.sbt;

public class MemoryTestBench {
//	public long calculateMemoryUsage(ObjectFactory factory) {
//		Object handle = factory.makeObject();
//		long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//		long mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//		handle = null;
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//		handle = factory.makeObject();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		System.gc();
//		mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//		return mem1 - mem0;
//	}
//
//	public void showMemoryUsage(ObjectFactory factory) {
//		long mem = calculateMemoryUsage(factory);
//		System.out.println(factory.getClass().getName() + " produced " + factory.makeObject().getClass().getName()
//				+ " which took " + mem + " bytes");
//	}

	
	  public long calculateMemoryUsage(ObjectFactory factory) {
		    Object handle = factory.makeObject();
		    long memory = usedMemory();
		    handle = null;
		    lotsOfGC();
		    memory = usedMemory();
		    handle = factory.makeObject();
		    lotsOfGC();
		    return usedMemory() - memory;
		  }

		  private long usedMemory() {
		    return Runtime.getRuntime().totalMemory() -
		        Runtime.getRuntime().freeMemory();
		  }

		  private void lotsOfGC() {
		    for (int i = 0; i < 20; i++) {
		      System.gc();
		      try {
		        Thread.sleep(100);
		      } catch (InterruptedException e) {
		        Thread.currentThread().interrupt();
		      }
		    }
		  }

		  public void showMemoryUsage(ObjectFactory factory) {
		    long mem = calculateMemoryUsage(factory);
		    System.out.println(
		        factory.getClass().getSimpleName() + " produced " +
		            factory.makeObject().getClass().getSimpleName() +
		            " which took " + mem + " bytes");
		  }
	
}
