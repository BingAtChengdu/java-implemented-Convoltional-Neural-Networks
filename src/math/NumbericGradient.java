package math;

import network.Network;

/**
 *  正向微分求导
 * 
 * @author hubing
 *
 */
public class NumbericGradient{


	public static void numbricGradident(Network network,double[][] w, double[] b, double[][] dw,double[] db) {
		
		for(int j = 0; j < w[0].length; j ++) {
			// 权重求导
			for(int i = 0; i < w.length; i ++) {
				
				double temp = w[i][j]; // 暂存当前变量

				w[i][j] = w[i][j] + Network.h;

				double loss1 = network.loss();

				w[i][j] = temp; // 恢复

				w[i][j] = w[i][j] - Network.h;

				double loss2 = network.loss();

				w[i][j] = temp; // 恢复

				dw[i][j] = (loss1 - loss2) / (2 * Network.h);// 计算偏导数，形成梯度。
			}
			
			//偏置求导
			double temp = b[j]; // 暂存当前变量

			b[j] = b[j] + Network.h;

			double loss1 = network.loss();

			b[j] = temp; // 恢复

			b[j] = b[j] - Network.h;

			double loss2 = network.loss();

			b[j] = temp; // 恢复

			db[j] = (loss1 - loss2) / (2 * Network.h);// 计算偏导数，形成梯度。
			
		}
	}
}
