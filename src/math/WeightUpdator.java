package math;

import java.util.concurrent.ForkJoinTask;

public class WeightUpdator {

	/**
	 * �ݶ��½������˲���
	 * @param w �˲���
	 * @param b ƫִ
	 * @param dw �˲����ݶ�
	 * @param db ƫִ�ݶ�
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
	 * �ݶ��½�����ȫ���Ӳ�Ȩ��
	 * @param w Ȩ��
	 * @param b ƫִ
	 * @param dw Ȩ���ݶ�
	 * @param db ƫִ�ݶ�
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
