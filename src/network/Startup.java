package network;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.NumberFormat;
import java.util.Properties;
import java.util.Scanner;

import dataset.MnistDataReader;
import pojo.CNNSetting;
import pojo.DenseSetting;
import trainer.Trainer;

/**
 * ������ �������ö��� startup.properties
 * 
 * @author hubing
 *
 */
public class Startup {

	private CNNSetting cnnSettings;
	private DenseSetting d;

	private int iteNum, trainSize, batchSize, testSize;
	private double lr; // ѧϰ��

	double[][][][] x_train;
	double[][] t_train;

	double[][][][] x_test;
	double[][] t_test;

	private String trainImgPath, trainLabelPath, testImgPath, testLabelPath, trainSavePath;

	private Network network;
	private static Scanner sc;
	
	MnistDataReader trainMatrix,testMatrix;

	/**
	 * ��������
	 * 
	 * @param args[0] = train ѵ�� args[0] = run ʶ�� args[1] ��������
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		if (args == null) {
			throw new RuntimeException("��������Ϊ�գ���");
		}

		String mode = args[0];
		String networkName = args[1];

		Startup startup = new Startup();
		String properties = "/startup.properties"; // ѵ�������ļ�
		startup.parseParam(properties);
		startup.parseDataset();

		if ("train".equals(mode)) {// ѵ��ģʽ
			startup.network = NetworkFactory.sequentialNetwork(networkName, startup.cnnSettings, startup.d);

			Trainer trainer = new Trainer(startup.network, startup.batchSize, startup.iteNum, startup.lr);
			trainer.train(startup.x_train, startup.t_train, startup.x_test, startup.t_test);
			trainer.save(startup.trainSavePath);

		} else if ("run".equals(mode)) {
			startup.network = startup.loadNetwork(networkName);

			sc = new Scanner(System.in);

			NumberFormat nf = NumberFormat.getPercentInstance();
			
			System.out.println("��������ʶ���ͼƬ������");


			while (sc.hasNext()) {
				// ����nextXXX()�����������
				String str = sc.next();
				int num = Integer.parseInt(str);
				
				if(num <= 0) {
					System.out.println("��Ų���С�ڵ���0��");
				}

				double[][][][] x = new double[num][startup.x_test[0].length][startup.x_test[0][0].length][startup.x_test[0][0][0].length];
				double[][] t = new double[num][startup.t_test[0].length];
				
				startup.getOne(x, t);

				startup.network.setParam(x, t);
				
				double[][] y = startup.network.predict();

				double accuracy = startup.network.accuracy();
				System.out.println("ʶ�𾫶ȣ� " + nf.format(accuracy));
			}

		} else {
			throw new RuntimeException("��������");
		}

	}

	/**
	 * ����ѵ������
	 * 
	 * @param properties
	 * @throws IOException
	 */
	private void parseParam(String properties) throws IOException {

		Properties prop = new Properties();

		prop.load(Properties.class.getResourceAsStream(properties));

		cnnSettings = new CNNSetting();

		cnnSettings.setChannels(Integer.parseInt(prop.getProperty("channel")));
		cnnSettings.setFilter_num(Integer.parseInt(prop.getProperty("filterNumber")));
		cnnSettings.setFilter_size(Integer.parseInt(prop.getProperty("filterSize")));
		cnnSettings.setLayers(Integer.parseInt(prop.getProperty("cnnLayers")));
		cnnSettings.setPad(Integer.parseInt(prop.getProperty("pad")));
		cnnSettings.setStride(Integer.parseInt(prop.getProperty("stride")));

		d = new DenseSetting();
		d.setInputSize(Integer.parseInt(prop.getProperty("inputSize")));
		d.setHiddenSize(Integer.parseInt(prop.getProperty("hiddenSize")));
		d.setOutputSize(Integer.parseInt(prop.getProperty("outputSize")));

		d.setActiv(prop.getProperty("activation"));
		d.setLayers(Integer.parseInt(prop.getProperty("denseLayers")));
		d.setDropout(Boolean.parseBoolean(prop.getProperty("userDropout")));

		batchSize = Integer.parseInt(prop.getProperty("batchSize"));
		trainSize = Integer.parseInt(prop.getProperty("trainSize"));
		testSize = Integer.parseInt(prop.getProperty("testSize"));

		lr = Double.parseDouble(prop.getProperty("learningRate"));
		iteNum = Integer.parseInt(prop.getProperty("iteNum"));

		trainImgPath = prop.getProperty("trainImgPath");
		trainLabelPath = prop.getProperty("trainLabelPath");
		testImgPath = prop.getProperty("testImgPath");
		testLabelPath = prop.getProperty("testLabelPath");

		trainSavePath = prop.getProperty("trainSavePath");

	}

	private void parseDataset() throws IOException {

		trainMatrix = new MnistDataReader(trainImgPath, trainLabelPath);
		testMatrix = new MnistDataReader(testImgPath, testLabelPath);

		x_train = new double[trainSize][1][28][28];
		t_train = new double[trainSize][10];

		x_test = new double[testSize][1][28][28];
		t_test = new double[testSize][10];

		trainMatrix.randomSelect(x_train, t_train);
		testMatrix.randomSelect(x_test, t_test);
	}

	/**
	 * ��������
	 * 
	 * @param name
	 * @return
	 */
	private Network loadNetwork(String name) {

		ObjectInputStream ois;
		Network network;
		try {
			ois = new ObjectInputStream(new FileInputStream(trainSavePath + name));
			network = (Network) ois.readObject();
			ois.close();
			return network;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("�����������ش���");
		}

	}

	private void getOne(double[][][][] x ,double[][] t) throws IOException {		
		testMatrix.randomSelect(x, t);
	}

}
