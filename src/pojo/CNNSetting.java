package pojo;

/**
 * ¾í»ı²ãÉèÖÃ
 * @author hubing
 *
 */
public class CNNSetting {
	
	public int getFilter_num() {
		return filter_num;
	}

	public void setFilter_num(int filter_num) {
		this.filter_num = filter_num;
	}

	public int getFilter_size() {
		return filter_size;
	}

	public void setFilter_size(int filter_size) {
		this.filter_size = filter_size;
	}

	public int getStride() {
		return stride;
	}

	public void setStride(int stride) {
		this.stride = stride;
	}

	public int getPad() {
		return pad;
	}

	public void setPad(int pad) {
		this.pad = pad;
	}

	private int filter_num, filter_size,stride,pad,layers,channels;

	public int getLayers() {
		return layers;
	}

	public void setLayers(int layers) {
		this.layers = layers;
	}

	public int getChannels() {
		return channels;
	}

	public void setChannels(int channels) {
		this.channels = channels;
	}


}
