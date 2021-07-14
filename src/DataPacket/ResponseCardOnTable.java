package DataPacket;

import java.io.Serializable;
import java.util.ArrayList;

import ModelCard.Card;

public class ResponseCardOnTable extends DataPacket implements Serializable{
	
	public static String tag = "ResponseCardOnTable";
	
	// tên của client đã đánh ra những bài này
	public String accountName;
	
	public ArrayList<Integer> deckImport;
	
	public ResponseCardOnTable(ArrayList<Integer> deckImport, String accountName) {
		super(tag);
		this.deckImport = deckImport;
		this.accountName = accountName;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public ArrayList<Integer> getDeckImport() {
		return deckImport;
	}
	
	public void setDeckImport(ArrayList<Integer> deckImport) {
		this.deckImport = deckImport;
	}
	
}
