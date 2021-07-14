package DataPacket;

import java.io.Serializable;

public class RequestSendMessenger extends DataPacket implements Serializable {

	public static String tag = "RequestSendMessenger";
	
	public String accountName;
	public String contentMessenger;
	
	public RequestSendMessenger(String accountName, String contenMessenger) {
		super(tag);
		this.accountName = accountName;
		this.contentMessenger = contenMessenger;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getContentMessenger() {
		return contentMessenger;
	}

	public void setContentMessenger(String contentMessenger) {
		this.contentMessenger = contentMessenger;
	}
	
	

}
