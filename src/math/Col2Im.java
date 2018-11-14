package math;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * °ÑËÄÎ¬¾ØÕó»»Îª¶þÎ¬¾ØÕó
 * 
 * @author hubing
 *
 */
public class Col2Im extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double[][][][] x;

	private double[][] colMatrix;

	private int fh, fw, start, end, stride, oh,ow;

	public Col2Im(double[][][][] x, int fh, int fw, int oh ,int ow,int stride, double[][] col, int start, int end) {
		// TODO Auto-generated constructor stub
		this.x = x;

		this.colMatrix = col;

		this.start = start;
		this.end = end;
		this.fh = fh;
		this.fw = fw;
		this.stride = stride;
		
		this.oh = oh;
		this.ow = ow;
	}

	@Override
	protected void compute() {
		// TODO Auto-generated method stub
		int length = end - start + 1;

		if (length < 4 || length <= x.length / 4) {

			fromCol();

		} else {

			int mid = (start + end + 1) >>> 1;
			Col2Im left = new Col2Im(x, fh, fw,oh,ow,stride, colMatrix, start, mid - 1);
			Col2Im right = new Col2Im(x, fh, fw,oh,ow, stride, colMatrix, mid, end);

			ForkJoinTask<Void> leftTask = left.fork();
			ForkJoinTask<Void> rightTask = right.fork();

			leftTask.join();
			rightTask.join();
		}
	}

	private void fromCol() {
		
		for (int n = start; n <= end; n++) {
			
			int posX = 0;
			int posY = 0;
			
			for (int i = 0; i < oh; i++) {				
				for (int j = 0; j < ow; j++) {
					int pos = 0;					
					double[] col = colMatrix[n * oh * ow + i * oh + j];
					
					for (int c = 0; c < x[n].length; c++) {
						fromCol(x[n][c], col, pos, posX, posY);
						pos += fh * fw;
					}
					posY = j + stride;
				}
				posX = i + stride;
				posY = 0;
			}
		}
	}

	private void fromCol(double[][] s, double[] t, int pos, int i, int j) {
		for (int k = i; k < i + fh; k++) {
			for(int w = 0; w < fw; w ++) {
				s[k][j + w] += t[pos + w];
			}
			pos += fw;
		}
	}
}
