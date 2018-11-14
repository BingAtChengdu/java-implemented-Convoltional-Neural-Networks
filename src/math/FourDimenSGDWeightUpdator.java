package math;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

import network.Network;

/**
 * ËÄÎ¬ÂË²¨Æ÷SGD¸üÐÂ
 * 
 * @author hubing
 *
 */
public class FourDimenSGDWeightUpdator extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double[][][][] w, dw;

	private double[] b, db;

	private int start, end;

	public FourDimenSGDWeightUpdator(double[][][][] w, double[] b, double[][][][] dw, double[] db, int start, int end) {
		this.w = w;
		this.b = b;
		this.dw = dw;
		this.db = db;
		this.start = start;
		this.end = end;
	}

	@Override
	protected void compute() {

		// TODO Auto-generated method stub
		int length = end - start + 1;

		if (length < 4 || length <= w.length / 4) {

			update();

		} else {

			int mid = (start + end + 1) >>> 1;
			FourDimenSGDWeightUpdator left = new FourDimenSGDWeightUpdator(w, b, dw, db, start, mid - 1);
			FourDimenSGDWeightUpdator right = new FourDimenSGDWeightUpdator(w, b, dw, db, mid, end);

			ForkJoinTask<Void> leftTask = left.fork();
			ForkJoinTask<Void> rightTask = right.fork();

			leftTask.join();
			rightTask.join();
		}
	}

	private void update() {
		for (int n = start; n <= end; n++) {
			for (int c = 0; c < w[0].length; c++)
				for (int i = 0; i < w[0][0].length; i++)
					for (int j = 0; j < w[0][0][0].length; j++) {
						w[n][c][i][j] -= Network.lr * dw[n][c][i][j];
					}
			b[n] -= db[n] * Network.lr;
		}
	}
	
}
