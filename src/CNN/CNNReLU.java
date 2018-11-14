package CNN;

import network.ReLU;

public class CNNReLU extends ReLU implements Filter {

	private static final long serialVersionUID = -1777835782819406932L;
	private double[][][][] rx; //用于反向求导

	@Override
	public double[][][][] forward(double[][][][] x) {
				
		rx =  x;
		
		double[][][][] activited = new double[x.length][x[0].length][x[0][0].length][x[0][0][0].length];
		
		for(int n = 0; n < x.length ; n ++)
			for(int c = 0; c < x[0].length ; c ++) {
				activited[n][c] = forward(x[n][c]);
			}
		
		return activited;
	}

	@Override
	public double[][][][] backward(double[][][][] dout) {
	
		double[][][][] activited = new double[dout.length][dout[0].length][dout[0][0].length][dout[0][0][0].length];
		
		for(int n = 0; n < dout.length ; n ++)
			for(int c = 0; c < dout[0].length ; c ++) {
				setRx(rx[n][c]);
				activited[n][c] = backward(dout[n][c]);
			}			
		return activited;
	}

}
