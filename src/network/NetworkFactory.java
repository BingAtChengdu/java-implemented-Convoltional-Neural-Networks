package network;

import CNN.CNNReLU;
import CNN.Convolution;
import CNN.MaxPooling;
import pojo.CNNSetting;
import pojo.DenseSetting;

public class NetworkFactory {

	private NetworkFactory() {
	}

	public static Network sequentialNetwork(String name,CNNSetting cnnSetting, DenseSetting denseSetting) {

		double b = 0;

		Network network = new Network(name);

		/**
		 * 卷积层
		 */

		for (int i = 0; i < cnnSetting.getLayers(); i++) {
			Convolution conv = new Convolution(cnnSetting.getFilter_num(), cnnSetting.getChannels(),
					cnnSetting.getFilter_size(), cnnSetting.getFilter_size(), 0.05d);

			conv.injectNetwork(network);

			network.addCNNLayer(conv);

			network.addCNNLayer(new CNNReLU());

			if ((i + 1) % 2 == 0) {

				MaxPooling mp = new MaxPooling(2, 2);

				network.addCNNLayer(mp);
			}
		}

		/**
		 * 全连接层
		 */
		// 第一层
		Transformer activation = null;

		if ("sigmoid".equals(denseSetting.getActiv())) {

			activation = new Sigmoid();
			b = 1 / (Math.sqrt((denseSetting.getInputSize() + denseSetting.getHiddenSize()) / 2.0d));

		} else if ("relu".equals(denseSetting.getActiv())) {
			activation = new ReLU();
			b = Math.sqrt(2.0d / denseSetting.getInputSize());
		}
		Affine af1 = new Affine(denseSetting.getInputSize(), denseSetting.getHiddenSize(), denseSetting.getActiv());
		af1.injectNetwork(network);
		
		BatchNorm bn = new BatchNorm();
		bn.injectNetwork(network);
		bn.initWeight(denseSetting.getHiddenSize());
		

		network.addDenseLayer(af1);
		network.addDenseLayer(bn);
		network.addDenseLayer(activation);

		double ratio = 0.5d;

		if (denseSetting.isDropout()) {
			Dropout dp = new Dropout(network, ratio);
			network.addDenseLayer(dp);
		}

		// 第二层

		for (int i = 0; i < denseSetting.getLayers() - 2; i++) {

			if ("sigmoid".equals(denseSetting.getActiv())) {
				activation = new Sigmoid();
				b = 1.0d / Math.sqrt(denseSetting.getHiddenSize());
			} else if ("relu".equals(denseSetting.getActiv())) {
				activation = new ReLU();
				b = Math.sqrt(2.0d / denseSetting.getHiddenSize());
			}

			Affine af2 = new Affine();
			af2.injectNetwork(network);
			af2.initWeight(denseSetting.getHiddenSize(), denseSetting.getHiddenSize(), b);

			BatchNorm bn2 = new BatchNorm();
			bn2.injectNetwork(network);
			bn2.initWeight(denseSetting.getHiddenSize());

			network.addDenseLayer(af2);
			network.addDenseLayer(bn2);
			network.addDenseLayer(activation);

			if (denseSetting.isDropout()) {
				Dropout dp = new Dropout(network, ratio);
				network.addDenseLayer(dp);
			}
		}

		// 第四层

		if ("sigmoid".equals(denseSetting.getActiv())) {
			activation = new Sigmoid();
			b = 1.0d / Math.sqrt((denseSetting.getHiddenSize() + denseSetting.getOutputSize()) / 2.0d);
		} else if ("relu".equals(denseSetting.getActiv())) {
			activation = new ReLU();
			b = Math.sqrt(2.0d / denseSetting.getHiddenSize());
		}

		Affine af4 = new Affine();
		af4.injectNetwork(network);
		af4.initWeight(denseSetting.getHiddenSize(), denseSetting.getOutputSize(), b);
		Transformer softmax = new SoftmaxWithLoss(network);
		
		network.addDenseLayer(af4);
		network.addDenseLayer(softmax);

		return network;
	}
}
