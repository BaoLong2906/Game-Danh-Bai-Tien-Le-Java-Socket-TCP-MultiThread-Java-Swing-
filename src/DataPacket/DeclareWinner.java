package DataPacket;

import java.io.Serializable;

public class DeclareWinner extends DataPacket implements Serializable {

	public static String tag = "DeclareWinner";
	public String accountName;
	
	public DeclareWinner(String accountName) {
		super(tag);
		// TODO Auto-generated constructor stub
		this.accountName = accountName;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
}
