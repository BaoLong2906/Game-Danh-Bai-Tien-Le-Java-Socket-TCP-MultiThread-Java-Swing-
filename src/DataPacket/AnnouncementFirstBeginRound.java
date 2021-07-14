package DataPacket;

import java.io.Serializable;

public class AnnouncementFirstBeginRound extends DataPacket implements Serializable {
	public static String tag = "AnnouncementFirstBeginRound";
	public String accountName;
	public int jumpOneStep;
	
	public AnnouncementFirstBeginRound(String accountName) {
		super(tag);
		this.accountName = accountName;
	}
	
	public AnnouncementFirstBeginRound(String accountName, int jumpOneStep) {
		super(tag);
		this.accountName = accountName;
		this.jumpOneStep = jumpOneStep;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public int getJumpOneStep() {
		return jumpOneStep;
	}
	
	public void setJumpOneStep(int jumpOneStep) {
		this.jumpOneStep = jumpOneStep;
	}
	
}
