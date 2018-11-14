package network;

import math.BackwardOp;
import math.Operate;

/**
 * ¼¤»î
 * @author hubing
 *
 */
public class Sigmoid implements Transformer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3221885334489943821L;
	private double[][] sig;
	
	public Sigmoid() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public double[][] forward(double[][] x) {
		// TODO Auto-generated method stub
		sig = Operate.sigmoid(x);		
		return sig;
	}

	@Override
	public double[][] backward(double[][] dout) {
		// TODO Auto-generated method stub
		return BackwardOp.sigmoid(sig, dout);
	}

}
