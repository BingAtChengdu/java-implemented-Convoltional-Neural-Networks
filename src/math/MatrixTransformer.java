package math;

import java.util.concurrent.ForkJoinTask;

import network.BatchNorm;

public class MatrixTransformer {
	

	private MatrixTransformer() {
	}
	public static double[][] transpose(double[][] trans) {

		double[][] transposed = new double[trans[0].length][trans.length];

		MatrixTransposion mt = new MatrixTransposion(trans, transposed, 0, trans.length - 1);

		ForkJoinTask<Void> a = ForkJoinPoolManager.createPool().submit(mt);

		a.join();

		return transposed;
	}
	
	public static double[][][][] transpose(double[][][][] trans,int[] seq) {

		assert trans != null;
		assert seq != null;
		assert seq.length == 4;
		
		
		int n = trans.length;
		int c = trans[0].length;
		int i = trans[0][0].length;
		int j = trans[0][0][0].length;
		
		int[] sort =  {n,c,i,j};
		int[] sorted = new int[sort.length];
		
		for(int k = 0; k < seq.length ; k ++) {
			sorted[k] = sort[seq[k]];
		}
		
		double[][][][] transposed = new double[sorted[0]][sorted[1]][sorted[2]][sorted[3]];

		FourDimensionMatrixTransposion mt = new FourDimensionMatrixTransposion(trans, transposed, seq,0, trans.length - 1);

		ForkJoinTask<Void> a = ForkJoinPoolManager.createPool().submit(mt);
		a.join();

		return transposed;
	}

	public static double[][][][] paddingArray(double[][][][] x, int padding) {
		double[][][][] y = new double[x.length][x[0].length][x[0][0].length + padding * 2][x[0][0][0].length
				+ padding * 2];
		PaddingArray pa = new PaddingArray(x, y, padding, 0, 0, y.length - 1);

		ForkJoinTask<Void> a = ForkJoinPoolManager.createPool().submit(pa);
		a.join();

		return y;
	}

	public static double[][] muliply(double[][] x, double[][] y) {
		// 输入数据和滤波器叠加运算
		ForkJoinTask<double[][]> matrixOp = ForkJoinPoolManager.createPool().submit(new MatrixMultiple(x, y, 0, x.length - 1));

		double[][] xw = matrixOp.join();
		
		return xw;
	}
	
	/**
	 * 把输入图像转换为二维行，与上面的不同，这里的有步长
	 * @param im
	 * @param fh
	 * @param fw
	 * @param oh
	 * @param ow
	 * @param stride
	 * @return
	 */
	public static double[][] im2col(double[][][][] im,int fh,int fw,int oh, int ow,int stride){
		
		double[][] flatX = new double[im.length * oh * ow][fh * fw * im[0].length];
		
		Im2col im2col = new Im2col(im, fh, fw, oh, ow, stride, 0, flatX, 0, im.length - 1);
		ForkJoinTask<Void> a = ForkJoinPoolManager.createPool().submit(im2col);
		a.join();
		
		return flatX;
	}
	
	public static double[][][][] col2im(double[][] col,int n,int c,int h ,int w,int fh,int fw,int oh,int ow,int stride,int pad){
		double[][][][] result = new double[n][c][w + 2*pad + stride - 1][w + 2*pad + stride - 1];
		
		Col2Im col2Im = new Col2Im(result,fh,fw,oh,ow,stride,col,0,result.length - 1);
		
		ForkJoinTask<Void> a = ForkJoinPoolManager.createPool().submit(col2Im);
		a.join();
		
		return result;
	}
	
	public static void normalize(BatchNorm bn,double[][] x){
		assert x != null;
		assert bn != null;

		
		Normalization nor = new Normalization(bn,x,0,x[0].length - 1);
		ForkJoinTask<Void> a = ForkJoinPoolManager.createPool().submit(nor);
		a.join();
	}
	
	public static void maxPooling(double[][][][] x,double[][][][]  result, int[][] mask, int height, int width, int stride){
		
		PoolingOperation po = new PoolingOperation(x, result, mask, height, width, stride, 0, x.length - 1);

		ForkJoinTask<Void> a = ForkJoinPoolManager.createPool().submit(po);
		a.join();		
		
	}

}
