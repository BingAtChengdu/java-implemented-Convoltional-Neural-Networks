package math;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

import network.Network;

/**
 * 二维全权重SGD更新
 * 
 * @author hubing
 *
 */
public class TwoDimenSGDWeightUpdator extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double[][] w, dw;

	private double[] b, db;

	private int start, end;

	public TwoDimenSGDWeightUpdator(double[][] w, double[] b, double[][] dw, double[] db,int start,int end) {
		this.w = w;
		this.b = b;
		this.dw = dw;
		this.db = db;
		this.start = start;
		this.end = end;
	}

	@Override
	protected void compute() {

		assert w != null && b != null && dw != null && db != null;

		// TODO Auto-generated method stub
		int length = end - start + 1;

		if (length < 4 || length <= w[0].length / 4) {

			update();

		} else {

			int mid = (start + end + 1) >>> 1;
			TwoDimenSGDWeightUpdator left = new TwoDimenSGDWeightUpdator(w, b, dw,db, start, mid - 1);
			TwoDimenSGDWeightUpdator right = new TwoDimenSGDWeightUpdator(w, b, dw,db,  mid, end);

			ForkJoinTask<Void> leftTask = left.fork();
			ForkJoinTask<Void> rightTask = right.fork();

			leftTask.join();
			rightTask.join();
		}
	}

	private void update() {
		for(int j = start; j <= end; j ++) {
			for(int i = 0; i < w.length; i ++ ) {
				w[i][j] -= Network.lr * dw[i][j];
			}
			b[j] -= db[j] * Network.lr;	
		}
	}

}
