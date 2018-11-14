package math;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class MatrixMultiple extends RecursiveTask<double[][]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double[][] x;
	private double[][] w;

	private int start;
	private int end;

	/**
	 * ���췽��
	 * 
	 * @param x
	 * @param w
	 * @param b
	 */
	public MatrixMultiple(double[][] x, double[][] w,  int start, int end) {

		if (x == null || w == null ) {
			throw new RuntimeException("��������Ϊ�գ�");
		}

		if (x[0].length != w.length) {
			System.out.println(x.length);
			System.out.println(w.length);
			throw new RuntimeException("�����������б�����ȣ�");
		}

		if (end < start) {
			throw new RuntimeException("���������ַ������ڿ�ʼ��ַ��");
		}

		this.x = x;
		this.w = w;

		this.start = start;
		this.end = end;
	}

	@Override
	protected double[][] compute() {
		// TODO Auto-generated method stub
		int length = end - start + 1;

		if (length < 4 || length <= x.length / 4) {

			return matrixMultiple();

		} else {
			int mid = (start + end + 1) >>> 1;
			MatrixMultiple left = new MatrixMultiple(x, w, start, mid - 1);
			MatrixMultiple right = new MatrixMultiple(x, w, mid, end);

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

	private double[][] matrixMultiple() {

		/*
		 * ��������
		 */

		double[][] result = new double[end - start + 1][w[0].length];
		
		for(int i = start; i <= end ; i ++) { //X�ĵ�ǰ��
			for(int j = 0; j < w[0].length; j ++) { //W�ĵ�ǰ��
				double sum = 0.0d;
				int k = 0;
				for(; k < x[0].length ; k ++) { //��������ĵ�ǰ��������
					sum += x[i][k] * w[k][j];
				}
				result[i-start][j] = sum;
			}
		}

		return result;
	}

}
