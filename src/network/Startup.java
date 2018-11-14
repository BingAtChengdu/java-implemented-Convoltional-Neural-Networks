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
 * 启动类 所有设置都在 startup.properties
 * 
 * @author hubing
 *
 */
public class Startup {

	private CNNSetting cnnSettings;
	private DenseSetting d;

	private int iteNum, trainSize, batchSize, testSize;
	private double lr; // 学习率

	double[][][][] x_train;
	double[][] t_train;

	double[][][][] x_test;
	double[][] t_test;

	private String trainImgPath, trainLabelPath, testImgPath, testLabelPath, trainSavePath;
	
	private Network network;
	private static Scanner sc;

	/**
	 * 启动网络
	 * 
	 * @param args[0] = train 训练 args[0] = run 识别 args[1] 网络名称 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		if (args == null) {
			throw new RuntimeException("参数不能为空！！");
		}

		String mode = args[0];
		String networkName = args[1];

		Startup startup = new Startup();
		String properties = "/startup.properties"; // 训练参数文件
		startup.parseParam(properties);
		startup.parseDataset();
		
		if ("train".equals(mode)) {// 训练模式
			startup.network = NetworkFactory.sequentialNetwork(networkName, startup.cnnSettings, startup.d);

			Trainer trainer = new Trainer(startup.network, startup.batchSize, startup.iteNum, startup.lr);
			trainer.train(startup.x_train, startup.t_train, startup.x_test, startup.t_test);
			trainer.save(startup.trainSavePath);
			
		} else if ("run".equals(mode)) {
						startup.network = startup.loadNetwork(networkName);
			
			sc = new Scanner(System.in);
	        
			System.out.println("请输入想识别的图片编号：");
			double[][][][] x = new double[1][startup.x_test[0].length][startup.x_test[0][0].length][startup.x_test[0][0][0].length];
			double[][] t = new double[1][startup.t_test[0].length];
			
	        while (sc.hasNext()) {
	            //利用nextXXX()方法输出内容
	            String str = sc.next();
	            int num = Integer.parseInt(str);
	            
	            startup.getOne(x,t,num);
	            
	            startup.network.setParam(x, t);
	            
	            double accuracy = startup.network.accuracy();
	            
	            System.out.println("识别精度： " + String.valueOf(accuracy));
	            
	        }
			startup.network.predict();
			
		} else {
			throw new RuntimeException("参数错误！");
		}

	}

	/**
	 * 加载训练参数
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

		MnistDataReader trainMatrix = new MnistDataReader(trainImgPath, trainLabelPath);
		MnistDataReader testMatrix = new MnistDataReader(testImgPath, testLabelPath);

		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);

		x_train = new double[trainSize][1][28][28];
		t_train = new double[trainSize][10];

		x_test = new double[testSize][1][28][28];
		t_test = new double[testSize][10];

		trainMatrix.randomSelect(x_train, t_train);
		testMatrix.randomSelect(x_test, t_test);
	}
	
	/**
	 * 加载网络
	 * @param name
	 * @return
	 */
	private Network loadNetwork(String name)  {
		
		ObjectInputStream ois;
		Network network;
		try {
			ois = new ObjectInputStream(new FileInputStream(trainSavePath + name));
			network = (Network) ois.readObject();
			ois.close();			
			return network;
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("卷积神经网络加载错误");
		}	

	}
	
	private void getOne(double[][][][] x, double[][] t,int num) {
		for(int c = 0; c < x_test[0].length; c ++)
			for(int i = 0; i < x_test[0][0].length; i ++) {
				System.arraycopy(x_test[num][c][i], 0, x[0][c][i], 0, x[0][0][0].length);
			}
		t[0] = t_test[num];
	}

}
