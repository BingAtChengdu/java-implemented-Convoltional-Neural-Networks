package math;

public class Operate {
	public static double[] matrixMultiple(double[] x, double[][] w, double[] b) throws Exception {
		if (x == null || w == null || b == null) {
			return null;
		}

		if (x.length != w.length || b.length != w.length) {
			throw new Exception("矩阵运算行列必须相等！");
		}

		/*
		 * 矩阵运算
		 */
		int i = 0;
		int column = w[0].length;
		int row = x.length;

		double[] result = new double[w[0].length];
		for (; i < column; i++) {

			double r = 0.0d;
			int c = 0;

			for (; c < row; c++) {
				r = x[c] * w[c][i] + r;

			}
			result[i] = r + b[i];

			// System.out.println(i + " 的总和是草泥马的：" + result[i] );
		}
		return result;
	}

	/**
	 * 激活函数
	 * 
	 * @param x
	 * @return
	 */
	public static double[][] sigmoid(double[][] x) {
			
		if (x == null) {
			return null;
		}
		
		double[][] a = new double[x.length][x[0].length];

		for (int i = 0; i < x.length; i++)
			for (int j = 0; j < x[0].length; j++) {
				// x[i][j] = x[i][j] / 100;
				a[i][j] = 1.0d / (1.0d + Math.exp(-x[i][j]));
			}
		return a;
	}

	/**
	 * 分类激活函数
	 * 
	 * @param a
	 * @return
	 */
	public static double[][] softmax(double[][] a) {
		if (a == null) {
			return null;
		}
		double[][] y = new double[a.length][a[0].length];
		double temp_max = 0;
		double[] max = new double[a.length];
		// 求最大值
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				temp_max = Math.max(a[i][j], temp_max);
			}
			max[i] = temp_max;
		}

		double[] exp_sum = new double[a.length];

		for (int i = 0; i < a.length; i++) {

			for (int j = 0; j < a[0].length; j++) {
				y[i][j] = Math.exp(a[i][j] - max[i]);
				exp_sum[i] += y[i][j];
			}
		}

		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[0].length; j++) {
				{
					y[i][j] = y[i][j] / exp_sum[i];
				}
			}

		return y;
	}

	/**
	 * 均方误差
	 * 
	 * @param y
	 * @param t
	 * @return
	 */
	public static double meanSquaredError(double[] y, double[] t) {
		double sum = 0.0f;
		for (int i = 0; i < y.length; i++) {
			sum += (Math.pow((y[i] - t[i]), 2));
		}
		return sum / 2;
	}

	/**
	 * 交叉熵误差 one hot 版
	 * 
	 * @return
	 */
	public static double crossEntropyError_onehot(double[][] y, double[][] t) {
		if (y == null || t == null) {
			new RuntimeException("计算交叉熵的参数不能为空 ！");
		}
		double sum = 0;

		double delta = 1E-7;
		for (int i = 0; i < y.length; i++) {
			for (int j = 0; j < y[0].length; j++) {
				double temp = Math.log(y[i][j] + delta);
				if (Double.isNaN(temp)) {
					continue;
				}
				sum += t[i][j] * Math.log(y[i][j] + delta);
			}
		}
		return - (sum/y.length);
	}
	
	/**
	 * 交叉熵误差版
	 * 
	 * @return
	 */
	public static double crossEntropyError(double[][] y, double[] t) {
		if (y == null || t == null) {
			new RuntimeException("计算交叉熵的参数不能为空 ！");
		}
		double sum = 0;

		double delta = 1E-7;
		for (int i = 0; i < y.length; i++) {
			for (int j = 0; j < y[0].length; j++) {
				double temp = Math.log(y[i][j] + delta);
				if (Double.isNaN(temp)) {
					continue;
				}
				sum += t[i] * Math.log(y[i][j] + delta);
			}
		}
		return - (sum/y.length);
	}


	public static double[][] addB(double[][] a, double[] b) {
		double[][] x = new double[a.length][a[0].length];
		
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[0].length; j++) {
				x[i][j] = a[i][j] + b[j];
			}
		return x;
	}

}
