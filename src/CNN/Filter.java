package CNN;

/**
 * ¾í»ý²ã½Ó¿Ú
 * @author hubing
 *
 */
public interface Filter {

	public double[][][][] forward(double[][][][] x);

	public double[][][][] backward(double[][][][] dout);
}
