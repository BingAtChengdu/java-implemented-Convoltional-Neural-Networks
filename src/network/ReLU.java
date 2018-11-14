package network;

/**
 * ReLU激活
 * @author hubing
 *
 */
public class ReLU implements Transformer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7281678079095762938L;
	private transient double[][] rx ;
	


	public ReLU() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public double[][] forward(double[][] x) {
		// TODO Auto-generated method stub
		rx = x; //供反向传播时使用
		
		double[][] zx = new double[x.length][x[0].length];
		for(int i = 0; i < x.length ; i ++) {
			for(int j = 0; j < x[0].length; j++) {
				if(x[i][j] > 0) {
					zx[i][j] = x[i][j];
				} else {
					zx[i][j] = 0;
				}
			}
		}
		return zx;
	}

	@Override
	public double[][] backward(double[][] dout) {
		// TODO Auto-generated method stub
		double[][] dRelu = new double[dout.length][dout[0].length];
		for(int i = 0; i < dRelu.length ; i ++) {
			for(int j = 0; j < dRelu[0].length; j++) {
				if(rx[i][j] > 0) {
					dRelu[i][j] = dout[i][j];
				} else {
					dRelu[i][j] = 0;
				}
			}
		}
		
		return dRelu;
	}
	
	public void setRx(double[][] rx) {
		this.rx = rx;
	}

}
