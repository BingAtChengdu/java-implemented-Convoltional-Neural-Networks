package math;

import java.util.concurrent.ForkJoinTask;

public class WeightUpdator {

	/**
	 * 梯度下降更新滤波器
	 * @param w 滤波器
	 * @param b 偏执
	 * @param dw 滤波器梯度
	 * @param db 偏执梯度
	 */
	public static void update(double[][][][] w, double[] b, double[][][][] dw, double[] db) {

		assert w != null && b != null && dw != null && db != null;
		assert w.length == dw.length && w[0].length == dw[0].length && w[0][0].length == dw[0][0].length
				&& w[0][0][0].length == dw[0][0][0].length;

		int start = 0;
		int end = w.length - 1;
		
		FourDimenSGDWeightUpdator fdsw = new FourDimenSGDWeightUpdator(w, b, dw, db, start, end);
		ForkJoinTask<Void> a = ForkJoinPoolManager.createPool().submit(fdsw);
		a.join();
	}
	
	/**
	 * 梯度下降更新全连接层权重
	 * @param w 权重
	 * @param b 偏执
	 * @param dw 权重梯度
	 * @param db 偏执梯度
	 */
	public static void update(double[][] w, double[] b, double[][] dw, double[] db) {

		assert w != null && b != null && dw != null && db != null;
		assert w.length == dw.length && w[0].length == dw[0].length;
		
		int start = 0;
		int end = w[0].length - 1;

		TwoDimenSGDWeightUpdator fdsw = new TwoDimenSGDWeightUpdator(w, b, dw, db,start,end);
		ForkJoinTask<Void> a = ForkJoinPoolManager.createPool().submit(fdsw);
		a.join();
	}

}
