package DataPacket;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseDeckShuffled extends DataPacket implements Serializable {
	public static String tag = "ResponseDeckShuffled";
	// có giá trị isYourTurn trong gói tin chia bài là để kiêm luôn việc xếp lượt đánh cho các client
	public boolean isYourTurn;
	public boolean isFirstBeginRound;
	
	public ArrayList<String> nameOfAnotherPlayer;
	public ArrayList<Integer> deckShuffled;
	
	public ResponseDeckShuffled(ArrayList<Integer> deckShuffled, ArrayList<String> nameOfAnotherPlayer,boolean isYourTurn, boolean isFirstBeginRound) {
		super(tag);
		this.deckShuffled = deckShuffled;
		this.nameOfAnotherPlayer = nameOfAnotherPlayer;
		this.isYourTurn = isYourTurn;
		this.isFirstBeginRound = isFirstBeginRound;
	}
	
	public ArrayList<Integer> getDeckShuffled() {
		return deckShuffled;
	}
	
	public void setDeckShuffled(ArrayList<Integer> deckShuffled) {
		this.deckShuffled = deckShuffled;
	}

	public boolean getIsYourTurn() {
		return isYourTurn;
	}

	public void setIsYourTurn(boolean isYourTurn) {
		this.isYourTurn = isYourTurn;
	}

	public boolean getIsFirstBeginRound() {
		return isFirstBeginRound;
	}

	public void setIsFirstBeginRound(boolean isFirstBeginRound) {
		this.isFirstBeginRound = isFirstBeginRound;
	}
	
	public ArrayList<String> getNameOfAnotherPlayer() {
		return nameOfAnotherPlayer;
	}
	
	public void setNameOfAnotherPlayer(ArrayList<String> nameOfAnotherPlayer) {
		this.nameOfAnotherPlayer = nameOfAnotherPlayer;
	}
	
}
