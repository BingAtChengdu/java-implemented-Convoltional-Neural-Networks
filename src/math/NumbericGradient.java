package math;

import network.Network;

/**
 *  ����΢����
 * 
 * @author hubing
 *
 */
public class NumbericGradient{


	public static void numbricGradident(Network network,double[][] w, double[] b, double[][] dw,double[] db) {
		
		for(int j = 0; j < w[0].length; j ++) {
			// Ȩ����
			for(int i = 0; i < w.length; i ++) {
				
				double temp = w[i][j]; // �ݴ浱ǰ����

				w[i][j] = w[i][j] + Network.h;

				double loss1 = network.loss();

				w[i][j] = temp; // �ָ�

				w[i][j] = w[i][j] - Network.h;

				double loss2 = network.loss();

				w[i][j] = temp; // �ָ�

				dw[i][j] = (loss1 - loss2) / (2 * Network.h);// ����ƫ�������γ��ݶȡ�
			}
			
			//ƫ����
			double temp = b[j]; // �ݴ浱ǰ����

			b[j] = b[j] + Network.h;

			double loss1 = network.loss();

			b[j] = temp; // �ָ�

			b[j] = b[j] - Network.h;

			double loss2 = network.loss();

			b[j] = temp; // �ָ�

			db[j] = (loss1 - loss2) / (2 * Network.h);// ����ƫ�������γ��ݶȡ�
			
		}
	}
}
