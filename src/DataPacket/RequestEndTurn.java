package DataPacket;

import java.io.Serializable;
import java.util.ArrayList;

public class RequestEndTurn extends DataPacket implements Serializable {
	public static String tag = "RequestEndTurn";
	public String clientWantEndTurn;
	
	// nếu đề nghị này đi kèm với một list các index của lá bài
	// thì đây là đè nghị yêu cầu server nhường lượt cho client tiếp theo
	// và gửi kết quả các lá bài được client này gửi đến tất cả các client còn lại trong ván game
	
	public ArrayList<Integer> dispatchDeck;
	
	public RequestEndTurn(String clientWantEndTurn, ArrayList<Integer> dispatchDeck) {
		super(tag);
		this.clientWantEndTurn = clientWantEndTurn;
		this.dispatchDeck = dispatchDeck;

	}
	
	public RequestEndTurn(String clientWantEndTurn) {
		super(tag);
		this.clientWantEndTurn = clientWantEndTurn;
	}
	
	
	public String getClientWantEndTurn() {
		return clientWantEndTurn;
	}
	
	public void setClientWantEndTurn(String clientWantEndTurn) {
		this.clientWantEndTurn = clientWantEndTurn;
	}
	
	public ArrayList<Integer> getDispatchDeck() {
		return dispatchDeck;
	}
	
	public void setDispatchDeck(ArrayList<Integer> dispatchDeck) {
		this.dispatchDeck = dispatchDeck;
	}
	
}
