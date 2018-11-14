package math;

/**
 * 类似于python里面的reshape函数，用于重新组织矩阵
 * @author hubing
 *
 */
public class MatrixShape {

	private MatrixShape() {
		// TODO Auto-generated constructor stub
	}

	private static double[] toRow(double[][] x) {
		assert x != null;
		double[] result = new double[x.length * x[0].length];

		for (int i = 0; i < x.length; i++) {
			System.arraycopy(x[i], 0, result, i * x[0].length, x[0].length);
		}

		return result;
	}

	private static  double[] toRow(double[][][][] x) {
		assert x != null;
		double[] result = new double[x.length * x[0].length * x[0][0].length * x[0][0][0].length];

		int nSize = x[0].length * x[0][0].length * x[0][0][0].length;
		int cSize = x[0][0].length * x[0][0][0].length;
		int wSize = x[0][0][0].length;
		
		for (int n = 0; n < x.length; n++)
			for (int c = 0; c < x[0].length; c++)
				for (int i = 0; i < x[0][0].length; i++) {
						int pos = n * nSize + c * cSize + i * wSize;
						System.arraycopy(x[n][c][i], 0, result, pos, wSize);
					}

		return result;
	}
	
	private static double[][] to2DimenArray(double[] x, int n, int d){
				
		double[][] result = new double[n][d];
		for(int i = 0; i < n; i ++) {
			System.arraycopy(x, i * d, result[i], 0, d);
		}
		
		return result;
	}
	
	/**
	 * 把一维数组转为四维立方体
	 * @param x
	 * @param n
	 * @param c
	 * @param h
	 * @param w
	 * @return
	 */
	private static double[][][][] toCube(double[] x,int n, int c, int h, int w){
		
		double[][][][] result = new double[n][c][h][w];
		
		int pos = 0;
		for(int in = 0; in < n ; in ++) {
			for(int ic = 0; ic < c; ic ++) {
				for(int ih = 0; ih < h; ih ++) {
					System.arraycopy(x, pos, result[in][ic][ih],0, w); 
					pos += w;
				}
			}
		}
				
		return result;
	}
	
	/**
	 * 把二维数组转换成四维数组
	 * @param x
	 * @param n
	 * @param c
	 * @param h
	 * @param w
	 * @return
	 */

	public static double[][][][] reshape(double[][] x, int n, int c, int h, int w) {

		int nSize = x.length * x[0].length;
		
		if(w != -1) {
			assert nSize >= n * c * h * w;
		} else {
			w = nSize/(n * c * h);
		}
		
		double[] row = toRow(x);
		double[][][][] result = toCube(row,n,c,h,w);
		return result;
	}

	/**
	 * 把四维数组转换成二维数组
	 * @param x
	 * @param n
	 * @param d
	 * @return
	 */
	public static double[][] reshape(double[][][][] x, int n, int d) {
		assert x != null;
		assert n != 0 && d != 0;
		
		int nSize = x.length * x[0].length * x[0][0].length * x[0][0][0].length;
		
		if(n == -1) {
			n = nSize / d;
		}
		if(d == -1) {
			d = nSize / n;
		}
		
		assert nSize >= n * d;
		
		double[] row = toRow(x);
		
		double[][] result = to2DimenArray(row,n,d);
		
		return result;
	}
	
	public static double[] reshape(double[][][][] x) {
		assert x != null;
		
		double[] r = toRow(x);
		
		return r;
		
	}

}
