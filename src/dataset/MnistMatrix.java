package dataset;

public class MnistMatrix {

    private double [][] data;

    private int nRows;
    private int nCols;

    private int label;

    public MnistMatrix(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;

        data = new double[nRows][nCols];
    }

    public double getValue(int r, int c) {
        return data[r][c];
    }

    public void setValue(int row, int col, double value) {
        data[row][col] = value;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getNumberOfRows() {
        return nRows;
    }

    public int getNumberOfColumns() {
        return nCols;
    }
    
    public int[] getArray() {
    	int[] a = new int[nRows * nCols];
    	
    	for(int i = 0; i < nRows; i++) {
    		System.arraycopy(data[i], 0, a, i * nCols, nRows);
    	}   	
    	
    	return a;
    }
    
    public double[] getXArray() {
    	double[] a = new double[nRows * nCols];
    	
    	for(int i = 0; i < nRows; i++) 
    		for(int j = 0; j < nCols; j++)    	
    	{
    		a[i * nRows +j ] = data[i][j];
    	}   	
    	
    	return a;
    }
    
    public double[] getT() {
    	double[] t = new double[10];
    	for(int i = 0; i < t.length ; i ++) {
    		if(i == this.label) {
    			t[i] = 1;
    		} else {
    			t[i] = 0;
    		}
    	}
    	return t;
    }
    public static void printMnistMatrix(final MnistMatrix matrix) {
        System.out.println("label: " + matrix.getLabel());
        for (int r = 0; r < matrix.getNumberOfRows(); r++ ) {
            for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
                System.out.print(matrix.getValue(r, c) + " ");
            }
            System.out.println();
        }
    }
    
    public double[][] getData(){
    	return this.data;
    }

}
