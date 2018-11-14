package math;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * °ÑËÄÎ¬¾ØÕó»»Îª¶þÎ¬¾ØÕó
 * 
 * @author hubing
 *
 */
public class Im2col extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double[][][][] x;

	private double[][] colMatrix;

	private int fh, fw, start, end, stride, pad,oh,ow;

	public Im2col(double[][][][] x, int fh, int fw, int oh ,int ow,int stride, int pad, double[][] col, int start, int end) {
		// TODO Auto-generated constructor stub
		this.x = x;

		this.colMatrix = col;

		this.start = start;
		this.end = end;
		this.fh = fh;
		this.fw = fw;
		this.stride = stride;
		this.pad = pad;
		
		this.oh = oh;
		this.ow = ow;
	}

	@Override
	protected void compute() {
		// TODO Auto-generated method stub
		int length = end - start + 1;

		if (length < 4 || length <= x.length / 4) {

			toCol();

		} else {

			int mid = (start + end + 1) >>> 1;
			Im2col left = new Im2col(x, fh, fw,oh,ow,stride, pad, colMatrix, start, mid - 1);
			Im2col right = new Im2col(x, fh, fw,oh,ow, stride, pad, colMatrix, mid, end);

			ForkJoinTask<Void> leftTask = left.fork();
			ForkJoinTask<Void> rightTask = right.fork();

			leftTask.join();
			rightTask.join();
		}
	}

	private void toCol() {
		
		for (int n = start; n <= end; n++) {
			
			int posX = 0;
			int posY = 0;
			
			for (int i = 0; i < oh; i++) {				
				for (int j = 0; j < ow; j++) {
					int pos = 0;					
					double[] col = colMatrix[n * oh * ow + i * oh + j];
					
					for (int c = 0; c < x[n].length; c++) {
						toCol(x[n][c], col, pos, posX, posY);
						pos += fh * fw;
					}
					posY = j + stride;
				}
				posX = i + stride;
				posY = 0;
			}
		}
	}

	private void toCol(double[][] s, double[] t, int pos, int i, int j) {
		for (int k = i; k < i + fh; k++) {
			System.arraycopy(s[k], j, t, pos, fw);
			pos += fw;
		}
	}
}
