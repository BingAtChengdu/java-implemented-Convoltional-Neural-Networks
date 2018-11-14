package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.text.NumberFormat;

import org.junit.jupiter.api.Test;

import CNN.Util;
import dataset.MnistDataReader;
import network.Network;
import network.NetworkFactory;
import pojo.CNNSetting;
import pojo.DenseSetting;
import trainer.Trainer;

class testIntegration {

	@Test
	void test() throws IOException {
		MnistDataReader mnistMatrix = new MnistDataReader("D:/AI/mnist-data-reader-master/data/train-images.idx3-ubyte",
				"D:/AI/mnist-data-reader-master/data/train-labels.idx1-ubyte");

		int trainSize = 10000;

		int testSize = 50;
		
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);

		double[][][][] x_train = new double[trainSize][1][28][28];
		double[][] t_train = new double[trainSize][10];
		
		double[][][][] x_test = new double[testSize][1][28][28];
		double[][] t_test = new double[testSize][10];
		

		mnistMatrix.getData(x_train, t_train);
		


		mnistMatrix.randomSelect(x_test, t_test);

		CNNSetting cnnSettings = new CNNSetting();
		
		cnnSettings.setChannels(1);
		cnnSettings.setFilter_num(20);
		cnnSettings.setFilter_size(3);
		cnnSettings.setLayers(4);
		cnnSettings.setPad(0);
		cnnSettings.setStride(1);
		
		DenseSetting d = new DenseSetting();
		d.setInputSize(-1);
		d.setHiddenSize(100);
		d.setOutputSize(10);
		d.setActiv("relu");
		d.setLayers(2);
		d.setDropout(false);

		// ´´½¨ÍøÂç
		
		Network network = NetworkFactory.sequentialNetwork(cnnSettings, d);
		
		Trainer trainer = new Trainer(network,100,2000,0.1d);
		trainer.train(x_train, t_train, x_test, t_test);
		return;

	}
	
}
