package network;

import math.BackwardOp;
import math.MatrixTransformer;
import math.Operate;

/**
 * ǿ�ư�������������������
 * @author hubing
 *
 */
public class BatchNorm implements Trainee,Transformer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4530783127758992159L;

	// ��̬������������
	private transient double[][] normalized;

	

	private double[] gamma; // ��
	private double[] beta; // ��

	private transient double[] dgama; // �õĵ���
	private transient double[] dbeta; // �µĵ���

	private  double[] mu; // ��ֵ
	private  double[] sigma; // ����
	private  double[] stardardDev; // ��׼��
	private  double[][] xmu; // ÿ��Ԫ�ص����

	private Network network;

	public void injectNetwork(Network n) {
		network = n;
	}

	@Override
	public double[][] forward(double[][] x) {

		// TODO Auto-generated method stub
		
		assert x != null;

		// ���ֵ
		
		if(x.length == 1) {
			return x;
		}

		double[] sum = BackwardOp.sumMatrix(x);

		mu = sum;

		for (int i = 0; i < sum.length; i++) {
			mu[i] = sum[i] / x.length;
		}

		if (xmu == null || xmu.length != x.length || xmu[0].length != x[0].length) {
			xmu = new double[x.length][x[0].length];
		}

		normalized = new double[x.length][x[0].length];
		
		MatrixTransformer.normalize(this, x);

		// ƽ�ƺ�����

		double[][] r = BackwardOp.matrixMultipy(gamma, normalized);

		r = Operate.addB(r, beta);
		
		return r;
	}

	@Override
	public double[][] backward(double[][] dout) {
		
		if(normalized == null) {
			return dout;
		}
		// ��µĵ���
		dbeta = BackwardOp.sumMatrix(dout);

		// ��õĵ���
		double[][] tmp = mmult(dout, this.normalized);
		dgama = BackwardOp.sumMatrix(tmp);

		// �������
		double[][] dxhat = mmult(dout, gamma);
		double[][] tmp1 = mmult(dxhat, xmu);
		double[] divar = BackwardOp.sumMatrix(tmp1);
		double[][] dxmu1 = this.modArray(dxhat, stardardDev);
		double[] tmp2 = modArray(-1, sigma);
		double[] dsqrtvar = mmult(tmp2, divar);
		double[] tmp3 = modArray(0.5d, stardardDev);
		double[] dvar = mmult(tmp3, dsqrtvar);
		double[][] one = ones(normalized.length, normalized[0].length, 1.0 / normalized.length);
		double[][] dsq = mmult(one, dvar);

		double[][] tmp4 = multi1(2, xmu);

		double[][] dxmu2 = mmult(dsq, tmp4);
		double[][] dx1 = addMatrix(dxmu1, dxmu2);
		double[] tmp5 = BackwardOp.sumMatrix(dx1);
		double[] dmu = multi2(-1, tmp5);

		double[][] dx2 = mmult(one, dmu);

		double[][] dx = addMatrix(dx1, dx2);

		update();

		return dx;
	}

	@Override
	public void update() {
		for (int i = 0; i < this.gamma.length; i++) {
			this.gamma[i] -= Network.lr * this.dgama[i];
			this.beta[i] -= Network.lr * this.dbeta[i];
		}
	}

	@Override
	public void numricGradient() {
		// ٤��
		double tmp = 0.0d;
		if (dgama == null) {
			dgama = new double[gamma.length];
		}
		for (int i = 0; i < gamma.length; i++) {
			tmp = gamma[i];
			gamma[i] += Network.h;
			double loss1 = network.loss();

			gamma[i] = tmp;
			gamma[i] -= Network.h;

			double loss2 = network.loss();

			double d = (loss1 - loss2) / (2 * Network.h);

			dgama[i] = d;

			gamma[i] = tmp;
		}

		if (dbeta == null) {
			dbeta = new double[beta.length];
		}

		// ��
		for (int i = 0; i < beta.length; i++) {
			tmp = beta[i];
			beta[i] += Network.h;
			double loss1 = network.loss();

			beta[i] = tmp;
			beta[i] -= Network.h;

			double loss2 = network.loss();

			double d = (loss1 - loss2) / (2 * Network.h);

			dbeta[i] = d;
			beta[i] = tmp;
		}
	}

	private double[][] mmult(double[][] x, double[][] y) {

		double[][] result = new double[x.length][x[0].length];

		if (x.length != y.length || x[0].length != y[0].length) {
			throw new RuntimeException("���б�����ȣ�");
		}

		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				result[i][j] = x[i][j] * y[i][j];
			}
		}

		return result;
	}

	private double[] mmult(double[] x, double[] y) {

		double[] result = new double[x.length];

		if (x.length != y.length) {
			throw new RuntimeException("���б�����ȣ�");
		}

		for (int j = 0; j < x.length; j++) {
			result[j] = x[j] * y[j];
		}

		return result;
	}

	private double[][] mmult(double[][] x, double[] y) {

		double[][] result = new double[x.length][x[0].length];

		if (x[0].length != y.length) {
			throw new RuntimeException("���б�����ȣ�");
		}
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				result[i][j] = x[i][j] * y[j];
			}
		}

		return result;
	}

	private double[][] multi1(int n, double[][] dy) {
		double[][] result = new double[dy.length][dy[0].length];

		for (int i = 0; i < dy.length; i++)
			for (int j = 0; j < dy[0].length; j++) {
				result[i][j] = dy[i][j] * n;
			}
		return result;
	}

	private double[] multi2(int n, double[] dy) {
		double[] result = new double[dy.length];

		for (int i = 0; i < dy.length; i++) {
			result[i] = dy[i] * n;
		}
		return result;
	}


	private double[] modArray(double n, double[] x) {

		double[] result = new double[x.length];

		for (int i = 0; i < x.length; i++) {
			result[i] = n / x[i];
		}
		return result;
	}

	public double[][] ones(int n, int d, double v) {
		double[][] result = new double[n][d];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				result[i][j] = v;
			}
		}
		return result;
	}

	private double[][] modArray(double[][] x, double[] y) {

		double[][] result = new double[x.length][x[0].length];

		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				result[i][j] = x[i][j] / y[j];
			}
		}
		return result;
	}


	private double[][] addMatrix(double[][] x, double[][] y) {
		if (x.length != y.length || x[0].length != y[0].length) {
			throw new RuntimeException("���б�����ȣ�");
		}

		double[][] result = new double[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				result[i][j] = x[i][j] + y[i][j];
			}
		}
		return result;
	}

	public double sumMatrix(double[][] x) {
		double[][] y = forward(x);
		double sum = 0.0d;
		for (int i = 0; i < y.length; i++)
			for (int j = 0; j < y[0].length; j++) {
				sum += y[i][j];
			}
		return sum;
	}

	public void initWeight(int d) {
		// TODO Auto-generated method stub{
		gamma = new double[d];
		for (int i = 0; i < gamma.length; i++) {
			gamma[i] = 1.0d;
		}

		beta = new double[d];

		sigma = new double[d];

		stardardDev = new double[d];
	}
	
	public void printWeight() {
		for (int i = 0; i < gamma.length; i++) {
			System.out.println(String.valueOf(gamma[i]));
			System.out.println(String.valueOf(beta[i]));
		}

	}
	
	
	public double[][] getNormalized() {
		return normalized;
	}

	public double[] getGamma() {
		return gamma;
	}

	public double[] getBeta() {
		return beta;
	}

	public double[] getDgama() {
		return dgama;
	}

	public double[] getDbeta() {
		return dbeta;
	}

	public double[] getMu() {
		return mu;
	}

	public double[] getSigma() {
		return sigma;
	}

	public double[] getStardardDev() {
		return stardardDev;
	}

	public double[][] getXmu() {
		return xmu;
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
