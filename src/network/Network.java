package network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import CNN.Filter;
import math.MatrixShape;

public class Network implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2432159159499848196L;
	private  double[][][][] x;
	private  double[][] t;

	private  double[][] flatX, y;

	private List<Transformer> denseLayers;
	private List<Filter> cnnLayers;


	public final static double h = 1E-4;
	public  static double lr = 0.1d;

	private boolean isTraining = false;

	private int c, height, width;
	
	private String name ;

	public Network(String name) {
		cnnLayers = new ArrayList<Filter>();
		denseLayers = new ArrayList<Transformer>();
		this.name = name;
	}

	public void setParam(double[][][][] x, double[][] t) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.t = t;
	}

	public void setParam(double[][] x, double[][] t) {
		// TODO Auto-generated constructor stub
		this.flatX = x;
		this.t = t;
	}

	public double[][] predict() {

		// 卷积层运算
		Iterator<Filter> filterIte = cnnLayers.iterator();

		double[][][][] tx = x;

		while (filterIte.hasNext()) {
			tx = filterIte.next().forward(tx);
		}

		c = tx[0].length;
		height = tx[0][0].length;
		width = tx[0][0][0].length;

		flatX = MatrixShape.reshape(tx, x.length, -1); // 转换成二维数组

		// 全连接层运算
		double[][] z = flatX;
		
		for(int i = 0; i < denseLayers.size() - 1; i ++) {
			z = denseLayers.get(i).forward(z);
		}

		y = z; //保存y用于反向求梯度

		return y;
	}

	/**
	 * 計算交叉熵損失
	 * 
	 * @return
	 */
	public double loss() {
		double[][] y = this.predict();
		double[][] r = denseLayers.get(denseLayers.size() - 1).forward(y);
		return r[0][0];
	}

	public double accuracy() {
		
		int accuracy = 0;

		for (int i = 0; i < y.length; i++) {
			int max = 0;
			for (int j = 0; j < y[0].length; j++) {
				if (y[i][j] > y[i][max]) {
					max = j;
				}
			}

			if (t[i][max] == 1.0d) { // 如果猜中，則猜中數加一。
				accuracy++;
			}
		}
		double percent = ((double) accuracy) / y.length;
		return percent;
	}

	/**
	 * 反向传播求梯度
	 * 
	 * @return
	 */
	public double[][][][] gradient(){

		double[][] z = y;

		ListIterator<Transformer> ite = denseLayers.listIterator(denseLayers.size());

		Transformer tr;

		while (ite.hasPrevious()) {
			tr = ite.previous();
			z = tr.backward(z);
		}

		ListIterator<Filter> cnnite = cnnLayers.listIterator(cnnLayers.size());

		Filter filter;

		double[][][][] dout = MatrixShape.reshape(z, x.length, c, height, width);

		while (cnnite.hasPrevious()) {
			filter = cnnite.previous();
			dout = filter.backward(dout);
		}

		return dout;
	}

	/**
	 * 微分求梯度
	 */
	public void numricGradient() {

		Iterator<Filter> filterIte = cnnLayers.iterator();
		while (filterIte.hasNext()) {
			Filter filter = filterIte.next();
			if (filter instanceof Trainee) {
				((Trainee) filter).numricGradient();
			}
		}

		Iterator<Transformer> ite = this.denseLayers.iterator();

		while (ite.hasNext()) {
			Transformer trans = ite.next();
			if (trans instanceof Trainee) {
				((Trainee) trans).numricGradient();
			}
		}

	}

	public void addDenseLayer(Transformer trans) {
		this.denseLayers.add(trans);
	}

	public void addCNNLayer(Filter filter) {
		this.cnnLayers.add(filter);
	}

	public void training() {
		this.isTraining = true;
	}

	public void shutTraining() {
		this.isTraining = false;
	}

	public boolean isTraining() {
		return isTraining;
	}

	public double[][] getT() {
		return t;
	}
	
	public String getName() {
		return name;
	}
}
