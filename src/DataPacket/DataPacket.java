package DataPacket;

import java.io.Serializable;

public class DataPacket implements Serializable {
	String tag;
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public DataPacket(String tag) {
		// TODO Auto-generated constructor stub
		this.tag = tag;
	}
}
