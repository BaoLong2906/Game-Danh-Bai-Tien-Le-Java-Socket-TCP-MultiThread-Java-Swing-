package DataPacket;

import java.io.Serializable;

public class ResponseTheFirstBeginRound extends DataPacket implements Serializable {
	// gói tin này định nghĩa ai là người bất đầu một round mới
	// là khi không ai đánh bài đè lên được thì client đó được quyền tiếp tục đánh combo mới
	
	public static String tag = "ResponseTheFirstBeginRound";
	public boolean isFirstBeginRound;
	
	public ResponseTheFirstBeginRound(boolean isFirstBeginRound) {
		super(tag);
		this.isFirstBeginRound = isFirstBeginRound;
	}

	public boolean getIsFirstBeginRound() {
		return isFirstBeginRound;
	}

	public void setIsFirstBeginRound(boolean isFirstBeginRound) {
		this.isFirstBeginRound = isFirstBeginRound;
	}

}
