package DataPacket;

import java.io.Serializable;

public class DestroyFirstBeginRound extends DataPacket implements Serializable {
	public static String tag = "DestroyFirstBeginRound";
	
	
	public DestroyFirstBeginRound() {
		super(tag);	
	}

}
