package CNN;

import java.util.Random;

import math.BackwardOp;
import math.MatrixShape;
import math.MatrixTransformer;
import math.NumbericGradient;
import math.Operate;
import math.WeightUpdator;
import network.Network;
import network.Trainee;

/**
 * �����
 * 
 * @author hubing
 *
 */
public class Convolution implements Filter, Trainee {

	private int stride = 1, padding = 0;
	private double[][][][] filter;
	private transient double[][][][] dfilter;
	private double[] b;
	private transient double[] db;
	private transient double std;

	private transient double[][] flatX, flatW, transposedW, dTransposedW, dw;

	private int h, w, fn, channel, height, width, oh, ow;

	private Network network;

	private void init(int fn, int channel, int height, int width, double std) {

		this.channel = channel;
		filter = this.initFilter(fn, channel, height, width, std);
	}

	public Convolution(int fn, int channel, int height, int width, double std) {

		this.fn = fn;
		this.channel = channel;
		this.height = height;
		this.width = width;
		this.std = std;

		b = new double[fn];

	}

	public Convolution(int fn, int channel, int height, int width, int stride, int padding, double std) {
		this.stride = stride;
		this.padding = padding;

		this.fn = fn;
		this.channel = channel;
		this.height = height;
		this.width = width;
		this.std = std;

		b = new double[fn];

	}

	public void injectNetwork(Network network) {
		this.network = network;
	}

	@Override
	public double[][][][] forward(double[][][][] x) {

		if (filter == null) {// ��̬��ʼ��
			std = x[0].length * x[0][0].length * x[0][0][0].length;
			std = Math.sqrt(2 / std);
			init(fn, x[0].length, height, width, std);
		}

		assert channel == x[0].length;

		h = x[0][0].length;
		w = x[0][0][0].length;

		oh = (h + 2 * padding - height) / stride + 1;
		ow = (w + 2 * padding - width) / stride + 1;

		if (padding != 0) {
			// �������
			double[][][][] y = MatrixTransformer.paddingArray(x, padding);

			x = y;
		}

		// �������ݶ�ά��
		flatX = MatrixTransformer.im2col(x, height, width, oh, ow, stride);

		// �˲�����ά��

		flatW = MatrixShape.reshape(filter, fn, -1);

		// ת���˲���
		transposedW = MatrixTransformer.transpose(flatW);

		double[][] xw = MatrixTransformer.muliply(flatX, transposedW);

		double[][] r = Operate.addB(xw, b);

		// �Ӷ�ά״̬�ָ���������״̬

		double[][][][] result = MatrixShape.reshape(r, x.length, oh, ow, -1);
		result = MatrixTransformer.transpose(result, new int[] { 0, 3, 1, 2 });

		return result;
	}

	@Override
	public double[][][][] backward(double[][][][] dout) {

		int n = dout.length;
		int fn = dout[0].length;
		int oh = dout[0][0].length;
		int ow = dout[0][0][0].length;

		// �����ά��
		dout = MatrixTransformer.transpose(dout, new int[] { 0, 2, 3, 1 });
		double[][] flatDout = MatrixShape.reshape(dout, -1, fn);

		// ƫִB�ĵ���
		db = BackwardOp.sumMatrix(flatDout);

		// ��W�ĵ��� x��ת�� ���� dout
		double[][] xt = MatrixTransformer.transpose(flatX);

		dw = MatrixTransformer.muliply(xt, flatDout);// �˲����ĵ�������״���˲�����ͬ

		double[][] dwt = MatrixTransformer.transpose(dw);

		// �Ӷ�ά״̬�ָ���������״

		dfilter = MatrixShape.reshape(dwt, fn, channel, height, width);

		// ��X�ĵ���
		double[][] dxout = MatrixTransformer.muliply(flatDout, flatW);
		double[][][][] doutx = MatrixTransformer.col2im(dxout, n, channel, h, w, height, width, oh, ow, stride,
				padding);

		update();
		return doutx;
	}

	private double[][][][] initFilter(int n, int c, int h, int w, double sqrt) {

		Random r = new Random();

		double[][][][] a = new double[n][c][h][w];

		for (int i = 0; i < n; i++)
			for (int j = 0; j < c; j++) {
				for (int k = 0; k < h; k++)
					for (int d = 0; d < w; d++) {
						a[i][j][k][d] = sqrt * r.nextGaussian();
					}
			}

		return a;
	}

	@Override
	public void numricGradient() {

		if (dTransposedW == null) {
			dTransposedW = new double[transposedW.length][transposedW[0].length];
		}

		if (db == null) {
			db = new double[b.length];
		}

		NumbericGradient.numbricGradident(network, transposedW, b, dTransposedW, db);
	}

	@Override
	public void update() {
		WeightUpdator.update(filter, b, dfilter, db);
	}

	@Override
	public void load(String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(String path) {
		// TODO Auto-generated method stub
		
	}
}
