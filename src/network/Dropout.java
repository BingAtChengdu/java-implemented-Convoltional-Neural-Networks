package network;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

import math.ForkJoinPoolManager;
import math.MatrixHadamardProduct;
import math.MatrixMultipleOne;
import math.MatrixRandomInit;

/**
 * 消除过拟合
 * @author hubing
 *
 */
public class Dropout implements Transformer {

	private double[][] mask;
	private Network network;
	private double ratio = 0.5;
	private transient ForkJoinPool fjp ;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6201066949958783851L;


	public Dropout(Network network,double ratio) {
		// TODO Auto-generated constructor stub
		this.network = network;
		if(ratio != 0.0d) {
			this.ratio = ratio;
		}
	}

	@Override
	public double[][] forward(double[][] x) {
		// TODO Auto-generated method stub
		
		if(fjp == null) {
			fjp = ForkJoinPoolManager.createPool();
		}
		
		if(network.isTraining()) {
			
			if(this.mask == null || mask.length != x.length || mask[0].length != x[0].length) {
				mask = new double[x.length][x[0].length];
			}

			MatrixRandomInit mri = new MatrixRandomInit(mask,ratio,0,mask.length - 1);
			ForkJoinTask<Void> f = fjp.submit(mri);
			f.join();
			
			MatrixHadamardProduct m = new MatrixHadamardProduct(x,mask,0,x.length - 1);
			ForkJoinTask<double[][]>  fjt = fjp.submit(m);
			double[][] result = fjt.join();
			
			return result;
			
		} else {
			double[][] result = new double[x.length][x[0].length];
			
			MatrixMultipleOne m = new MatrixMultipleOne(x,result,1 - ratio,0,x.length - 1);
			
			ForkJoinTask<Void> a = fjp.submit(m);
			a.join();
			return result;
		}	
	}

	/**
	 * 后向传播
	 */
	@Override
	public double[][] backward(double[][] dout) {
		// TODO Auto-generated method stub		
		MatrixHadamardProduct m = new MatrixHadamardProduct(dout,mask,0,dout.length - 1);
		ForkJoinTask<double[][]>  fjt = fjp.submit(m);
		double[][] result = fjt.join();		
		return result;
	}

}
