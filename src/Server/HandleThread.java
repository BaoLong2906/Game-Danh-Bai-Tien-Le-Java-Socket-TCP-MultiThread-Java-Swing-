package Server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import DataPacket.AnnouncementFirstBeginRound;
import DataPacket.DataPacket;
import DataPacket.DeclareWinner;
import DataPacket.DestroyFirstBeginRound;
import DataPacket.RegistrationAccountName;
import DataPacket.RequestEndTurn;
import DataPacket.RequestSendFileData;
import DataPacket.RequestSendMessenger;
import DataPacket.RequestTheFirstBeginRound;
import DataPacket.ResponseCardOnTable;
import DataPacket.ResponseClientTurn;
import DataPacket.ResponseDeckShuffled;
import DataPacket.ResponseIdRoom;
import DataPacket.ResponseSendMessenger;
import DataPacket.ResponseTheFirstBeginRound;
import DataPacket.ResponseWinner;
import ModelCard.Card;
import ModelCard.CardFactory;

public class HandleThread extends Thread{
	
	public int idRoom;
	
	public ArrayList<HandleThread> handleThreadList;
	
	public String accountName;
	public Socket socket;
	
	public ObjectInputStream objectInputStream;
	public ObjectOutputStream objectOutputStream;
	
	public String whoIsFristBeginRound;
	public int intIndexOfWhoIsFirstBeginRound;
	
	public String numberOne;
	public String numberTwo;
	public String numberThree;
	public String numberFour;
	
	public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
		this.objectOutputStream = objectOutputStream;
	}
	
	public ObjectOutputStream getObjectOutputStream() {
		return objectOutputStream;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		// test thêm vòng lặp while, có thể sai, có thể ảnh hưởng đến doWrite()
		
		handleThreadList =  Server.roomList.get(idRoom);
		System.out.println("hiện tại phòng này đang có số người là: " + handleThreadList.size());
		
		// gửi số phòng về client.
		ResponseIdRoom responseIdRoom = new ResponseIdRoom(idRoom);
		DataPacket providerIdRoom = (DataPacket) responseIdRoom;
		doWrite(providerIdRoom);
		
		while (true) {
			doRead();
		}
	}
	
	public HandleThread(Socket socket, int idRoom) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.idRoom = idRoom;
		
		
		//handleThreadList =  Server.roomList.get(idRoom);
		
		
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void doRead() {
		try {
			DataPacket dataPacket = (DataPacket) objectInputStream.readObject();
			
			String tag = dataPacket.getTag();
			
			
			switch (tag) {
			case "registrationAccountName":
				RegistrationAccountName registrationAccountName = (RegistrationAccountName) dataPacket;
				
				accountName = registrationAccountName.getAccountName();
				System.out.println("nguoi dung thu: " + accountName);
				
				if (handleThreadList.size() == 4) {
					
					
					for (int i = 0; i < handleThreadList.size(); i++) {
						System.out.println("client ten la: " + handleThreadList.get(i).accountName);
					}
					System.out.println("full slot 4 player, game begin");
					
					// tạo index cho 52 lá bài
					ArrayList<Integer> deck = new ArrayList<Integer>();
					for (int i = 0; i < 52; i++) {
						deck.add(i);
					}
					
					// xao bai
					Collections.shuffle(deck);
					System.out.println("index bo bai 52 la, sau khi xao bai");
					System.out.println(deck);
					

					for (int i = 0; i < handleThreadList.size(); i++) {
						
						ArrayList<String> nameOfAnotherPlayer = new ArrayList<String>();
						for (int j = 0; j < handleThreadList.size(); j++) {
							if (!handleThreadList.get(j).accountName.equals(handleThreadList.get(i).accountName)) {
								nameOfAnotherPlayer.add(handleThreadList.get(j).accountName);
							}
						}
						
						
						ArrayList<Integer> deckShuffled = new ArrayList<Integer>();
						for (int x = 0; x < 13; x++) {
							// lấy ra 13 lá sau khi xáo bài
							deckShuffled.add(deck.get(x));
							
						}
						
						System.out.println("13 la bai cho client so " + (i+1) + " do la:");
						System.out.println(deckShuffled);
				
						
						for (int y = 0; y < 13; y++) {
							// xóa những lá đã lấy ra khỏi bộ bài
							deck.remove(deckShuffled.get(y));
						}
						
						System.out.println("nhung la bai con lai trong deck la:");
						System.out.println(deck);
						
						
						// gửi những 13 lá bài đến 4 client và set thứ tự đánh cho các client trong ván game
						ResponseDeckShuffled responseDeckShuffled = null;
						ResponseTheFirstBeginRound responseTheFirstBeginRound = null;
						if (i == 0) {
							
							whoIsFristBeginRound = handleThreadList.get(0).accountName;
							// client đầu tiên trong danh sách sẽ là người có lượt đầu tiên và là người bắt đầu round đầu tiên
							responseDeckShuffled = new ResponseDeckShuffled(deckShuffled, nameOfAnotherPlayer, true, true);
							// đã tích hợp nội dung isFristBeginRound vào gói tin ở trên
						} else {
							
							responseDeckShuffled = new ResponseDeckShuffled(deckShuffled, nameOfAnotherPlayer, false, false);
							// đã tích hợp nội dung isFristBeginRound vào gói tin ở trên
						}
						//ResponseDeckShuffled responseDeckShuffled = new ResponseDeckShuffled(deckShuffled);
						DataPacket dataPacket2 = (DataPacket) responseDeckShuffled;
						
						if (accountName == handleThreadList.get(i).accountName) {
							// gửi chính client của thread này
							
							doWrite(dataPacket2);
							System.out.println("da gui den: " + handleThreadList.get(i).accountName);
							
						} else {
							// gửi đến 3 client còn lại
							Socket socket2 = handleThreadList.get(i).socket;
							
							// đây là dòng gây lỗi
							// lỗi này là do mỗi socket chỉ được 1 đối tượng trên bộ nhớ heap gán đến, nếu 2 đối tượng thì sẽ lỗi
							
							//ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(socket2.getOutputStream());
							//objectOutputStream2.writeObject(dataPacket2);
							
							// cach nay cung ok, nhung chua toi uu
							//ObjectOutputStream objectOutputStream2 = Server.handleThreadList.get(i).getObjectOutputStream();
							//objectOutputStream2.writeObject(dataPacket2);
							
							handleThreadList.get(i).doWrite(dataPacket2);
							
							System.out.println("da gui den: " + handleThreadList.get(i).accountName);
						}
						
					}
					
					
				}
				break;
				
			case "RequestEndTurn":
				// thực thi chuyển turn cho các client
				RequestEndTurn requestEndTurn = (RequestEndTurn) dataPacket;
				
				// kiểm tra có kèm deckShuffled (list bài mà client này gửi cho các client khác)
				if (requestEndTurn.getDispatchDeck() == null || requestEndTurn.getDispatchDeck().size() == 0) {
					// nếu không có, có nghĩa là client này bỏ qua lượt
					System.out.println("client " + accountName + " bo luot");
					
					// lấy ra tên của client muốn bỏ qua lượt này
					String thisClientWantEndTurn = requestEndTurn.getClientWantEndTurn();
						
					//ResponseClientTurn responseClientTurn = new ResponseClientTurn(isYourTurn);
					
					for (int i = 0; i < handleThreadList.size(); i++) {
						if (thisClientWantEndTurn == handleThreadList.get(i).accountName) {
							// bỏ qua element này, client này đã tự đánh dấu thuộc isYourTurn của chính client là false
							
							ResponseClientTurn responseClientTurn = new ResponseClientTurn(true);
							DataPacket dataPacket3 = (DataPacket) responseClientTurn;
							if (i == (handleThreadList.size() - 1)) {
								handleThreadList.get(0).doWrite(dataPacket3);
							} else {
								handleThreadList.get(i + 1).doWrite(dataPacket3);
							}
							
							break;
						}
					}
				} else {
					System.out.println("client " + accountName + " danh cac la bai co index sau:");
					System.out.println(requestEndTurn.getDispatchDeck());
					// nếu gói tin này có đi kèm thông số về các lá bài mà client chọn đánh
					// thì gửi những thông số lá bài đó đến các client còn lại
					// không cần phải lo lắng về tính hợp lệ của các lá bài đó, bởi vì
					// tính hợp lệ của những lá bài đó đã được client kiểm tra trước khi gửi đến server
					
					// lấy ra tên của client đã đánh bài ở lượt này
					String thisClientWantGoToEndTurn = requestEndTurn.getClientWantEndTurn();
					//System.out.println("thisClientWantEndTurn: " + thisClientWantGoToEndTurn);
						
					//ResponseClientTurn responseClientTurn = new ResponseClientTurn(isYourTurn);
					
					// để nhường lượt cho client kế tiếp
					for (int i = 0; i < handleThreadList.size(); i++) {
						if (thisClientWantGoToEndTurn == handleThreadList.get(i).accountName) {
							// bỏ qua element này, client này đã tự đánh dấu thuộc isYourTurn của chính client là false
							
							ResponseClientTurn responseClientTurn = new ResponseClientTurn(true);
							DataPacket dataPacket3 = (DataPacket) responseClientTurn;
							if (i == (handleThreadList.size() - 1)) {
								handleThreadList.get(0).doWrite(dataPacket3);
							} else {
								handleThreadList.get(i + 1).doWrite(dataPacket3);
							}
							
							break;
						}
					}
					
					ResponseCardOnTable responseCardOnTable = new ResponseCardOnTable(requestEndTurn.getDispatchDeck(), thisClientWantGoToEndTurn);
					DataPacket dataPacket4 = (DataPacket) responseCardOnTable;
					
					int count = 0;
					// gửi cho tất cả client còn lại về thông số những lá bài mà client này chọn để đánh lên
					for (int i = 0; i < handleThreadList.size(); i++) {
						if (handleThreadList.get(i).accountName != thisClientWantGoToEndTurn) {
							handleThreadList.get(i).doWrite(dataPacket4);
							count++;
						}
					}
					System.out.println(count);
				}
				
				break;
				
			case "RequestTheFirstBeginRound":
				// thực thi chuyển quyền firstBeginRound giữa các client
				RequestTheFirstBeginRound requestTheFirstBeginRound = (RequestTheFirstBeginRound) dataPacket;
				
				String clientSentThisRequest = requestTheFirstBeginRound.getAccountName();
				
				for (int i = 0; i < handleThreadList.size(); i++) {
					if (clientSentThisRequest == handleThreadList.get(i).accountName) {
						// bỏ qua element này, client này đã tự đánh dấu thuộc isFirstBeginRound của chính client là false
						
						ResponseTheFirstBeginRound responseTheFirstBeginRound = new ResponseTheFirstBeginRound(true);
						DataPacket dataPacket5 = (DataPacket) responseTheFirstBeginRound;
						if (i == (handleThreadList.size() - 1)) {
							handleThreadList.get(0).doWrite(dataPacket5);
						} else {
							handleThreadList.get(i + 1).doWrite(dataPacket5);
						}
						break;
					}
					
					
				}
				
				break;
			case "AnnouncementFirstBeginRound":
				// gói này có nhiệm vụ truyên bố client mà đã yêu cầu chức này, chính là client đang "phù hợp"
				// để trở thành một FirstBeginRound
				AnnouncementFirstBeginRound announcementFirstBeginRound = (AnnouncementFirstBeginRound) dataPacket;
				whoIsFristBeginRound = announcementFirstBeginRound.getAccountName();
				for (int i = 0; i < handleThreadList.size(); i++) {
					if (handleThreadList.get(i).accountName != whoIsFristBeginRound) {
						// thông báo đến các thread khác rằng nay đã có whoIsFristBeginRoung mới
						handleThreadList.get(i).whoIsFristBeginRound = whoIsFristBeginRound;
						
						// yêu cầu những client khác với client tại vị, yêu cầu hủy tư cách FristBeginRound
						DestroyFirstBeginRound destroyFirstBeginRound = new DestroyFirstBeginRound();
						DataPacket dataPacket6 = (DataPacket) destroyFirstBeginRound;
						
						handleThreadList.get(i).doWrite(dataPacket6);
						
					}
				}
				
				// test
				System.out.println("/////////////////////");
				for (int i = 0; i < handleThreadList.size(); i++) {
					System.out.println("FristBeginRound of Client "+ handleThreadList.get(i).accountName+ "is " + handleThreadList.get(i).whoIsFristBeginRound);
				}
				System.out.println("///////////////////");
				
				
				break;
				
			case "RequestSendMessenger":
				// thực hiện nhận một tin nhắn từ client nào đó, và gửi đến những client còn lại
				RequestSendMessenger requestSendMessenger = (RequestSendMessenger) dataPacket;
				
				String whoSendThisMessenger = requestSendMessenger.getAccountName();
				
				ResponseSendMessenger responseSendMessenger = new ResponseSendMessenger(whoSendThisMessenger, requestSendMessenger.getContentMessenger());
				
				for (int i = 0; i < handleThreadList.size(); i++) {
					if (!whoSendThisMessenger.equals(handleThreadList.get(i).accountName)) {
						DataPacket dataPacket10 = (DataPacket) responseSendMessenger;
						handleThreadList.get(i).doWrite(dataPacket10);
					}
				}
				
				break;
				
			case "DeclareWinner":
				DeclareWinner declareWinner = (DeclareWinner) dataPacket;
				
				String whoDeclareWinner = declareWinner.getAccountName();
				
				if (numberOne.isEmpty()) {
					numberOne = whoDeclareWinner;
					ResponseWinner responseWinner = new ResponseWinner(numberOne, null, null, null);
					DataPacket dataPacketResponseWinner = (DataPacket) responseWinner;
					doWrite(dataPacketResponseWinner);
					
				} else if (numberTwo.isEmpty() && numberOne.isEmpty() == false) {
					numberTwo = whoDeclareWinner;
					ResponseWinner responseWinner = new ResponseWinner(numberOne, numberTwo, null, null);
					DataPacket dataPacketResponseWinner = (DataPacket) responseWinner;
					doWrite(dataPacketResponseWinner);
				} else if (numberThree.isEmpty() && numberTwo.isEmpty() == false) {
					numberThree = whoDeclareWinner;
					ResponseWinner responseWinner = new ResponseWinner(numberOne, numberTwo, numberThree, null);
					DataPacket dataPacketResponseWinner = (DataPacket) responseWinner;
					doWrite(dataPacketResponseWinner);
				} else if (numberFour.isEmpty() && numberThree.isEmpty() == false) {
					numberFour = whoDeclareWinner;
					ResponseWinner responseWinner = new ResponseWinner(numberOne, numberTwo, numberThree, numberFour);
					DataPacket dataPacketResponseWinner = (DataPacket) responseWinner;
					doWrite(dataPacketResponseWinner);
				}
				
				break;
				
			case "RequestSendFileData":
				RequestSendFileData requestSendFileData = (RequestSendFileData) dataPacket;
				File file = new File("C:\\Users\\Acer\\Downloads\\" + requestSendFileData.getFile().getName());
				byte[] content = requestSendFileData.getContent();
				//Files.write(file.toPath(), content);
				doWrite(content, file);
				break;
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void doWrite(DataPacket dataPacket) {
		try {
			objectOutputStream.writeObject(dataPacket);
			objectOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	FileOutputStream fos;
	public void doWrite(byte[] content, File file) {
		try {
			System.out.println(file.getAbsolutePath());
			fos = new FileOutputStream(file);
			fos.write(content);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendDeckShuffledTo(ArrayList<Integer> deckShuffled) {
		try {
			objectOutputStream.writeObject(deckShuffled);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getIntIndexFromStringIndex(String accountname) {
		for (int i = 0; i < handleThreadList.size(); i++) {
			if (accountname == handleThreadList.get(i).accountName) {
				return i;
			}
		}
		
		return -1;
	}
}
