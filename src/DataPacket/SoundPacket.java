package DataPacket;

import java.io.Serializable;

public class SoundPacket extends DataPacket implements Serializable, Cloneable {
	public static String tag = "SoundPacket";
	public static int defaultDataLenght = 1200;
	private byte[] data;
	
	public SoundPacket(byte[] data) {
		super(tag);
		// TODO Auto-generated constructor stub
		this.data = data;
	}
	
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
