package math;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class MatrixHadamardProduct extends RecursiveTask<double[][]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double[][] x,y;
	
	private int start, end;

	public MatrixHadamardProduct(double[][] x, double[][] y, int start, int end) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
		
		this.start = start;
		this.end = end;
		
		if(x.length != y.length || x[0].length != y[0].length) {
			throw new RuntimeException("矩阵hadamard积的形状必须相等！");
		}
	}

	@Override
	protected double[][] compute() {
		// TODO Auto-generated method stub
		int length = end - start + 1;

		if (length < 4 || length <= x.length / 4) {

			double[][] result = hadamardProduct();
			return result;

		} else {

			int mid = (start + end + 1) >>> 1;
			MatrixHadamardProduct left = new MatrixHadamardProduct(x,y,start, mid - 1);
			MatrixHadamardProduct right = new MatrixHadamardProduct(x,y,mid, end);

			ForkJoinTask<double[][]> leftTask = left.fork();
			ForkJoinTask<double[][]> rightTask = right.fork();

			double[][] leftR = leftTask.join();
			double[][] rightR = rightTask.join();

			double[][] result = new double[leftR.length + rightR.length][leftR[0].length];
			
			for(int i = 0; i < leftR.length ; i++) {
				System.arraycopy(leftR[i], 0, result[i], 0, leftR[0].length);
			}
			
			for(int i = leftR.length ; i < rightR.length + leftR.length; i++) {
				System.arraycopy(rightR[i-leftR.length], 0, result[i], 0, rightR[0].length);
			}
			
			return result;
			
		}
	}

	private double[][] hadamardProduct() {
		double[][] result = new double[end - start + 1][x[0].length];
		
		for(int i = start; i <= end ; i ++) { //X的当前行
			for(int j = 0; j < x[0].length; j ++) { //W的当前列
				result[i-start][j] = x[i][j] * y[i][j];
			}
		}		
		return result;
	}

}
