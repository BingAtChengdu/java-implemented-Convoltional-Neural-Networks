package math;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

import network.BatchNorm;

/**
 * 把数据normlize正则化
 * @author hubing
 *
 */
public class Normalization extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BatchNorm bn;
	private int start;
	private int end;
	private double[][] x;
	
	public Normalization(BatchNorm bn, double[][] x, int start, int end) {
		// TODO Auto-generated constructor stub
		this.bn = bn;
		this.start = start;
		this.end = end;
		this.x = x;
	}

	@Override
	protected void compute() {
		// TODO Auto-generated method stub
		int length = end - start + 1;

		if (length < 4 || length <= x.length / 4) {

			mean();

		} else {

			int mid = (start + end + 1) >>> 1;
			Normalization left = new Normalization(bn,x, start, mid - 1);
			Normalization right = new Normalization(bn,x, mid, end);

			ForkJoinTask<Void> leftTask = left.fork();
			ForkJoinTask<Void> rightTask = right.fork();

			leftTask.join();
			rightTask.join();
			
		}
	}

	private void mean() {
		
		double[][] xmu = bn.getXmu();
		double[] mu = bn.getMu();
		double[] sigma = bn.getSigma();
		double[] stardardDev = bn.getStardardDev();
		
		for (int i = start; i <= end; i++) {
			for (int j = 0; j < x.length; j++) {
				xmu[j][i] = x[j][i] - mu[i];
				sigma[i] += Math.pow(xmu[j][i], 2);
			}
			sigma[i] = sigma[i] / x.length;
		}

		double[][] normalized = bn.getNormalized();

		for (int i = start; i <= end; i++) {
			for (int j = 0; j < x.length; j++) {
				stardardDev[i] = Math.sqrt(sigma[i] + 1E-7);
				normalized[j][i] = (x[j][i] - mu[i]) / stardardDev[i];
			}
		}
		return;
	}

}
