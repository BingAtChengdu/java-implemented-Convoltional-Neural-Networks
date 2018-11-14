package math;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class MatrixRandomInit extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double[][] result;
	
	private double ratio;
	
	private int start, end;

	public MatrixRandomInit(double[][] result, double ratio,int start, int end) {
		// TODO Auto-generated constructor stub
		this.result = result;
		
		this.ratio = ratio;
		
		this.start = start;
		this.end = end;
	}

	@Override
	protected void compute() {
		// TODO Auto-generated method stub
		int length = end - start + 1;

		if (length < 4 || length <= result.length / 4) {

			rand();

		} else {

			int mid = (start + end + 1) >>> 1;
			MatrixRandomInit left = new MatrixRandomInit(result,ratio,start, mid - 1);
			MatrixRandomInit right = new MatrixRandomInit(result,ratio,mid, end);

			ForkJoinTask<Void> leftTask = left.fork();
			ForkJoinTask<Void> rightTask = right.fork();

			leftTask.join();
			rightTask.join();
		}
	}

	private double[][] rand() {
		
		for(int i = start; i <= end ; i ++) { //X的当前行
			for(int j = 0; j < result[0].length; j ++) { //W的当前列
				double randn= Math.random();
				result[i][j] = randn > ratio ? 1.0d : 0.0d;
			}
		}		
		return result;
	}

}
