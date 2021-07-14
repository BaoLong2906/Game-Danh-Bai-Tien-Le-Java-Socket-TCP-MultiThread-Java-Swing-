package DataPacket;

import java.io.Serializable;

public class ResponseWinner extends DataPacket implements Serializable {
	
	public static String tag = "ResponseWinner";
	public String winnerOne;
	public String winnerTwo;
	public String winnerThree;
	public String winnerFour;
	
	public ResponseWinner(String winnerOne, String winnerTwo, String winnerThree, String winnerFour) {
		super(tag);
		// TODO Auto-generated constructor stub
		this.winnerOne  = winnerOne;
		this.winnerTwo  = winnerTwo;
		this.winnerFour = winnerFour;
	}

	public String getWinnerOne() {
		return winnerOne;
	}

	public void setWinnerOne(String winnerOne) {
		this.winnerOne = winnerOne;
	}

	public String getWinnerTwo() {
		return winnerTwo;
	}

	public void setWinnerTwo(String winnerTwo) {
		this.winnerTwo = winnerTwo;
	}

	public String getWinnerThree() {
		return winnerThree;
	}

	public void setWinnerThree(String winnerThree) {
		this.winnerThree = winnerThree;
	}

	public String getWinnerFour() {
		return winnerFour;
	}

	public void setWinnerFour(String winnerFour) {
		this.winnerFour = winnerFour;
	}
	
}
