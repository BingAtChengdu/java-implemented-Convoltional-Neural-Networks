package CNN;

public final class Util {

	private Util() {
		// TODO Auto-generated constructor stub
	}

	public static void printArray(double[][][][] x) {

		for (int n = 0; n < x.length; n++) {
			System.out.println("第 " + n + " 个数据");
			for (int c = 0; c < x[n].length; c++) {
				System.out.println("第 " + c + " 个通道");
				for (int i = 0; i < x[n][c].length; i++) {
					for (int j = 0; j < x[n][c][i].length; j++) {
						System.out.print(" " + String.valueOf(x[n][c][i][j]) + " ");
					}
					System.out.println("");
				}
			}
		}
	}

	public static void printArray(double[][] x) {

		for (int n = 0; n < x.length; n++) {
			for (int c = 0; c < x[n].length; c++) {
				System.out.print(" " + String.valueOf(x[n][c]) + " ");
			}
			System.out.println("");
		}
	}
	
	public static void printArray(int[][] x) {

		for (int n = 0; n < x.length; n++) {
			for (int c = 0; c < x[n].length; c++) {
				System.out.print(" " + String.valueOf(x[n][c]) + " ");
			}
			System.out.println("");
		}
	}

}
