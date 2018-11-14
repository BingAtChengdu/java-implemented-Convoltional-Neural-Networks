package pojo;

public class DenseSetting {

	private int inputSize, hiddenSize, outputSize, layers;
	private String activ;
	private boolean dropout;
	
	public int getInputSize() {
		return inputSize;
	}
	public void setInputSize(int inputSize) {
		this.inputSize = inputSize;
	}
	public int getHiddenSize() {
		return hiddenSize;
	}
	public void setHiddenSize(int hiddenSize) {
		this.hiddenSize = hiddenSize;
	}
	public int getOutputSize() {
		return outputSize;
	}
	public void setOutputSize(int outputSize) {
		this.outputSize = outputSize;
	}
	public int getLayers() {
		return layers;
	}
	public void setLayers(int layers) {
		this.layers = layers;
	}
	public String getActiv() {
		return activ;
	}
	public void setActiv(String activ) {
		this.activ = activ;
	}
	public boolean isDropout() {
		return dropout;
	}
	public void setDropout(boolean dropout) {
		this.dropout = dropout;
	}
	public DenseSetting() {
		// TODO Auto-generated constructor stub
	}

}
