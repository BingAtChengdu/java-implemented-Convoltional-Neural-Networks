package math;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * 四维矩阵转置
 * @author hubing
 *
 */
public class FourDimensionMatrixTransposion extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double[][][][] x, result;

	private int start, end;
	private int[] seq;

	public FourDimensionMatrixTransposion(double[][][][] x, double[][][][] result, int[] seq, int start, int end) {

		assert x != null;
		assert result != null;
		assert seq != null;

		// TODO Auto-generated constructor stub
		this.result = result;

		this.x = x;

		this.start = start;
		this.end = end;

		this.seq = seq;

	}

	@Override
	protected void compute() {
		// TODO Auto-generated method stub
		int length = end - start + 1;

		if (length < 4 || length <= x.length / 4) {

			transpose();

		} else {

			int mid = (start + end + 1) >>> 1;
			FourDimensionMatrixTransposion left = new FourDimensionMatrixTransposion(x, result, seq, start, mid - 1);
			FourDimensionMatrixTransposion right = new FourDimensionMatrixTransposion(x, result, seq, mid, end);

			ForkJoinTask<Void> leftTask = left.fork();
			ForkJoinTask<Void> rightTask = right.fork();

			leftTask.join();
			rightTask.join();
		}
	}

	private void transpose() {

		// 使用Integer，即指针形式实现转置与被转置矩阵下标的联动

		int[] sorted = new int[4];
		int[] sortIdx = new int[4];
		
		for(int k = 0 ; k < 4 ; k ++) {
			sortIdx[seq[k]] = k;  //存入调换后轴的对应索引
		}

		for (int n = start ; n <= end; n++) { // X的当前行
			sorted[sortIdx[0]] = n;
			for (int c = 0; c < x[0].length; c++) { // W的当前列
				sorted[sortIdx[1]] = c;
				for (int i = 0; i < x[0][0].length; i++) {
					sorted[sortIdx[2]] = i;
					for (int j = 0; j < x[0][0][0].length; j++) {
						sorted[sortIdx[3]] = j;
						result[sorted[0]][sorted[1]][sorted[2]][sorted[3]] = x[n][c][i][j];
					}
				}
			}

		}
	}

}
