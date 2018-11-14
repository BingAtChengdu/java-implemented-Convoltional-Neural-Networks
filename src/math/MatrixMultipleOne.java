package math;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class MatrixMultipleOne extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double[][] x,result;
	
	private double one;
	
	private int start, end;

	public MatrixMultipleOne(double[][] x, double[][] result, double one,int start, int end) {
		// TODO Auto-generated constructor stub
		this.x = x;
		
		this.result = result;
		
		this.one = one;
		
		this.start = start;
		this.end = end;
	}

	@Override
	protected void compute() {
		// TODO Auto-generated method stub
		int length = end - start + 1;

		if (length < 4 || length <= result.length / 4) {

			mult();

		} else {

			int mid = (start + end + 1) >>> 1;
			MatrixMultipleOne left = new MatrixMultipleOne(x,result,one,start, mid - 1);
			MatrixMultipleOne right = new MatrixMultipleOne(x,result,one,mid, end);

			ForkJoinTask<Void> leftTask = left.fork();
			ForkJoinTask<Void> rightTask = right.fork();

			leftTask.join();
			rightTask.join();
		}
	}

	private void mult() {		
		for(int i = start; i <= end ; i ++) { //X的当前行
			for(int j = 0; j < x[0].length; j ++) { //W的当前列
				result[i][j] = x[i][j] * one;
			}
		}		
	}

}
