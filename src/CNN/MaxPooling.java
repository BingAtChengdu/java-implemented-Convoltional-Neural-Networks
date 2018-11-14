package CNN;

import math.MatrixShape;
import math.MatrixTransformer;

public class MaxPooling implements Filter {

	private int height, width, pad, stride;

	private int N, C, H, W; // 输入数据形状，用于反向求导

	private int[][] mask;

	private void init(int height, int width, int pad, int stride) {
		this.height = height;
		this.width = width;
		this.pad = pad;
		this.stride = stride;
	}

	public MaxPooling(int height, int width, int pad, int stride) {
		init(height, width, pad, stride);
	}

	public MaxPooling(int height, int width) {
		init(height, width, 0, 2);
	}

	@Override
	public double[][][][] forward(double[][][][] x) {
		
		if (pad != 0) {
			// 填充数组
			double[][][][] y = MatrixTransformer.paddingArray(x, pad);

			x = y;
		}

		N = x.length;
		C = x[0].length;
		H = x[0][0].length;
		W = x[0][0][0].length;

		int out_h = 1 + (H - height) / stride;
		int out_w = 1 + (W - width) / stride;

		double[][][][] result = new double[N][C][out_h][out_w];

		mask = new int[N * C * out_h * out_w][4];

		MatrixTransformer.maxPooling(x, result, mask, height, width, stride);
		return result;
	}

	@Override
	public double[][][][] backward(double[][][][] dout) {	

		
		double[][][][] dxout = new double[N][C][H][W];
		
	
		double[] col = MatrixShape.reshape(dout);
		
		assert col.length == mask.length;

		// 反向传播把在池化中传给下一层的数据的导数。
		for (int i = 0; i < mask.length; i++) {

			dxout[mask[i][0]][mask[i][1]][mask[i][2]][mask[i][3]] = col[i] ;
		}
				

		return dxout;
	}	

}
