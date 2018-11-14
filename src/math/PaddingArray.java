package math;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * 填充数组
 * @author hubing
 *
 */
public class PaddingArray extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double[][][][] x,y;
	
	private double number;
	
	private int start, end,pad;

	public PaddingArray(double[][][][] x, double[][][][] y, int pad,double number,int start,int end) {
		// TODO Auto-generated constructor stub
		this.x = x;
		
		this.start = start;
		this.end = end;
		this.pad = pad;
		
		this.y = y;
		
		if(x == null || y == null) {
			throw new RuntimeException("数组不能为空");
		}
		
		this.number = number;
	}

	@Override
	protected void compute() {
		// TODO Auto-generated method stub
		int length = end - start + 1;

		if (length < 4 || length <= x.length / 4) {

			padding();

		} else {

			int mid = (start + end + 1) >>> 1;
			PaddingArray left = new PaddingArray(x,y,pad,number,start, mid - 1);
			PaddingArray right = new PaddingArray(x,y,pad,number,mid, end);

			ForkJoinTask<Void> leftTask = left.fork();
			ForkJoinTask<Void> rightTask = right.fork();

			leftTask.join();
			rightTask.join();
		}
	}

	private void padding() {
		
		int xi = x[0][0].length;
		int xj = x[0][0][0].length;
		
		for(int n = start; n <= end ; n ++) { //批数量
			for(int c = 0; c < y[n].length; c ++) //通道 
				for(int i = 0; i < y[n][c].length; i ++ ) //行
					for(int j = 0; j < y[n][c][i].length; j ++ ) //列
			{ //通道数量
				int ip = i - pad;
				int jp = j - pad;
				if(ip >=0 && ip < xi && jp >=0 && jp < xj) {
					y[n][c][i][j] = x[n][c][ip][jp];
				} else {
					y[n][c][i][j] = number;
				}
			}
		}		
	}

}
