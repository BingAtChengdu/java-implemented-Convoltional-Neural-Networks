package CNN;

/**
 * �����ӿ�
 * @author hubing
 *
 */
public interface Filter {

	public double[][][][] forward(double[][][][] x);

	public double[][][][] backward(double[][][][] dout);
}
