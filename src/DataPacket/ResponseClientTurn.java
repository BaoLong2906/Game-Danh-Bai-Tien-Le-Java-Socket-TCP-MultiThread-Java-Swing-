package DataPacket;

import java.io.Serializable;

public class ResponseClientTurn extends DataPacket implements Serializable {
	// để cho các client nào nhận được gói này thì sẽ set isYourTurn = 0
	public static String tag = "ResponseClientTurn";
	public boolean isYourTurn;
	
	public ResponseClientTurn(boolean isYourTurn) {
		super(tag);
		this.isYourTurn = isYourTurn;
	}

	public boolean getIsYourTurn() {
		return isYourTurn;
	}

	public void setIsYourTurn(boolean isYourTurn) {
		this.isYourTurn = isYourTurn;
	}

}
