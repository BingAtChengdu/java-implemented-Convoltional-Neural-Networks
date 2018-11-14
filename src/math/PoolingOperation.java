package math;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * ≥ÿªØ‘ÀÀ„
 * 
 * @author hubing
 *
 */
public class PoolingOperation extends RecursiveAction {

	private static final long serialVersionUID = 1L;

	private double[][][][] x, result;
	private int[][] mask; 

	private int start, end, stride, ph, pw;

	public PoolingOperation(double[][][][] x, double[][][][] result, int[][] mask,int ph, int pw, int stride, int start, int end) {
		this.x = x;

		this.result = result;

		this.start = start;
		this.end = end;
		this.stride = stride;

		this.ph = ph;
		this.pw = pw;
		
		this.mask = mask;

	}

	@Override
	protected void compute() {
		int length = end - start + 1;

		if (length < 4 || length <= x.length / 4) {

			fromCol();

		} else {

			int mid = (start + end + 1) >>> 1;
			PoolingOperation left = new PoolingOperation(x, result, mask, ph, pw, stride, start, mid - 1);
			PoolingOperation right = new PoolingOperation(x, result, mask, ph, pw, stride, mid, end);

			ForkJoinTask<Void> leftTask = left.fork();
			ForkJoinTask<Void> rightTask = right.fork();

			leftTask.join();
			rightTask.join();
		}
	}

	private void fromCol() {

		int C = x[0].length;

		int out_h = result[0][0].length;
		int out_w = result[0][0][0].length;

		for (int n = start; n <= end; n++) {
			
			for (int c = 0; c < C; c++) {
				
				int posX = 0;
				int posY = 0;
				
				int cur = n * C * out_h * out_w + c * out_h * out_w;
				
				for (int h = 0; h < out_h; h++) {						
					for (int w = 0; w < out_w; w++) {
							
						result[n][c][h][w]  = max(x[n][c],posX,posY,ph,pw,n,c,cur);						
						
						posY = posY + stride;
						
						cur ++;
					}
					posX = posX + stride;
					posY = 0;
				}
			}
		}
	}

	private double max(double[][] data, int posX,int posY,int h, int w,int n ,int c ,int cur) {
		
		double max = 0.0d;
		mask[cur][0] = n;
		mask[cur][1] = c;

		mask[cur][2] = posX;
		mask[cur][3] = posY;
		
		for (int i = posX; i < posX + h; i++) {
			
			for(int j = posY; j < posY + w; j ++) {
				if(data[i][j] > max) {
					max = data[i][j];
					mask[cur][2] = i;
					mask[cur][3] = j;
				}
			}
		}

		return max;
	}
}
