package trainer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import network.Network;

/**
 * ����ѵ������
 * @author hubing
 *
 */
public class Trainer {

	private ArrayList<Double> trainLossList, trainAccList, testAccList;
	private int trainSize, batchSize; // ÿ�����Ĵ�С
	private int iteNum; // ��������
	private Network network;

	private double[][][][] x_train;
	private double[][] t_train;

	public Trainer(Network network, int batchSize, int iteNum, double lr) {
		this.batchSize = batchSize;
		this.iteNum = iteNum;
		this.network = network;
		
		Network.lr = lr;
		
		trainLossList = new ArrayList<Double>();
		trainAccList = new ArrayList<Double>();
		testAccList = new ArrayList<Double>();
	}

	public void train(double[][][][] x_train, double[][] t_train, double[][][][] x_test, double[][] t_test) {

		trainSize = x_train.length;
		this.x_train = x_train;
		this.t_train = t_train;

		int epoch = Math.max(x_train.length / batchSize, 1);
		double[][][][] x_batch = new double[batchSize][x_train[0].length][x_train[0][0].length][x_train[0][0][0].length];
		double[][] t_batch = new double[batchSize][t_train[0].length];

		network.training();
		
		for (int i = 0; i < iteNum; i++) {
			getBatch(x_batch, t_batch);
			network.setParam(x_batch, t_batch);

			double loss = network.loss();
			
			System.out.println("i: " + i + " loss: " + String.valueOf(loss));
			
			trainLossList.add(loss);
			network.gradient();
			if (i % epoch == 0) {
				// ����ѵ�����ݾ���
				double trainAcc = network.accuracy();
				trainAccList.add(trainAcc);

				// ����������ݾ���
				network.setParam(x_test, t_test);
				network.predict();
				double testAcc = network.accuracy();
				testAccList.add(testAcc);
			}
		}
		printOut();
	}

	public void save(String path) throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path+network.getName()));
		oos.writeObject(network);
		oos.close();
		
	}
	private void getBatch(double[][][][] x_batch, double[][] t_batch) {
		Random random = new Random();
		for (int i = 0; i < batchSize; i++) {
			int idx = random.nextInt(trainSize);
			x_batch[i] = x_train[idx];
			t_batch[i] = t_train[idx];
		}
	}

	private void printOut() {
		NumberFormat nf = NumberFormat.getPercentInstance();
		
		// loss�仯
		System.out.println("-----------��ʧ�仯���-------------");
		Iterator<Double> ite = trainLossList.iterator();
		while (ite.hasNext()) {
			System.out.println(String.valueOf(ite.next()));
		}

		System.out.println("-----------ѵ�����ݾ��ȱ仯-------------");
		ite = trainAccList.iterator();
		while (ite.hasNext()) {
			System.out.println(nf.format(ite.next()));
		}

		System.out.println("-----------�������ݾ��ȱ仯-------------");
		ite = testAccList.iterator();
		while (ite.hasNext()) {
			System.out.println(nf.format(ite.next()));
		}
	}

}
