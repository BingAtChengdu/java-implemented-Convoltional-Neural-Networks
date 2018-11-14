package network;

import math.Operate;

/**
 * Êä³ö¼¤»î
 * @author hubing
 *
 */
public class SoftmaxWithLoss implements Transformer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 981609232771160893L;
	private Network network;
	
	public SoftmaxWithLoss(Network network) {
		// TODO Auto-generated constructor stub
		this.network = network;
	}

	@Override
	public double[][] forward(double[][] x) {
		assert x != null;
		// TODO Auto-generated method stub
		double[][] y =  Operate.softmax(x);
		double[][] t = network.getT();
		
		double[][] r = new double[1][1];		
		r[0][0] = Operate.crossEntropyError_onehot(y, t);
		return r;
	}

	@Override
	public double[][] backward(double[][] y) {
		// TODO Auto-generated method stub
		double[][] r = new double[y.length][y[0].length];
		double[][] t = network.getT();

		for (int i = 0; i < y.length; i++)
			for (int j = 0; j < y[0].length; j++) {
				r[i][j] = (y[i][j] - t[i][j]) / y.length;
			}
		return r;
	}

}
