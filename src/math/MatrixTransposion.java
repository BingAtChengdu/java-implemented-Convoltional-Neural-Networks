package math;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
/**
 * 二维矩阵转置
 * @author hubing
 *
 */
public class MatrixTransposion extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double[][] x,result;
	
	private int start, end;

	public MatrixTransposion(double[][] x, double[][] result,int start, int end) {
		
		
		assert x != null;
		assert result != null;
		
		this.result = result;
		
		this.x = x;
		
		this.start = start;
		this.end = end;
	}

	@Override
	protected void compute() {
		int length = end - start + 1;

		if (length < 4 || length <= result.length / 4) {

			transpose();

		} else {

			int mid = (start + end + 1) >>> 1;
			MatrixTransposion left = new MatrixTransposion(x, result, start, mid - 1);
			MatrixTransposion right = new MatrixTransposion(x, result,mid, end);

			ForkJoinTask<Void> leftTask = left.fork();
			ForkJoinTask<Void> rightTask = right.fork();

			leftTask.join();
			rightTask.join();
		}
	}

	private void transpose() {
		
		for(int i = start; i <= end ; i ++) { //X的当前行
			for(int j = 0; j < x[0].length; j ++) { //W的当前列
				result[j][i] = x[i][j];
			}
		}		
	}

}
