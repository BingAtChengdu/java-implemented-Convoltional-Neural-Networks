package CNN;

import java.io.Serializable;

/**
 * ¾í»ý²ã½Ó¿Ú
 * @author hubing
 *
 */
public interface Filter  extends Serializable{

	public double[][][][] forward(double[][][][] x);

	public double[][][][] backward(double[][][][] dout);
}
