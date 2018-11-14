package math;

import java.util.concurrent.ForkJoinPool;

public class ForkJoinPoolManager {

	private final static ForkJoinPool forkJoinPool = new ForkJoinPool();
	private ForkJoinPoolManager() {
		// TODO Auto-generated constructor stub
	}
	
	public static ForkJoinPool createPool() {
		return forkJoinPool;
	}

}
