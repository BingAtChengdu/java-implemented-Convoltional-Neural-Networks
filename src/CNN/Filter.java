package CNN;

import java.io.Serializable;

/**
 * �����ӿ�
 * @author hubing
 *
 */
public interface Filter  extends Serializable{

	public double[][][][] forward(double[][][][] x);

	public double[][][][] backward(double[][][][] dout);
}
