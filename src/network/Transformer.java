package network;

import java.io.Serializable;

/**
 * �任���ܽӿ�
 * @author hubing
 *
 */
public interface Transformer extends Serializable{
	
	public abstract double[][] forward(double[][] x);
	public abstract double[][] backward(double[][] dout);
}
