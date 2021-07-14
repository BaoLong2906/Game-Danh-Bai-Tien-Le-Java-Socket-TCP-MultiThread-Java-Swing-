package DataPacket;

import java.io.Serializable;

public class RegistrationAccountName extends DataPacket implements Serializable{
	// goi tin nay dung de ghi danh accountName;
	
	public static String tag = "registrationAccountName";
	public String accountName;

	public RegistrationAccountName(String accountName) {
		super(tag);
		this.accountName = accountName;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
}
