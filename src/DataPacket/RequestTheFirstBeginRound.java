package DataPacket;

import java.io.Serializable;

public class RequestTheFirstBeginRound extends DataPacket implements Serializable {
	// gói tin này để yêu cầu server gán giá trị isFirstBeginRound cho client kế cận
	// tính từ client có tên là giá trị account name
	
	public static String tag = "RequestTheFirstBeginRound";
	public String accountName;
	
	// nếu giá trị này là true tức là một client kế trước client đang nắm quyền firstbeginround đã có thể đánh đè được
	public int goOverCurrentTheFirstBeginRound;
	
	public RequestTheFirstBeginRound(String accountName, int goOverCurrentTheFirstBeginRound) {
		super(tag);
		this.accountName = accountName;
		this.goOverCurrentTheFirstBeginRound = goOverCurrentTheFirstBeginRound;
	}
	
	public RequestTheFirstBeginRound(String accountName) {
		super(tag);
		this.accountName = accountName;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public int getGoOverCurrentTheFirstBeginRound() {
		return goOverCurrentTheFirstBeginRound;
	}
	
	public void setGoOverCurrentTheFirstBeginRound(int goOverCurrentTheFirstBeginRound) {
		this.goOverCurrentTheFirstBeginRound = goOverCurrentTheFirstBeginRound;
	}
}
