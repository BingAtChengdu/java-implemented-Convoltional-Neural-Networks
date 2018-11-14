package network;

import java.util.Random;

import math.BackwardOp;
import math.MatrixTransformer;
import math.NumbericGradient;
import math.Operate;
import math.WeightUpdator;

/**
 * ִ�����Ծ�������
 * @author hubing
 *
 */
public class Affine implements Trainee,Transformer {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4316821676762286035L;
	private transient double[][] x, xw;
	private transient double[][] xt;

	private double[][] w;
	private transient double[][] wt;

	private double[] b;

	private transient double[][] gradW;
	private transient double[] gradB;

	private Network network;
	
	private int n,d;
	private String activ;

	
	public Affine() {
	}

	public Affine(int n, int d,String activ) {
		this.n = n;
		this.d = d;
		this.activ = activ;
	}

	public void injectNetwork(Network n) {
		this.network = n;
	}

	/**
	 * ǰ�����
	 * 
	 * @param x
	 * @return
	 */
	@Override
	public double[][] forward(double[][] x) {
		
		
		if(this.n == -1 && w == null) { //��ȫ���Ӳ�ǰ���Ǿ����ʱ���ڵ�һ����Ԫ������ʱ�ٳ�ʼ����
			
			double sqr = 0.0d;
			n = x[0].length;			
			if ("sigmoid".equals(activ)) {
				sqr = 1 / (Math.sqrt((n + d) / 2.0d));

			} else if ("relu".equals(activ)) {
				sqr = Math.sqrt(2.0d / n );
			}

			initWeight(n,d,sqr);
		}

		this.x = x;

		xw = MatrixTransformer.muliply(x, w);
	
		return Operate.addB(xw, b);
	}

	/**
	 * �������
	 * 
	 * @param dout
	 * @return
	 */
	@Override
	public double[][] backward(double[][] dout) {

		this.wt = MatrixTransformer.transpose(this.w);
		this.xt = MatrixTransformer.transpose(x);

		// XW+B�е� +B�ӷ��ڵ㵼��
		gradB = BackwardOp.sumMatrix(dout);

		double[][] a1 = MatrixTransformer.muliply(dout, wt);


		gradW = MatrixTransformer.muliply(xt, dout);

		update();		

		return a1;
	}
	

	/**
	 * ΢����
	 */

	@Override
	public void numricGradient() {
		if(this.gradW == null) {
			gradW = new double[w.length][w[0].length];
		}
		
		if(this.gradB == null) {
			gradB = new double[w[0].length];
		}
		
		NumbericGradient.numbricGradident(network,w,b,gradW,gradB);
	}


	@Override
	public void update() {
		WeightUpdator.update(w, b, gradW, gradB);
	}
	

	public void initWeight(int n, int d, double sqrt) {
		// TODO Auto-generated method stub
		Random r = new Random();

		w = new double[n][d];
		b = new double[d];

		gradW = new double[n][d];

		gradB = new double[d];

		for (int i = 0; i < w.length; i++)
			for (int j = 0; j < w[0].length; j++) {
				w[i][j] = sqrt * r.nextGaussian();
			}
	}

}
