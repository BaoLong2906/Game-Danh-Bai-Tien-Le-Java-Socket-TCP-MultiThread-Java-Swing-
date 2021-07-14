package Client;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.Cleaner;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import DataPacket.DataPacket;
import DataPacket.DeclareWinner;
import DataPacket.RegistrationAccountName;
import DataPacket.ResponseCardOnTable;
import DataPacket.ResponseDeckShuffled;
import DataPacket.ResponseIdRoom;
import DataPacket.ResponseSendMessenger;
import DataPacket.ResponseWinner;
import ModelCard.Card;
import ModelCard.CardFactory;
import Storage.Storage;
import Storage.UserInfor;

public class ClientThread extends Thread{
	
	ClientGameplay clientGameplay;
	Socket socket;
	ObjectInputStream objectInputStream;
	ObjectOutputStream objectOutputStream;
	String accountName;
	
	ArrayList<Card> originalCardList;
	
	ArrayList<Integer> deckShuffled;
	
	//InputThread inputThread;
	
	boolean run = true;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		
		
		// gửi đến server giá trị của string accountName
		firstSending();
		
		
//		while (true) {
//		doRead();
//		}
		
		InputThread inputThread = new InputThread();
		inputThread.start();
	}
	
	public ClientThread(ClientGameplay clientGameplay) {
		
		this.clientGameplay = clientGameplay;
		socket = clientGameplay.socket;
		//accountName = clientGameplay.getAccountName();
		accountName = UserInfor.accountName;
		
		try {
			//objectInputStream = new ObjectInputStream(socket.getInputStream());
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			//objectInputStream = new ObjectInputStream(socket.getInputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void firstSending() {
		System.out.println("fristSending() has been called");
		
		RegistrationAccountName registrationAccountName = new RegistrationAccountName(accountName);
		System.out.println(accountName);
		
		DataPacket dataPacket = (DataPacket) registrationAccountName;
		
		doWrite(dataPacket);
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
	
	public void doRead() {
		try {
			//objectInputStream = new ObjectInputStream(socket.getInputStream());
			System.out.println(objectInputStream.available());
			if (objectInputStream.available() > 0) {
				JOptionPane.showMessageDialog(clientGameplay, "Eggs are not supposed to be green.");
			}
			if (objectInputStream.available() != 0) {
				DataPacket dataPacket = (DataPacket) objectInputStream.readObject();
				
				String tag = dataPacket.getTag();
				
				switch (tag) {
				case "ResponseDeckShuffled":
					ResponseDeckShuffled responseDeckShuffled = (ResponseDeckShuffled) dataPacket;
					
					// lấy ra chỉ mục index 13 lá bài được server chia
					deckShuffled = responseDeckShuffled.getDeckShuffled();
					
					System.out.println("it worked !");
					//objectInputStream.close();
					break;
				}
			} else {
				//System.out.println("khong co gi de lam");
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class InputThread extends Thread {
		ObjectInputStream objectInputStream;
		boolean run = true;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (run) {
//				try {
//					sleep(2500);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				doRead();
			}
			
		}
		public InputThread() {
			// TODO Auto-generated constructor stub
			try {
				objectInputStream = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public synchronized void doRead() {
//			try {
//				
//				if (objectInputStream.available() != 0) {
//					System.out.println("tien trinh busy");
//					
//					try {
//						//objectInputStream = new ObjectInputStream(socket.getInputStream());
//						DataPacket dataPacket = (DataPacket) objectInputStream.readObject();
//						
//						String tag = dataPacket.getTag();
//						
//						switch (tag) {
//						case "ResponseDeckShuffled":
//							ResponseDeckShuffled responseDeckShuffled = (ResponseDeckShuffled) dataPacket;
//							
//							// lấy ra chỉ mục index 13 lá bài được server chia
//							deckShuffled = responseDeckShuffled.getDeckShuffled();
//							
//							System.out.println("it worked !");
//							JOptionPane.showMessageDialog(clientGameplay, "Eggs are not supposed to be green.");
//							break;
//						}
//						
//						
//						//objectInputStream.close();
//						
//					} catch (ClassNotFoundException | IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				} else {
//					//System.out.println("tien trinh input ranh");
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			
			try {
				//objectInputStream = new ObjectInputStream(socket.getInputStream());
				DataPacket dataPacket = (DataPacket) objectInputStream.readObject();
				
				String tag = dataPacket.getTag();
				
				switch (tag) {
					case "ResponseDeckShuffled":
						// lệnh này là khi server chia bài cho các client
						ResponseDeckShuffled responseDeckShuffled = (ResponseDeckShuffled) dataPacket;
						
						// lấy ra thứ tự đánh bài
						clientGameplay.isYourTurn = responseDeckShuffled.getIsYourTurn();
						if (clientGameplay.isYourTurn == true) {
							clientGameplay.lblYourTurn.setText("Đến lượt bạn đánh bài");
						}
						// lấy ra quyền firstBeginRound
						clientGameplay.isFirstBeginRound = responseDeckShuffled.getIsFirstBeginRound();
						// lấy ra tên của những client khác để hiển thị tên của những người chơi khác
						clientGameplay.nameOfAnotherPlayer = responseDeckShuffled.getNameOfAnotherPlayer();
						
						// lấy ra chỉ mục index 13 lá bài được server chia
						deckShuffled = responseDeckShuffled.getDeckShuffled();
						System.out.println(deckShuffled);
						
						System.out.println("ResponseDeckShuffled has been called !");
						
						for (int i = 0; i < 13; i++) {
							// unboxing
							int index = deckShuffled.get(i);
							// gán đối tượng thông tin giao diện lá bài phù hợp theo index
							Card card = clientGameplay.originalCardList.get(index);
							
							clientGameplay.deckShuffledList.add(card);
						}
						
						// lưu trữ đối tượng imageIcon
						for (int i = 0; i < 13; i++) {
							System.out.println("go here 1");
							BufferedImage bufferdImage = ImageIO.read(new File(clientGameplay.deckShuffledList.get(i).getUrl()));
							Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
							ImageIcon imageIcon = new ImageIcon(imageScaled);
							System.out.println("go here 2");
							
							clientGameplay.imageIconList.add(imageIcon);
							//JLabel lblNewLabel = new JLabel(imageIcon);
							System.out.println("go here 3");
						}
						
						clientGameplay.createDeckGUI();
						
						break;
						
					case "ResponseClientTurn":
						// nếu nhận được gói tin này thì turn hiện tại là của client này
						clientGameplay.isYourTurn = true;
						System.out.println("my turn now is: " + clientGameplay.isYourTurn);
						clientGameplay.lblYourTurn.setText("Đến lượt bạn đánh bài");
						
						break;
						
					case "ResponseTheFirstBeginRound":
						
						clientGameplay.isFirstBeginRound = true;
						System.out.println("this turn has became TheFirstBeginRound");
						
						break;
						
					case "ResponseCardOnTable":
						// import các lá bài trên bàn mà các client khác đánh
						// nếu là client này đánh thì, client này tự hiển thị bài của chính nó
						// không cần phải import, chỉ trừ khi là client khác đánh, không phải client này
						ResponseCardOnTable responseCardOnTable = (ResponseCardOnTable) dataPacket;
						
						// hiển thị ai là người đã đánh những lá bài đó
						clientGameplay.lblLuotBaiCua.setText(responseCardOnTable.getAccountName());
						
						// tính toán những lá bài còn lại của những client khác
						if (!clientGameplay.accountName.equals(responseCardOnTable.getAccountName())) {
							for (int j = 0; j < clientGameplay.nameOfAnotherPlayer.size(); j++) {
								// update số lá bài còn lại, dựa trên tên của client đã đánh bài này lên
								String name = clientGameplay.nameOfAnotherPlayer.get(j);
								System.out.println("name: " + name);
								System.out.println("responseCardOnTable: " + responseCardOnTable.getAccountName());
								if (responseCardOnTable.getAccountName().equals(name)) {
									// kiểm tra client đánh bài lên là client nào trong danh sách các client trong ván game
									// tìm ra được rồi thì tiến hành update số lá bài còn lại, và vẽ lại label
									if (name.equals(clientGameplay.lblAnotherClientSlotOne.getText().toString())) {
										clientGameplay.numCardClientOne -= responseCardOnTable.getDeckImport().size();
										clientGameplay.numberCardSlotOne.setText(String.valueOf(clientGameplay.numCardClientOne));
										
										if (clientGameplay.numCardClientOne == 0) {
											DeclareWinner declareWinner = new DeclareWinner(clientGameplay.accountName);
											DataPacket dataPacketDeclareWinner = (DataPacket) declareWinner;
											doWrite(dataPacketDeclareWinner);
										}
									}
										
									if (name.equals(clientGameplay.lblAnotherClientSlotTwo.getText().toString())) {
										clientGameplay.numCardClientTwo -= responseCardOnTable.getDeckImport().size();
										clientGameplay.numberCardSlotTwo.setText(String.valueOf(clientGameplay.numCardClientTwo));
										
										if (clientGameplay.numCardClientTwo == 0) {
											DeclareWinner declareWinner = new DeclareWinner(clientGameplay.accountName);
											DataPacket dataPacketDeclareWinner = (DataPacket) declareWinner;
											doWrite(dataPacketDeclareWinner);
										}
									}
										
									if (name.equals(clientGameplay.lblAnotherClientSlotThree.getText().toString())) {
										clientGameplay.numCardClientThree -= responseCardOnTable.getDeckImport().size();
										clientGameplay.numberCardSlotThree.setText(String.valueOf(clientGameplay.numCardClientThree));
										
										if (clientGameplay.numCardClientThree == 0) {
											DeclareWinner declareWinner = new DeclareWinner(clientGameplay.accountName);
											DataPacket dataPacketDeclareWinner = (DataPacket) declareWinner;
											doWrite(dataPacketDeclareWinner);
										}
									}
										
								}
							}
							//if (responseCardOnTable.getAccountName().equals(client))
						}
						
						// tại sao clear() cardchosedList tại thread lại không lỗi ? 
						//clientGameplay.getCardChosedList().clear();
						//System.out.println("test o clientthread: " + clientGameplay.cardChosedList.size());
						//clientGameplay.cardChosedList.clear();
						clientGameplay.getCardOnTable().clear();
						clientGameplay.imageIconOnTableList.clear();
						
						for (int i = 0; i < responseCardOnTable.getDeckImport().size(); i++) {
							// unboxing
							int index = responseCardOnTable.getDeckImport().get(i);
							
							// gán đối tượng thông tin giao diện lá bài phù hợp theo index
							Card card = clientGameplay.originalCardList.get(index);
							
							clientGameplay.cardOnTable.add(card);
							
							for (int y = 0; y < clientGameplay.cardOnTable.size(); y++) {
								System.out.println("clientthread say, it geted: " + clientGameplay.cardOnTable.get(y).getPoint());
							}
						}
						clientGameplay.cardOnTable = CardFactory.sortDeck(clientGameplay.cardOnTable);
						System.out.println("hello " + clientGameplay.cardOnTable);
						for (int y = 0; y < clientGameplay.cardOnTable.size(); y++) {
							System.out.println("part 2 clientthread say, it geted: " + clientGameplay.cardOnTable.get(y).getPoint());
						}
						// chọn lựa và lưu trữ đối tượng của imageIconOnTableList
						for (int i = 0; i < clientGameplay.cardOnTable.size(); i++) {
							BufferedImage bufferdImage = ImageIO.read(new File(clientGameplay.cardOnTable.get(i).getUrl()));
							Image imageScaled = bufferdImage.getScaledInstance(61, 90, Image.SCALE_SMOOTH);
							ImageIcon imageIcon = new ImageIcon(imageScaled);
							
							clientGameplay.imageIconOnTableList.add(imageIcon);
							//JLabel lblNewLabel = new JLabel(imageIcon);

						}
						
						//Storage.deckOnTable = responseCardOnTable.deckImport;
						
						//clientGameplay.cardOnTable = responseCardOnTable.getDeckImport();
						clientGameplay.displayCardOnTable();
						
						break;
						
					case "DestroyFirstBeginRound":
						// hủy giá trị fristbeginround
						clientGameplay.isFirstBeginRound = false;
						System.out.println("this turn now isn't TheFirstBeginRound");
						
						break;
						
					case "ResponseSendMessenger":
						
						ResponseSendMessenger responseSendMessenger = (ResponseSendMessenger) dataPacket;
						
						boolean isPrivateMessenger = false;
						boolean isPrivateMessengerForYou = false;
						
						if (responseSendMessenger.getContentMessenger().contains("@private-")) {
							isPrivateMessenger = true;
						}
						
						if (responseSendMessenger.getContentMessenger().contains("@private-"+clientGameplay.getAccountName())) {
							isPrivateMessengerForYou = true;
						}
						
						if (!isPrivateMessenger) {
							String string = responseSendMessenger.getAccountName() + ": " + responseSendMessenger.getContentMessenger() + "\n";
							
							clientGameplay.logMessenger.append(string);
							clientGameplay.textPaneDisplayChat.setText(clientGameplay.logMessenger.toString());
						}
						
						if (isPrivateMessengerForYou) {
							String string = responseSendMessenger.getAccountName() + " gửi một tín nhắn riêng: " + responseSendMessenger.getContentMessenger() + "\n";
							
							clientGameplay.logMessenger.append(string);
							clientGameplay.textPaneDisplayChat.setText(clientGameplay.logMessenger.toString());
						}
						
						
						break;
						
					case "ResponseWinner":
						
						ResponseWinner responseWinner = (ResponseWinner) dataPacket;
						
						String winnerOne   = responseWinner.winnerOne;
						String winnerTwo   = responseWinner.winnerTwo;
						String winnerThree = responseWinner.winnerThree;
						String winnerFour  = responseWinner.winnerFour;
						
						if (winnerOne != null) {
							if (winnerOne.equals(clientGameplay.lblAnotherClientSlotOne.getText())) {
								clientGameplay.winnerResultSlotOne.setText("về Nhất");
							} else if (winnerOne.equals(clientGameplay.lblAnotherClientSlotTwo.getText())) {
								clientGameplay.winnerResultSlotTwo.setText("về Nhất");
							} else if (winnerOne.equals(clientGameplay.lblAnotherClientSlotThree.getText())) {
								clientGameplay.winnerResultSlotThree.setText("về Nhất");
							}
						} 
						
						if (winnerTwo != null) {
							if (winnerTwo.equals(clientGameplay.lblAnotherClientSlotOne.getText())) {
								clientGameplay.winnerResultSlotOne.setText("về Nhì");
							} else if (winnerTwo.equals(clientGameplay.lblAnotherClientSlotTwo.getText())) {
								clientGameplay.winnerResultSlotTwo.setText("về Nhì");
							} else if (winnerTwo.equals(clientGameplay.lblAnotherClientSlotThree.getText())) {
								clientGameplay.winnerResultSlotThree.setText("về Nhì");
							}
						}
						
						if (winnerThree != null) {
							if (winnerThree.equals(clientGameplay.lblAnotherClientSlotOne.getText())) {
								clientGameplay.winnerResultSlotOne.setText("về Ba");
							} else if (winnerThree.equals(clientGameplay.lblAnotherClientSlotTwo.getText())) {
								clientGameplay.winnerResultSlotTwo.setText("về Ba");
							} else if (winnerThree.equals(clientGameplay.lblAnotherClientSlotThree.getText())) {
								clientGameplay.winnerResultSlotThree.setText("về Ba");
							}
						}
						
						if (winnerFour != null) {
							if (winnerFour.equals(clientGameplay.lblAnotherClientSlotOne.getText())) {
								clientGameplay.winnerResultSlotOne.setText("về Tư");
							} else if (winnerFour.equals(clientGameplay.lblAnotherClientSlotTwo.getText())) {
								clientGameplay.winnerResultSlotTwo.setText("về Tư");
							} else if (winnerFour.equals(clientGameplay.lblAnotherClientSlotThree.getText())) {
								clientGameplay.winnerResultSlotThree.setText("về Tư");
							}
						}
						
						break;
						
					case "ResponseIdRoom":
						ResponseIdRoom responseIdRoom = (ResponseIdRoom) dataPacket;
						int idRoom = responseIdRoom.getIdRoom();
						idRoom++;
						clientGameplay.lblRoomNumber.setText(String.valueOf(idRoom));
						break;
						
				}
				
				//objectInputStream.close();
				
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
		
	}
}
