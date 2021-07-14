package DataPacket;

import java.io.Serializable;

public class ResponseSendMessenger extends DataPacket implements Serializable {
	
	// gói này để server gửi về client những tin nhắn từ một client nào đó gửi đến
	public static String tag = "ResponseSendMessenger";
	public String accountName;
	public String contentMessenger;
	
	public ResponseSendMessenger(String accountName, String contentMessenger) {
		super(tag);
		this.accountName = accountName;
		this.contentMessenger = contentMessenger;
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
