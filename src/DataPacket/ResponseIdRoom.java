package DataPacket;

public class ResponseIdRoom extends DataPacket {
	public static String tag = "ResponseIdRoom";
	public int idRoom;
	
	public ResponseIdRoom(int idRoom) {
		super(tag);
		// TODO Auto-generated constructor stub
		this.idRoom = idRoom;
	}

	public int getIdRoom() {
		return idRoom;
	}

	public void setIdRoom(int idRoom) {
		this.idRoom = idRoom;
	}
	
}
