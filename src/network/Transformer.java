package network;

import java.io.Serializable;

/**
 * 变换层总接口
 * @author hubing
 *
 */
public interface Transformer extends Serializable{
	
	public abstract double[][] forward(double[][] x);
	public abstract double[][] backward(double[][] dout);
}
