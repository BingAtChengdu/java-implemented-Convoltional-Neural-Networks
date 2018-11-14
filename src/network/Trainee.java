package network;

/**
 * 权重变化层接口
 * @author hubing
 *
 */
public interface Trainee{
	public  void numricGradient();
	public  void update();
	public void load(String path);
	public void save(String path);
}
