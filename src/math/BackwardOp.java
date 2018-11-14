package math;

public class BackwardOp {

	// TODO Auto-generated constructor stub
	/**
	 * 激活函数反向传播求导数
	 * @param result
	 * @param dout
	 * @return
	 */
	public static double[][] sigmoid(double[][] result, double[][] dout) {

		for (int i = 0; i < result.length; i++)
			for (int j = 0; j < result[0].length; j++) {
				dout[i][j] = dout[i][j] * (1 - result[i][j]) * result[i][j];
			}
		return dout;
	}
	
	
	public static double[][] arrayCopy(double[][] x){
		if(x == null) {
			throw new RuntimeException("数组不能为空！！！");
		}
	
		double[][] xt = new double[x.length][x[0].length];
		
		for(int i = 0; i < x.length ; i ++) {
			System.arraycopy(x[i], 0, xt[i], 0, x[0].length);
		}
		
		return xt;
		
	}
	
	public static double[] sumMatrix(double[][] b) {
		
		double[] sum = new double[b[0].length];
		
		for(int i = 0 ; i < b[0].length ; i ++)
			for(int j = 0 ; j < b.length ; j ++)
		{
			sum[i] += b[j][i];
		}
		return sum;
	}
	
	public static double sumMatrixAll(double[][] b) {
		
		double sum = 0.0d;
		
		for(int i = 0 ; i < b[0].length ; i ++)
			for(int j = 0 ; j < b.length ; j ++)
		{
			sum += b[j][i];
		}
		return sum;
	}
	public static double[][] softmaxWithLoss(double[][] dout){
		return dout;
	}
	
	public static double[][] matrixMultipy(double[] x,double[][] y){
		if(x.length != y[0].length) {
			throw new RuntimeException("矩阵相乘列必须相等！");
		}
		double[][] result = new double[y.length][y[0].length];
		
		for(int i = 0; i < y.length; i ++) {
			for(int j = 0; j < y[0].length; j ++) {
				result[i][j] = x[j]*y[i][j];
			}
		}		
		return result;
	}

}
