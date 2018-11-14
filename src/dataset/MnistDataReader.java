package dataset;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MnistDataReader  {
	
	private MnistMatrix[] m;
	
	private String dataFilePath, labelFilePath;
	
	public MnistDataReader(String dataFilePath, String labelFilePath) {
		this.dataFilePath = dataFilePath;
		this.labelFilePath = labelFilePath;
	}

    public void readData() throws IOException {

        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(dataFilePath)));
        int magicNumber = dataInputStream.readInt();
        int numberOfItems = dataInputStream.readInt();
        int nRows = dataInputStream.readInt();
        int nCols = dataInputStream.readInt();

        System.out.println("magic number is " + magicNumber);
        System.out.println("number of items is " + numberOfItems);
        System.out.println("number of rows is: " + nRows);
        System.out.println("number of cols is: " + nCols);

        DataInputStream labelInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(labelFilePath)));
        int labelMagicNumber = labelInputStream.readInt();
        int numberOfLabels = labelInputStream.readInt();

        System.out.println("labels magic number is: " + labelMagicNumber);
        System.out.println("number of labels is: " + numberOfLabels);
        
        assert numberOfItems == numberOfLabels;

        //numberOfItems = 10; //计算机性能不行，少加载点图片
        MnistMatrix[] data = new MnistMatrix[numberOfItems];
 

        for(int i = 0; i < numberOfItems; i++) {
            MnistMatrix mnistMatrix = new MnistMatrix(nRows, nCols);
            mnistMatrix.setLabel(labelInputStream.readUnsignedByte());
            for (int r = 0; r < nRows; r++) {
                for (int c = 0; c < nCols; c++) {
                    mnistMatrix.setValue(r, c, dataInputStream.readUnsignedByte() / 255.0d);
                }
            }
            data[i] = mnistMatrix;
        }
        dataInputStream.close();
        labelInputStream.close();
        
        this.m = data;
    }
    
    public void randomSelect(double[][] x_batch,double[][] t_batch) throws IOException {
    	if(this.m == null) {
    		this.readData();
    	}
    	
    	ArrayList<Integer> al = new ArrayList<Integer>();

		Random r = new Random();

		for (int i = 0; i < x_batch.length; i++) {
			
			int num = (int) Math.abs((r.nextInt(60000)));

			while (al.contains(num) || num > 60000 || num < 0) {
				num = r.nextInt();
			}
			
			double[] x = m[i].getXArray();
			
			System.arraycopy(x, 0, x_batch[i], 0, 784);
			double[] t = m[i].getT();
			System.arraycopy(t, 0, t_batch[i], 0, 10);
			
		}
    }
    
    /**
     * 随机选择出带通道的立体数据
     * @param x_batch
     * @param t_batch
     * @throws IOException
     */
    
    public void randomSelect(double[][][][] x_batch, double[][] t_batch) throws IOException {
    	    	
    	if(this.m == null) {
    		this.readData();
    	}
    	
    	ArrayList<Integer> al = new ArrayList<Integer>();

    	Random r = new Random();
    	
    	for(int n = 0; n < x_batch.length; n ++)
    		for(int c = 0; c < x_batch[0].length; c ++) {
    			int num = (int) Math.abs((r.nextInt(60000)));

    			while (al.contains(num) || num > 60000 || num < 0) {
    				num = r.nextInt();
    			}
    			   			
    			x_batch[n][c] = m[num].getData();
    			t_batch[n] = m[num].getT();
    		}
    }
    
    public void getData(double[][][][] x,double[][] t) throws IOException {
    	
       	if(this.m == null) {
    		this.readData();
    	}       	
    	int n = x.length;
    	for(int i = 0; i < n ; i ++) {
    		x[i][0] = m[i].getData();
    		t[i] = m[i].getT();
    	}    	
    }
}
