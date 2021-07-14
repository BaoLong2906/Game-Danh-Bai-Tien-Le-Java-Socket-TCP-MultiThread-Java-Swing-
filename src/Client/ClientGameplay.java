package Client;

import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DataPacket.AnnouncementFirstBeginRound;
import DataPacket.DataPacket;
import DataPacket.RequestEndTurn;
import DataPacket.RequestSendMessenger;
import DataPacket.RequestTheFirstBeginRound;
import FileExplore.FileExplore;
import ModelCard.Card;
import ModelCard.CardFactory;
import Storage.UserInfor;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

public class ClientGameplay extends JFrame {

	private JPanel contentPane;
	
	public Socket socket;
	public String accountName = UserInfor.accountName;
	
	public ClientThread clientThread;
	
	//-------------------------------------------
	public boolean isYourTurn;
	public boolean isFirstBeginRound;
	//-------------------------------------------
	
	// full đối tượng 52 lá và các lá hide
	public ArrayList<Card> originalCardList = new ArrayList<Card>();
	
	// full đối tượng 52 lá client được  chọn
	public ArrayList<Card> originalCardChosedList = new ArrayList<Card>();
	
	//-------------------------------------------
	
	// giao diện các lá mà server đã xáo gửi về client index
	public ArrayList<Card> deckShuffledList =  new ArrayList<Card>();
	
	public ArrayList<ImageIcon> imageIconList = new ArrayList<ImageIcon>();
	
	// lưu chữ 0 và 1 giữa 2 trạng thái chưa pick và picked
	public int[] slotStatus = new int[13];
	
	//-------------------------------------------
	
	// danh sách các lá bài mà client chọn ra để đánh bài
	public ArrayList<Card> cardChosedList = new ArrayList<Card>();
	
	// danh sách các lá bài hiện đang ở trên bàn trong lượt hiện tại
	public ArrayList<Card> cardOnTable = new ArrayList<Card>();
	
	public ArrayList<Card> tempCardOnTable = new ArrayList<Card>();
	
	// tập hợp các imageIcon của các lá bài đã đánh trên bàn
	public ArrayList<ImageIcon> imageIconOnTableList = new ArrayList<ImageIcon>(); 
	
	public ArrayList<Card> firedCardList = new ArrayList<Card>();
	
	public Card hiddenCard;
	
	public boolean stateOfBtnMic = false;
	
	public boolean isOver = false;
	
	public String prizeName;
	
	public ArrayList<Card> getCardChosedList() {
		return cardChosedList;
	}
	
	public ArrayList<Card> getCardOnTable() {
		return cardOnTable;
	}
	
	public void setCardChosedList(ArrayList<Card> cardChosedList) {
		this.cardChosedList = cardChosedList;
	}
	
	public void setCardOnTable(ArrayList<Card> cardOnTable) {
		this.cardOnTable = cardOnTable;
	}
	
	public static String resourcePath = "F:\\ecilpse-workspace\\PlayingCardGame\\Resource\\";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGameplay frame = new ClientGameplay();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	JLabel lblYourTurn;
	//----------------------------------
	JLabel lblSlotCard1;
	JLabel lblSlotCard2;
	JLabel lblSlotCard3;
	JLabel lblSlotCard4;
	JLabel lblSlotCard5;
	JLabel lblSlotCard6;
	JLabel lblSlotCard7;
	JLabel lblSlotCard8;
	JLabel lblSlotCard9;
	JLabel lblSlotCard10;
	JLabel lblSlotCard11;
	JLabel lblSlotCard12;
	JLabel lblSlotCard13;
	ArrayList<JLabel> jLabelSlotList = new ArrayList<JLabel>();
	
	ArrayList<String> nameOfAnotherPlayer = new ArrayList<String>();
	//----------------------------------
	
	// hàm này là để luồng clientThread gọi, hàm này để vẽ những lá bài của người chơi.
	public void createDeckGUI() {
		System.out.println("createDeckGUI has been called");
		System.out.println("my turn now is: " + isYourTurn);
		System.out.println("my status of theFirstBeginRound now is: " + isFirstBeginRound);
		
		
		lblAnotherClientSlotOne.setText(nameOfAnotherPlayer.get(0).toString());
		lblAnotherClientSlotTwo.setText(nameOfAnotherPlayer.get(1).toString());
		lblAnotherClientSlotThree.setText(nameOfAnotherPlayer.get(2).toString());
		
//		if (isYourTurn == true) {
//			lblYourTurn.setText("Đây là lượt của bạn");
//			if (isFirstBeginRound == true && cardOnTable.size() == 0) {
//				lblYourTurn.setText("Bạn hãy bất đầu một tổ hợp đánh mới");
//			}
//			if (isFirstBeginRound == false && cardOnTable.size() != 0) {
//				lblYourTurn.setText("Những người chơi khác đã bỏ lượt, bạn hãy đánh tiếp một tổ hợp mới");
//			}
//		} else {
//			lblYourTurn.setText("Chưa tới lượt của bạn");
//		}
		
		lblSlotCard1 = new JLabel(imageIconList.get(0));
		lblSlotCard1.setBounds(50, 411, 92, 113);
		lblSlotCard1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				super.mouseClicked(arg0);
				
				if (slotStatus[0] == 0) {
					BufferedImage bufferdImage;
					try {
						int index = deckShuffledList.get(0).getIndex();
						System.out.println("pick card is: " + index);
						bufferdImage = ImageIO.read(new File(originalCardChosedList.get(index).getUrl()));
						System.out.println("url is " + originalCardChosedList.get(index).getUrl());
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard1.setIcon(imageIcon);
						
						Card card = deckShuffledList.get(0);
						cardChosedList.add(card);
						System.out.println("client " + accountName + " da chon card \n" + card.getUrl());
						
						slotStatus[0] = 1;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// nếu như là đã pick rồi, thì chuyển về lại hình chưa chuyển
					slotStatus[0] = 0;

					BufferedImage bufferdImage;
					try {
						bufferdImage = ImageIO.read(new File(deckShuffledList.get(0).getUrl()));
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard1.setIcon(imageIcon);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// remove không chọn card này nữa thì xóa khỏi danh sách
					Card card = deckShuffledList.get(0);
					for (int i = 0; i < cardChosedList.size(); i++) {
						if (card.getIndex() == cardChosedList.get(i).getIndex()) {
							cardChosedList.remove(card);
						}
					}
					System.out.println("client " + accountName + " da dung chon card \n" + card.getUrl());
					System.out.println("kich thuoc cua danh sach la bai, client muon danh la " + cardChosedList.size());
					
				}
			}
		});
		contentPane.add(lblSlotCard1);
		//lblSlotCardList.add(lblSlotCard1);
		
		lblSlotCard2 = new JLabel(imageIconList.get(1));
		lblSlotCard2.setBounds(152, 411, 92, 113);
		lblSlotCard2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				super.mouseClicked(arg0);
				if (slotStatus[1] == 0) {
					BufferedImage bufferdImage;
					try {
						int index = deckShuffledList.get(1).getIndex();
						System.out.println("pick card is: " + index);
						bufferdImage = ImageIO.read(new File(originalCardChosedList.get(index).getUrl()));
						System.out.println("url is " + originalCardChosedList.get(index).getUrl());
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard2.setIcon(imageIcon);
						
						Card card = deckShuffledList.get(1);
						cardChosedList.add(card);
						System.out.println("client " + accountName + " da chon card \n" + card.getUrl());
						
						for (int i = 0; i < cardOnTable.size(); i++) {
							System.out.println(cardOnTable.get(i).getPoint());
						}
						
						slotStatus[1] = 1;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// nếu như là đã pick rồi, thì chuyển về lại hình chưa chuyển
					slotStatus[1] = 0;

					BufferedImage bufferdImage;
					try {
						bufferdImage = ImageIO.read(new File(deckShuffledList.get(1).getUrl()));
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard2.setIcon(imageIcon);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// remove không chọn card này nữa thì xóa khỏi danh sách
					Card card = deckShuffledList.get(1);
					for (int i = 0; i < cardChosedList.size(); i++) {
						if (card.getIndex() == cardChosedList.get(i).getIndex()) {
							cardChosedList.remove(card);
						}
					}
					System.out.println("client " + accountName + " da dung chon card \n" + card.getUrl());
					System.out.println("kich thuoc cua danh sach client muon danh la " + cardChosedList.size());
				}
			}
		});
		contentPane.add(lblSlotCard2);
		//lblSlotCardList.add(lblSlotCard2);
		
		lblSlotCard3 = new JLabel(imageIconList.get(2));
		lblSlotCard3.setBounds(266, 411, 92, 113);
		lblSlotCard3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				super.mouseClicked(arg0);
				if (slotStatus[2] == 0) {
					BufferedImage bufferdImage;
					try {
						int index = deckShuffledList.get(2).getIndex();
						System.out.println("pick card is: " + index);
						bufferdImage = ImageIO.read(new File(originalCardChosedList.get(index).getUrl()));
						System.out.println("url is " + originalCardChosedList.get(index).getUrl());
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard3.setIcon(imageIcon);
						
						Card card = deckShuffledList.get(2);
						cardChosedList.add(card);
						System.out.println("client " + accountName + " da chon card \n" + card.getUrl());
						
						slotStatus[2] = 1;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// nếu như là đã pick rồi, thì chuyển về lại hình chưa chuyển
					slotStatus[2] = 0;

					BufferedImage bufferdImage;
					try {
						bufferdImage = ImageIO.read(new File(deckShuffledList.get(2).getUrl()));
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard3.setIcon(imageIcon);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// remove không chọn card này nữa thì xóa khỏi danh sách
					Card card = deckShuffledList.get(2);
					for (int i = 0; i < cardChosedList.size(); i++) {
						if (card.getIndex() == cardChosedList.get(i).getIndex()) {
							cardChosedList.remove(card);
						}
					}
					System.out.println("client " + accountName + " da dung chon card \n" + card.getUrl());
					System.out.println("kich thuoc cua danh sach client muon danh la " + cardChosedList.size());
				}
			}
		});
		contentPane.add(lblSlotCard3);
		//lblSlotCardList.add(lblSlotCard3);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.YELLOW);
		separator.setBackground(Color.YELLOW);
		separator.setBounds(138, 402, 2, 141);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setForeground(Color.YELLOW);
		separator_1.setBackground(Color.YELLOW);
		separator_1.setBounds(254, 402, 2, 141);
		contentPane.add(separator_1);
		
		lblSlotCard4 = new JLabel(imageIconList.get(3));
		lblSlotCard4.setBounds(390, 411, 92, 113);
		lblSlotCard4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				super.mouseClicked(arg0);
				if (slotStatus[3] == 0) {
					BufferedImage bufferdImage;
					try {
						int index = deckShuffledList.get(3).getIndex();
						System.out.println("pick card is: " + index);
						bufferdImage = ImageIO.read(new File(originalCardChosedList.get(index).getUrl()));
						System.out.println("url is " + originalCardChosedList.get(index).getUrl());
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard4.setIcon(imageIcon);
						
						Card card = deckShuffledList.get(3);
						cardChosedList.add(card);
						System.out.println("client " + accountName + " da chon card \n" + card.getUrl());
						
						slotStatus[3] = 1;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// nếu như là đã pick rồi, thì chuyển về lại hình chưa chuyển
					slotStatus[3] = 0;

					BufferedImage bufferdImage;
					try {
						bufferdImage = ImageIO.read(new File(deckShuffledList.get(3).getUrl()));
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard4.setIcon(imageIcon);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// remove không chọn card này nữa thì xóa khỏi danh sách
					Card card = deckShuffledList.get(3);
					for (int i = 0; i < cardChosedList.size(); i++) {
						if (card.getIndex() == cardChosedList.get(i).getIndex()) {
							cardChosedList.remove(card);
						}
					}
					System.out.println("client " + accountName + " da dung chon card \n" + card.getUrl());
					System.out.println("kich thuoc cua danh sach client muon danh la " + cardChosedList.size());
				}
			}
		});
		contentPane.add(lblSlotCard4);
		//lblSlotCardList.add(lblSlotCard4);
		
		lblSlotCard5 = new JLabel(imageIconList.get(4));
		lblSlotCard5.setBounds(503, 411, 92, 113);
		lblSlotCard5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				super.mouseClicked(arg0);
				if (slotStatus[4] == 0) {
					BufferedImage bufferdImage;
					try {
						int index = deckShuffledList.get(4).getIndex();
						System.out.println("pick card is: " + index);
						bufferdImage = ImageIO.read(new File(originalCardChosedList.get(index).getUrl()));
						System.out.println("url is " + originalCardChosedList.get(index).getUrl());
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard5.setIcon(imageIcon);
						
						Card card = deckShuffledList.get(4);
						cardChosedList.add(card);
						System.out.println("client " + accountName + " da chon card \n" + card.getUrl());
						
						slotStatus[4] = 1;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// nếu như là đã pick rồi, thì chuyển về lại hình chưa chuyển
					slotStatus[4] = 0;

					BufferedImage bufferdImage;
					try {
						bufferdImage = ImageIO.read(new File(deckShuffledList.get(4).getUrl()));
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard5.setIcon(imageIcon);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// remove không chọn card này nữa thì xóa khỏi danh sách
					Card card = deckShuffledList.get(4);
					for (int i = 0; i < cardChosedList.size(); i++) {
						if (card.getIndex() == cardChosedList.get(i).getIndex()) {
							cardChosedList.remove(card);
						}
					}
					System.out.println("client " + accountName + " da dung chon card \n" + card.getUrl());
					System.out.println("kich thuoc cua danh sach client muon danh la " + cardChosedList.size());
				}
			}
		});
		contentPane.add(lblSlotCard5);
		//lblSlotCardList.add(lblSlotCard5);
		
		JSeparator separator_1_1 = new JSeparator();
		separator_1_1.setOrientation(SwingConstants.VERTICAL);
		separator_1_1.setForeground(Color.YELLOW);
		separator_1_1.setBackground(Color.YELLOW);
		separator_1_1.setBounds(368, 402, 2, 141);
		contentPane.add(separator_1_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setForeground(Color.YELLOW);
		separator_2.setBackground(Color.YELLOW);
		separator_2.setBounds(491, 402, 2, 141);
		contentPane.add(separator_2);
		
		JSeparator separator_2_1 = new JSeparator();
		separator_2_1.setOrientation(SwingConstants.VERTICAL);
		separator_2_1.setForeground(Color.YELLOW);
		separator_2_1.setBackground(Color.YELLOW);
		separator_2_1.setBounds(669, 402, -15, 141);
		contentPane.add(separator_2_1);
		
		JSeparator separator_1_1_1 = new JSeparator();
		separator_1_1_1.setOrientation(SwingConstants.VERTICAL);
		separator_1_1_1.setForeground(Color.YELLOW);
		separator_1_1_1.setBackground(Color.YELLOW);
		separator_1_1_1.setBounds(605, 402, 2, 141);
		contentPane.add(separator_1_1_1);
		
		lblSlotCard6 = new JLabel(imageIconList.get(5));
		lblSlotCard6.setBounds(617, 411, 92, 113);
		lblSlotCard6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				super.mouseClicked(arg0);
				if (slotStatus[5] == 0) {
					BufferedImage bufferdImage;
					try {
						int index = deckShuffledList.get(5).getIndex();
						System.out.println("pick card is: " + index);
						bufferdImage = ImageIO.read(new File(originalCardChosedList.get(index).getUrl()));
						System.out.println("url is " + originalCardChosedList.get(index).getUrl());
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard6.setIcon(imageIcon);
						
						Card card = deckShuffledList.get(5);
						cardChosedList.add(card);
						System.out.println("client " + accountName + " da chon card \n" + card.getUrl());
						
						slotStatus[5] = 1;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// nếu như là đã pick rồi, thì chuyển về lại hình chưa chuyển
					slotStatus[5] = 0;

					BufferedImage bufferdImage;
					try {
						bufferdImage = ImageIO.read(new File(deckShuffledList.get(5).getUrl()));
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard6.setIcon(imageIcon);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// remove không chọn card này nữa thì xóa khỏi danh sách
					Card card = deckShuffledList.get(5);
					for (int i = 0; i < cardChosedList.size(); i++) {
						if (card.getIndex() == cardChosedList.get(i).getIndex()) {
							cardChosedList.remove(card);
						}
					}
					System.out.println("client " + accountName + " da dung chon card \n" + card.getUrl());
					System.out.println("kich thuoc cua danh sach client muon danh la " + cardChosedList.size());
				}
			}
		});
		contentPane.add(lblSlotCard6);
		//lblSlotCardList.add(lblSlotCard6);
		
		JSeparator separator_1_1_1_1 = new JSeparator();
		separator_1_1_1_1.setOrientation(SwingConstants.VERTICAL);
		separator_1_1_1_1.setForeground(Color.YELLOW);
		separator_1_1_1_1.setBackground(Color.YELLOW);
		separator_1_1_1_1.setBounds(719, 402, 2, 141);
		contentPane.add(separator_1_1_1_1);
		
		lblSlotCard7 = new JLabel(imageIconList.get(6));
		lblSlotCard7.setBounds(731, 411, 92, 113);
		lblSlotCard7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				super.mouseClicked(arg0);
				if (slotStatus[6] == 0) {
					BufferedImage bufferdImage;
					try {
						int index = deckShuffledList.get(6).getIndex();
						System.out.println("pick card is: " + index);
						bufferdImage = ImageIO.read(new File(originalCardChosedList.get(index).getUrl()));
						System.out.println("url is " + originalCardChosedList.get(index).getUrl());
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard7.setIcon(imageIcon);
						
						Card card = deckShuffledList.get(6);
						cardChosedList.add(card);
						System.out.println("client " + accountName + " da chon card \n" + card.getUrl());
						
						slotStatus[6] = 1;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// nếu như là đã pick rồi, thì chuyển về lại hình chưa chuyển
					slotStatus[6] = 0;

					BufferedImage bufferdImage;
					try {
						bufferdImage = ImageIO.read(new File(deckShuffledList.get(6).getUrl()));
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard7.setIcon(imageIcon);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// remove không chọn card này nữa thì xóa khỏi danh sách
					Card card = deckShuffledList.get(6);
					for (int i = 0; i < cardChosedList.size(); i++) {
						if (card.getIndex() == cardChosedList.get(i).getIndex()) {
							cardChosedList.remove(card);
						}
					}
					System.out.println("client " + accountName + " da dung chon card \n" + card.getUrl());
					System.out.println("kich thuoc cua danh sach client muon danh la " + cardChosedList.size());
				}
			}
		});
		contentPane.add(lblSlotCard7);
		//lblSlotCardList.add(lblSlotCard7);
		
		JSeparator separator_1_1_1_1_1 = new JSeparator();
		separator_1_1_1_1_1.setOrientation(SwingConstants.VERTICAL);
		separator_1_1_1_1_1.setForeground(Color.YELLOW);
		separator_1_1_1_1_1.setBackground(Color.YELLOW);
		separator_1_1_1_1_1.setBounds(833, 402, 2, 141);
		contentPane.add(separator_1_1_1_1_1);
		
		lblSlotCard8 = new JLabel(imageIconList.get(7));
		lblSlotCard8.setBounds(845, 411, 92, 113);
		lblSlotCard8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				super.mouseClicked(arg0);
				if (slotStatus[7] == 0) {
					BufferedImage bufferdImage;
					try {
						int index = deckShuffledList.get(7).getIndex();
						System.out.println("pick card is: " + index);
						bufferdImage = ImageIO.read(new File(originalCardChosedList.get(index).getUrl()));
						System.out.println("url is " + originalCardChosedList.get(index).getUrl());
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard8.setIcon(imageIcon);
						
						Card card = deckShuffledList.get(7);
						cardChosedList.add(card);
						System.out.println("client " + accountName + " da chon card \n" + card.getUrl());
						
						slotStatus[7] = 1;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// nếu như là đã pick rồi, thì chuyển về lại hình chưa chuyển
					slotStatus[7] = 0;

					BufferedImage bufferdImage;
					try {
						bufferdImage = ImageIO.read(new File(deckShuffledList.get(7).getUrl()));
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard8.setIcon(imageIcon);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// remove không chọn card này nữa thì xóa khỏi danh sách
					Card card = deckShuffledList.get(7);
					for (int i = 0; i < cardChosedList.size(); i++) {
						if (card.getIndex() == cardChosedList.get(i).getIndex()) {
							cardChosedList.remove(card);
						}
					}
					System.out.println("client " + accountName + " da dung chon card \n" + card.getUrl());
					System.out.println("kich thuoc cua danh sach client muon danh la " + cardChosedList.size());
				}
			}
		});
		contentPane.add(lblSlotCard8);
		//lblSlotCardList.add(lblSlotCard8);
		
		JSeparator separator_1_1_1_1_1_1 = new JSeparator();
		separator_1_1_1_1_1_1.setOrientation(SwingConstants.VERTICAL);
		separator_1_1_1_1_1_1.setForeground(Color.YELLOW);
		separator_1_1_1_1_1_1.setBackground(Color.YELLOW);
		separator_1_1_1_1_1_1.setBounds(945, 402, 2, 141);
		contentPane.add(separator_1_1_1_1_1_1);
		
		lblSlotCard9 = new JLabel(imageIconList.get(8));
		lblSlotCard9.setBounds(957, 411, 92, 113);
		lblSlotCard9.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				super.mouseClicked(arg0);
				if (slotStatus[8] == 0) {
					BufferedImage bufferdImage;
					try {
						int index = deckShuffledList.get(8).getIndex();
						System.out.println("pick card is: " + index);
						bufferdImage = ImageIO.read(new File(originalCardChosedList.get(index).getUrl()));
						System.out.println("url is " + originalCardChosedList.get(index).getUrl());
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard9.setIcon(imageIcon);
						
						Card card = deckShuffledList.get(8);
						cardChosedList.add(card);
						System.out.println("client " + accountName + " da chon card \n" + card.getUrl());
						
						slotStatus[8] = 1;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// nếu như là đã pick rồi, thì chuyển về lại hình chưa chuyển
					slotStatus[8] = 0;

					BufferedImage bufferdImage;
					try {
						bufferdImage = ImageIO.read(new File(deckShuffledList.get(8).getUrl()));
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard9.setIcon(imageIcon);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// remove không chọn card này nữa thì xóa khỏi danh sách
					Card card = deckShuffledList.get(8);
					for (int i = 0; i < cardChosedList.size(); i++) {
						if (card.getIndex() == cardChosedList.get(i).getIndex()) {
							cardChosedList.remove(card);
						}
					}
					System.out.println("client " + accountName + " da dung chon card \n" + card.getUrl());
					System.out.println("kich thuoc cua danh sach client muon danh la " + cardChosedList.size());
				}
			}
		});
		contentPane.add(lblSlotCard9);
		//lblSlotCardList.add(lblSlotCard9);
		
		JSeparator separator_1_1_1_1_1_1_1 = new JSeparator();
		separator_1_1_1_1_1_1_1.setOrientation(SwingConstants.VERTICAL);
		separator_1_1_1_1_1_1_1.setForeground(Color.YELLOW);
		separator_1_1_1_1_1_1_1.setBackground(Color.YELLOW);
		separator_1_1_1_1_1_1_1.setBounds(1059, 402, 2, 141);
		contentPane.add(separator_1_1_1_1_1_1_1);
		
		lblSlotCard10 = new JLabel(imageIconList.get(9));
		lblSlotCard10.setBounds(1071, 411, 92, 113);
		lblSlotCard10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				super.mouseClicked(arg0);
				if (slotStatus[9] == 0) {
					BufferedImage bufferdImage;
					try {
						int index = deckShuffledList.get(9).getIndex();
						System.out.println("pick card is: " + index);
						bufferdImage = ImageIO.read(new File(originalCardChosedList.get(index).getUrl()));
						System.out.println("url is " + originalCardChosedList.get(index).getUrl());
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard10.setIcon(imageIcon);
						
						Card card = deckShuffledList.get(9);
						cardChosedList.add(card);
						System.out.println("client " + accountName + " da chon card \n" + card.getUrl());
						
						slotStatus[9] = 1;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// nếu như là đã pick rồi, thì chuyển về lại hình chưa chuyển
					slotStatus[9] = 0;

					BufferedImage bufferdImage;
					try {
						bufferdImage = ImageIO.read(new File(deckShuffledList.get(9).getUrl()));
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard10.setIcon(imageIcon);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// remove không chọn card này nữa thì xóa khỏi danh sách
					Card card = deckShuffledList.get(9);
					for (int i = 0; i < cardChosedList.size(); i++) {
						if (card.getIndex() == cardChosedList.get(i).getIndex()) {
							cardChosedList.remove(card);
						}
					}
					System.out.println("client " + accountName + " da dung chon card \n" + card.getUrl());
					System.out.println("kich thuoc cua danh sach client muon danh la " + cardChosedList.size());
				}
			}
		});
		contentPane.add(lblSlotCard10);
		//lblSlotCardList.add(lblSlotCard10);
		
		JSeparator separator_1_1_1_1_1_1_1_1 = new JSeparator();
		separator_1_1_1_1_1_1_1_1.setOrientation(SwingConstants.VERTICAL);
		separator_1_1_1_1_1_1_1_1.setForeground(Color.YELLOW);
		separator_1_1_1_1_1_1_1_1.setBackground(Color.YELLOW);
		separator_1_1_1_1_1_1_1_1.setBounds(1172, 402, 2, 141);
		contentPane.add(separator_1_1_1_1_1_1_1_1);
		
		lblSlotCard11 = new JLabel(imageIconList.get(10));
		lblSlotCard11.setBounds(1184, 411, 92, 113);
		lblSlotCard11.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				super.mouseClicked(arg0);
				if (slotStatus[10] == 0) {
					BufferedImage bufferdImage;
					try {
						int index = deckShuffledList.get(10).getIndex();
						System.out.println("pick card is: " + index);
						bufferdImage = ImageIO.read(new File(originalCardChosedList.get(index).getUrl()));
						System.out.println("url is " + originalCardChosedList.get(index).getUrl());
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard11.setIcon(imageIcon);
						
						Card card = deckShuffledList.get(10);
						cardChosedList.add(card);
						System.out.println("client " + accountName + " da chon card \n" + card.getUrl());
						
						slotStatus[10] = 1;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// nếu như là đã pick rồi, thì chuyển về lại hình chưa chuyển
					slotStatus[10] = 0;

					BufferedImage bufferdImage;
					try {
						bufferdImage = ImageIO.read(new File(deckShuffledList.get(10).getUrl()));
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard11.setIcon(imageIcon);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// remove không chọn card này nữa thì xóa khỏi danh sách
					Card card = deckShuffledList.get(10);
					for (int i = 0; i < cardChosedList.size(); i++) {
						if (card.getIndex() == cardChosedList.get(i).getIndex()) {
							cardChosedList.remove(card);
						}
					}
					System.out.println("client " + accountName + " da dung chon card \n" + card.getUrl());
					System.out.println("kich thuoc cua danh sach client muon danh la " + cardChosedList.size());
				}
			}
		});
		contentPane.add(lblSlotCard11);
		//lblSlotCardList.add(lblSlotCard11);
		
		JSeparator separator_1_1_1_1_1_1_1_1_1 = new JSeparator();
		separator_1_1_1_1_1_1_1_1_1.setOrientation(SwingConstants.VERTICAL);
		separator_1_1_1_1_1_1_1_1_1.setForeground(Color.YELLOW);
		separator_1_1_1_1_1_1_1_1_1.setBackground(Color.YELLOW);
		separator_1_1_1_1_1_1_1_1_1.setBounds(1286, 402, 2, 141);
		contentPane.add(separator_1_1_1_1_1_1_1_1_1);
		
		lblSlotCard12 = new JLabel(imageIconList.get(11));
		lblSlotCard12.setBounds(1300, 411, 92, 113);
		lblSlotCard12.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				super.mouseClicked(arg0);
				if (slotStatus[11] == 0) {
					BufferedImage bufferdImage;
					try {
						int index = deckShuffledList.get(11).getIndex();
						System.out.println("pick card is: " + index);
						bufferdImage = ImageIO.read(new File(originalCardChosedList.get(index).getUrl()));
						System.out.println("url is " + originalCardChosedList.get(index).getUrl());
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard12.setIcon(imageIcon);
						
						Card card = deckShuffledList.get(11);
						cardChosedList.add(card);
						System.out.println("client " + accountName + " da chon card \n" + card.getUrl());
						
						slotStatus[11] = 1;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// nếu như là đã pick rồi, thì chuyển về lại hình chưa chuyển
					slotStatus[11] = 0;

					BufferedImage bufferdImage;
					try {
						bufferdImage = ImageIO.read(new File(deckShuffledList.get(11).getUrl()));
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard12.setIcon(imageIcon);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// remove không chọn card này nữa thì xóa khỏi danh sách
					Card card = deckShuffledList.get(11);
					for (int i = 0; i < cardChosedList.size(); i++) {
						if (card.getIndex() == cardChosedList.get(i).getIndex()) {
							cardChosedList.remove(card);
						}
					}
					System.out.println("client " + accountName + " da dung chon card \n" + card.getUrl());
					System.out.println("kich thuoc cua danh sach client muon danh la " + cardChosedList.size());
				}
			}
		});
		contentPane.add(lblSlotCard12);
		//lblSlotCardList.add(lblSlotCard12);
		
		JSeparator separator_1_1_1_1_1_1_1_1_1_1 = new JSeparator();
		separator_1_1_1_1_1_1_1_1_1_1.setOrientation(SwingConstants.VERTICAL);
		separator_1_1_1_1_1_1_1_1_1_1.setForeground(Color.YELLOW);
		separator_1_1_1_1_1_1_1_1_1_1.setBackground(Color.YELLOW);
		separator_1_1_1_1_1_1_1_1_1_1.setBounds(1402, 402, 2, 141);
		contentPane.add(separator_1_1_1_1_1_1_1_1_1_1);
		
		lblSlotCard13 = new JLabel(imageIconList.get(12));
		lblSlotCard13.setBounds(1414, 411, 92, 113);
		lblSlotCard13.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				super.mouseClicked(arg0);
				if (slotStatus[12] == 0) {
					BufferedImage bufferdImage;
					try {
						int index = deckShuffledList.get(12).getIndex();
						System.out.println("pick card is: " + index);
						bufferdImage = ImageIO.read(new File(originalCardChosedList.get(index).getUrl()));
						System.out.println("url is " + originalCardChosedList.get(index).getUrl());
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard13.setIcon(imageIcon);
						
						Card card = deckShuffledList.get(12);
						cardChosedList.add(card);
						System.out.println("client " + accountName + " da chon card \n" + card.getUrl());
						
						slotStatus[12] = 1;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// nếu như là đã pick rồi, thì chuyển về lại hình chưa chuyển
					slotStatus[12] = 0;

					BufferedImage bufferdImage;
					try {
						bufferdImage = ImageIO.read(new File(deckShuffledList.get(12).getUrl()));
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						imageScaled.flush();
						ImageIcon imageIcon = new ImageIcon(imageScaled);
						lblSlotCard13.setIcon(imageIcon);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// remove không chọn card này nữa thì xóa khỏi danh sách
					Card card = deckShuffledList.get(12);
					for (int i = 0; i < cardChosedList.size(); i++) {
						if (card.getIndex() == cardChosedList.get(i).getIndex()) {
							cardChosedList.remove(card);
						}
					}
					System.out.println("client " + accountName + " da dung chon card \n" + card.getUrl());
					System.out.println("kich thuoc cua danh sach client muon danh la " + cardChosedList.size());
				}
			}
		});
		contentPane.add(lblSlotCard13);
		//lblSlotCardList.add(lblSlotCard13);
		
		
		// lưu vào arraylist này là tái tổ chức hiển thị các lá bài còn lại, sau khi đánh bài
		jLabelSlotList.clear();
		
		jLabelSlotList.add(lblSlotCard1);
		jLabelSlotList.add(lblSlotCard2);
		jLabelSlotList.add(lblSlotCard3);
		jLabelSlotList.add(lblSlotCard4);
		jLabelSlotList.add(lblSlotCard5);
		jLabelSlotList.add(lblSlotCard6);
		jLabelSlotList.add(lblSlotCard7);
		jLabelSlotList.add(lblSlotCard8);
		jLabelSlotList.add(lblSlotCard9);
		jLabelSlotList.add(lblSlotCard10);
		jLabelSlotList.add(lblSlotCard11);
		jLabelSlotList.add(lblSlotCard12);
		jLabelSlotList.add(lblSlotCard13);
		
		
		contentPane.repaint();
	}
	
	// CẨN THẬN
	// hàm này để tái tạo lại sấp bài
	
	/**
	 * Create the frame.
	 */
	public ClientGameplay() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1554, 718);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
				
		JLabel lblNewLabel = new JLabel("B\u1EA1n l\u00E0: ");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel.setBounds(279, 597, 69, 30);
		contentPane.add(lblNewLabel);
		
		JLabel lblClientBossAccountName = new JLabel("New label");
		lblClientBossAccountName.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblClientBossAccountName.setBounds(368, 601, 252, 21);
		
		lblClientBossAccountName.setText(UserInfor.accountName);
		contentPane.add(lblClientBossAccountName);
		
		JButton btnXepBai = new JButton("X\u1EBFp b\u00E0i");
		btnXepBai.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnXepBai.setBounds(833, 605, 97, 52);
		btnXepBai.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("thuc hien xep bai");
				//Collections.shuffle(deckShuffledList);
				deckShuffledList = CardFactory.sortDeck(deckShuffledList);
				
				imageIconList.clear();
				
				// lưu trữ đối tượng imageIcon
				for (int i = 0; i < deckShuffledList.size(); i++) {
					
					BufferedImage bufferdImage;
					try {
						bufferdImage = ImageIO.read(new File(deckShuffledList.get(i).getUrl()));
						Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
						ImageIcon imageIcon = new ImageIcon(imageScaled);

						
						imageIconList.add(imageIcon);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				System.out.println("kich thuoc cua imageIconList: " + imageIconList.size());
				contentPane.remove(lblSlotCard1);
				contentPane.remove(lblSlotCard2);
				contentPane.remove(lblSlotCard3);
				contentPane.remove(lblSlotCard4);
				contentPane.remove(lblSlotCard5);
				contentPane.remove(lblSlotCard6);
				contentPane.remove(lblSlotCard7);
				contentPane.remove(lblSlotCard8);
				contentPane.remove(lblSlotCard9);
				contentPane.remove(lblSlotCard10);
				contentPane.remove(lblSlotCard11);
				contentPane.remove(lblSlotCard12);
				contentPane.remove(lblSlotCard13);
				
				createDeckGUI();
				
				//removeLabelFiredCard();
				reconstructSlotStatus();
			}
		});
		contentPane.add(btnXepBai);
		
		JButton btnDanhBai = new JButton("\u0110\u00E1nh b\u00E0i");
		btnDanhBai.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnDanhBai.setBounds(945, 605, 117, 52);
		btnDanhBai.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				// kiểm tra nếu tổng số lá bài mà client này chọn đánh bé hơn tổng số lá bài đang ở trên bàn
//				if (cardChosedList.size() < cardOnTable.size()) {
//					JOptionPane.showMessageDialog(ClientGameplay.this, "những lá bài bạn chọn không hợp lệ");
//				}
				
				// nếu như đây là client đánh đầu tiên hoặc được quyền ưu tiên đánh đè không cần so sánh
				if (isYourTurn == true) {
					// kiểm tra nếu client này chưa chọn lá bài nào mà đã đánh thì báo lỗi
					
					for(int i=0;i<cardChosedList.size();i++) {
						System.out.println("debug cardChosedList: " + cardChosedList.get(i).getPoint());
					}
					
					if (cardOnTable.size() != 0) {
						for(int i=0;i<cardOnTable.size();i++) {
							System.out.println("debug cardOnTable: " + cardOnTable.get(i).getPoint());
						}
					}
					
					if (cardChosedList.size() != 0) {
						
						
						if (isFirstBeginRound == true) {
							
							// được quyền đánh đè lên, bất chấp bài trên bàn như thế nào
							
							// nhưng phải là những lá bài được chọn hợp lệ với việc bất đầu một round
							if (CardFactory.isRightForBeginNewRound(cardChosedList)) {
								System.out.println("đã đi qua nơi này");
								isYourTurn = false;
								lblYourTurn.setText("Chưa đến lượt đánh bài của bạn");
								
								lblLuotBaiCua.setText(accountName);
								//isFirstBeginRound = false;
								
								// đúng là x và y là 1 thì "tư cách" FirstBeginRound vẫn thuộc về client này khi nó đánh bài.
								// tuy nhiên 
								AnnouncementFirstBeginRound announcementFirstBeginRound = new AnnouncementFirstBeginRound(accountName, 1);
								clientThread.doWrite(announcementFirstBeginRound);
								
								System.out.println(accountName + " danh bai ");
								System.out.println(accountName + " turn now is: " + isYourTurn);
								System.out.println(accountName + " firstBeginRound is: "+ isFirstBeginRound);
								
								firedCardList.clear();
								firedCardList = (ArrayList<Card>) cardChosedList.clone();
										
								// sao chép nhớ các lá trong cardChosedList sang cardOnTable
								cardOnTable = CardFactory.sortDeck(cardChosedList);
								//tempCardOnTable = cardOnTable;
								
								imageIconOnTableList.clear();
								for (int i = 0; i < jLabelCardOnTable.size(); i++) {
									contentPane.remove(jLabelCardOnTable.get(i));
								}
								
								// hiển thị lên table
								displayCardOnTable();
								
								// lấy ra index của các card trong cardOnTable
								ArrayList<Integer> dispatchDeck = new ArrayList<Integer>();
								for (int i = 0; i < cardOnTable.size(); i++) {
									dispatchDeck.add(cardOnTable.get(i).getIndex());
								}
								
								// gửi đi thông sô các lá bài
								RequestEndTurn requestEndTurn = new RequestEndTurn(accountName, dispatchDeck);
								clientThread.doWrite(requestEndTurn);
								
								// xóa cache cardOnTable và imageIconOnTableList
								// lỗi cũng ở đây, như đã giải thích ở dưới.
								//cardOnTable.clear();
						
								
								// không xóa cardChosedList vì để tháo các card đã đánh
								cardChosedList.clear();
								
								
								System.out.println("kich thuoc: " + cardChosedList.size());
								
								for (int i = 0; i < jLabelSlotList.size(); i++) {
									if (slotStatus[i] == 1) {
										jLabelSlotList.get(i).setVisible(false);
									}
								}
							} else {
								JOptionPane.showMessageDialog(ClientGameplay.this, "những lá bài bạn chọn không hợp lệ");
							}

							
						} else {
							// phải đánh các lá bài đè lên hợp lệ
							int kq = CardFactory.isComboCardBigger(cardChosedList, cardOnTable);
							System.out.println("kq " + kq);
							if (kq == -1) {
								JOptionPane.showMessageDialog(ClientGameplay.this, "những lá bài bạn chọn không hợp lệ");
								System.out.println(cardChosedList);
								System.out.println(cardOnTable);
//								if (cardChosedList.get(0).equals(cardOnTable.get(0))) {
//									cardOnTable.clear();
//									cardOnTable = tempCardOnTable;
//									
//									
//								} else {
//									JOptionPane.showMessageDialog(ClientGameplay.this, "những lá bài bạn chọn không hợp lệ");
//								}
								
							} else if (kq == 2) {
								JOptionPane.showMessageDialog(ClientGameplay.this, "những lá bài bạn chọn không lớn hơn bài trên bàn");
								System.out.println(cardChosedList);
								System.out.println(cardOnTable);
							} else if (kq == 1) {
								isYourTurn = false;
								isFirstBeginRound = true;
								
								lblYourTurn.setText("Chưa đến lượt đánh bài của bạn");
								
								lblLuotBaiCua.setText(accountName);
								
								// thông báo server rằng client này lúc này đang nắm quyền isFirstBeginRound
								AnnouncementFirstBeginRound announcementFirstBeginRound = new AnnouncementFirstBeginRound(accountName);
								clientThread.doWrite(announcementFirstBeginRound);
								
								System.out.println(accountName + " danh bai ");
								System.out.println(accountName + " turn now is: " + isYourTurn);
								System.out.println(accountName + " firstBeginRound is: "+ isFirstBeginRound);
								System.out.println("cardChosedList size is " + cardChosedList.size());
								
								firedCardList.clear();
								firedCardList = (ArrayList<Card>) cardChosedList.clone();
								
								// sao chép nhớ các lá trong cardChosedList sang cardOnTable
								//ArrayList<Card> tempCardChosedList = CardFactory.sortDeck(cardChosedList);
								cardOnTable = CardFactory.sortDeck(cardChosedList);
								// tempCardOnTable = cardOnTable;
								//cardChosedList.clear();
								
								imageIconOnTableList.clear();
								for (int i = 0; i < jLabelCardOnTable.size(); i++) {
									contentPane.remove(jLabelCardOnTable.get(i));
								}
								
								// hiển thị lên table
								displayCardOnTable();
								
								// lấy ra index của các card trong cardOnTable
								ArrayList<Integer> dispatchDeck = new ArrayList<Integer>();
								for (int i = 0; i < cardChosedList.size(); i++) {
									dispatchDeck.add(cardChosedList.get(i).getIndex());
								}
								
								System.out.println("dispatchDeck co" + dispatchDeck.size());
								cardChosedList.clear();
								
								// gửi đi thông sô các lá bài
								RequestEndTurn requestEndTurn = new RequestEndTurn(accountName, dispatchDeck);
								clientThread.doWrite(requestEndTurn);
								
								// xóa cache cardOnTable và imageIconOnTableList
								// sai ở ngay đây, khi bạn xóa, thì bàn đã, rỗng, nên bài đánh lên
								// không biết phải so bài với bài nào ??????
								//cardOnTable.clear();
								// imageIconOnTableList.clear();
								
								
								
								// không xóa cardChosedList vì để tháo các card đã đánh
								//displayRestDeckOfClient();
								
								// không xóa cardChosedList vì để tháo các card đã đánh
								
								cardChosedList.clear();
								System.out.println("kich thuoc : " + cardChosedList.size());
								
								
								
								for (int i = 0; i < jLabelSlotList.size(); i++) {
									if (slotStatus[i] == 1) {
										jLabelSlotList.get(i).setVisible(false);
									}
								}
							}
	
						}
						
					} else {
						JOptionPane.showMessageDialog(ClientGameplay.this, "bạn chưa chọn lá nào");
					}
					
				} else {
					// không phải turn của client này thì client này không được đánh
					JOptionPane.showMessageDialog(ClientGameplay.this, "chưa tới lượt của bạn, bạn không thể đánh bài lúc này");
				}
				
			}
		});
		contentPane.add(btnDanhBai);
		
		JButton btnBoLuot = new JButton("B\u1ECF l\u01B0\u1EE3t");
		btnBoLuot.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnBoLuot.setBounds(719, 605, 97, 52);
		btnBoLuot.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//displayCardOnTable();
				// kiểm tra xem lúc này đã là lượt của client này chưa
				if (isYourTurn == false ) {
						JOptionPane.showMessageDialog(ClientGameplay.this, "Lượt của bạn chưa đến");

				} else {
					
					if (isFirstBeginRound == true) {
						// nếu như giá trị isFristBeginRound = true 
						// set giá trị isYourTurn = false
						isYourTurn = false;
						
						lblYourTurn.setText("Chưa đến lượt đánh bài của bạn");
						
						// set giá trị isFristBeginRound = false
						isFirstBeginRound = false;	
						
						System.out.println(accountName + " bo luot ");
						System.out.println(accountName + " turn now is: " + isYourTurn);
						System.out.println(accountName + " firstBeginRound is: "+ isFirstBeginRound);
						
						RequestEndTurn requestEndTurn = new RequestEndTurn(accountName);
						RequestTheFirstBeginRound requestTheFirstBeginRound = new RequestTheFirstBeginRound(accountName);
						//AnnouncementFirstBeginRound announcementFirstBeginRound = new AnnouncementFirstBeginRound(accountName, 1);
						
						DataPacket dataPacket = (DataPacket) requestEndTurn;
						DataPacket dataPacket2 = (DataPacket) requestTheFirstBeginRound;
						
						clientThread.doWrite(dataPacket);
						clientThread.doWrite(dataPacket2);
						
					} else {
						// nếu không phải là fristBeginRound thì vẫn set isyourturn = false
						isYourTurn = false;
						
						lblYourTurn.setText("Chưa đến lượt đánh bài của bạn");
						
						System.out.println(accountName + " bo luot ");
						System.out.println(accountName + " turn now is: " + isYourTurn);
						System.out.println(accountName + " firstBeginRound is: "+ isFirstBeginRound);
						
						// tạo ra gói tin đề nghị server bỏ qua lượt của client này
						RequestEndTurn requestEndTurn = new RequestEndTurn(accountName);
						//RequestTheFirstBeginRound requestTheFirstBeginRound = new RequestTheFirstBeginRound(accountName);
						
						DataPacket dataPacket = (DataPacket) requestEndTurn;
						//DataPacket dataPacket2 = (DataPacket) requestTheFirstBeginRound;
						
						// gửi gói tin đi
						clientThread.doWrite(dataPacket);
						//clientThread.doWrite(dataPacket2);
					}
					
//					isYourTurn = false;
//					System.out.println("my turn now is: " + isYourTurn);
//					
//					// tạo ra gói tin đề nghị server bỏ qua lượt của client này
//					RequestEndTurn requestEndTurn = new RequestEndTurn(accountName);
//					
//					DataPacket dataPacket = (DataPacket) requestEndTurn;
//					
//					// gửi gói tin đi
//					clientThread.doWrite(dataPacket);
				}
			}
		});
		contentPane.add(btnBoLuot);
		
		JButton btnNewButton = new JButton("fix bug");
		btnNewButton.setBounds(29, 605, 85, 21);
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//cardChosedList.clear();
//				System.out.println("cardChosedList: " + cardChosedList.size());
//				System.out.println("cardOnTable " + cardOnTable.size());
				for (int i = 0; i < cardChosedList.size(); i++) {
					System.out.println("cardChosedList: " + cardChosedList.size());
				}
				for (int j = 0; j < cardOnTable.size(); j++) {
					System.out.println("cardOnTable " + cardOnTable.size());
				}
			}
		});
		contentPane.add(btnNewButton);
		
		lblYourTurn = new JLabel("New label");
		lblYourTurn.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblYourTurn.setBounds(279, 637, 341, 20);
		lblYourTurn.setText("Chưa đến lượt đánh bài của bạn");
		contentPane.add(lblYourTurn);
		
		JLabel lblNewLabel_1 = new JLabel("Lượt bài này được đánh lên bởi:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel_1.setBounds(265, 355, 242, 29);
		contentPane.add(lblNewLabel_1);
		
		lblLuotBaiCua = new JLabel();
		lblLuotBaiCua.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblLuotBaiCua.setBounds(517, 359, 115, 21);
		contentPane.add(lblLuotBaiCua);
	
		
		lblAnotherClientSlotOne = new JLabel("New label");
		lblAnotherClientSlotOne.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAnotherClientSlotOne.setBounds(21, 191, 120, 30);
		contentPane.add(lblAnotherClientSlotOne);
		
		lblAnotherClientSlotTwo = new JLabel("New label");
		lblAnotherClientSlotTwo.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAnotherClientSlotTwo.setBounds(551, 31, 117, 30);
		contentPane.add(lblAnotherClientSlotTwo);
		
		lblAnotherClientSlotThree = new JLabel("New label");
		lblAnotherClientSlotThree.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblAnotherClientSlotThree.setBounds(1402, 191, 128, 30);
		contentPane.add(lblAnotherClientSlotThree);
		
		numberCardSlotOne = new JLabel("13");
		numberCardSlotOne.setFont(new Font("Tahoma", Font.PLAIN, 14));
		numberCardSlotOne.setBounds(112, 332, 29, 21);
		contentPane.add(numberCardSlotOne);
		
		numberCardSlotTwo = new JLabel("13");
		numberCardSlotTwo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		numberCardSlotTwo.setBounds(630, 183, 29, 21);
		contentPane.add(numberCardSlotTwo);
		
		numberCardSlotThree = new JLabel("13");
		numberCardSlotThree.setFont(new Font("Tahoma", Font.PLAIN, 14));
		numberCardSlotThree.setBounds(1480, 342, 29, 21);
		contentPane.add(numberCardSlotThree);
		
		// tạo giao diện 3 hidden card
		CardFactory cardFactoryNew = new CardFactory();
		cardFactoryNew.createHideCard();
		hiddenCard = cardFactoryNew.getHideCard();
		
		lblHiddenCardSlotOne = new JLabel();
		lblHiddenCardSlotOne.setBounds(21, 220, 92, 113);
		BufferedImage bufferdImageHiddenCardSlotOne;
		try {
			bufferdImageHiddenCardSlotOne = ImageIO.read(new File(hiddenCard.getUrl()));
			Image imageScaled = bufferdImageHiddenCardSlotOne.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
			imageScaled.flush();
			ImageIcon imageIcon = new ImageIcon(imageScaled);
			lblHiddenCardSlotOne.setIcon(imageIcon);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentPane.add(lblHiddenCardSlotOne);
		
		lblHiddenCardSlotTwo = new JLabel();
		lblHiddenCardSlotTwo.setBounds(551, 58, 92, 113);
		BufferedImage bufferdImageHiddenCardSlotTwo;
		try {
			bufferdImageHiddenCardSlotTwo = ImageIO.read(new File(hiddenCard.getUrl()));
			Image imageScaled = bufferdImageHiddenCardSlotTwo.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
			imageScaled.flush();
			ImageIcon imageIcon = new ImageIcon(imageScaled);
			lblHiddenCardSlotTwo.setIcon(imageIcon);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentPane.add(lblHiddenCardSlotTwo);
		
		lblHiddenCardSlotThree = new JLabel("New label");
		lblHiddenCardSlotThree.setBounds(1402, 220, 92, 113);
		BufferedImage bufferdImageHiddenCardSlotThree;
		try {
			bufferdImageHiddenCardSlotThree = ImageIO.read(new File(hiddenCard.getUrl()));
			Image imageScaled = bufferdImageHiddenCardSlotThree.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
			imageScaled.flush();
			ImageIcon imageIcon = new ImageIcon(imageScaled);
			lblHiddenCardSlotThree.setIcon(imageIcon);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentPane.add(lblHiddenCardSlotThree);
		
		JLabel lblNewLabel_2 = new JLabel("Số lá còn lại:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(21, 332, 85, 21);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("Số lá còn lại:");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2_1.setBounds(547, 183, 85, 21);
		contentPane.add(lblNewLabel_2_1);
		
		JLabel lblNewLabel_2_2 = new JLabel("Số lá còn lại:");
		lblNewLabel_2_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2_2.setBounds(1398, 342, 85, 21);
		contentPane.add(lblNewLabel_2_2);
		
		textFieldInputMessenger = new JTextField();
		textFieldInputMessenger.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldInputMessenger.setBounds(833, 123, 575, 30);
		textFieldInputMessenger.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				// TODO Auto-generated method stub
				if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!textFieldInputMessenger.getText().isEmpty()) {
						RequestSendMessenger requestSendMessenger = new RequestSendMessenger(accountName, textFieldInputMessenger.getText());
						DataPacket dataPacket = (DataPacket) requestSendMessenger;
						
						clientThread.doWrite(dataPacket);
						
						String string = "You: " + textFieldInputMessenger.getText().toString() + " \n";
						//System.out.println(string);
						logMessenger.append(string);
						//System.out.println(logMessenger);
						textPaneDisplayChat.setText(logMessenger.toString());
						textFieldInputMessenger.setText("");
					}
				}
			}
		});
		contentPane.add(textFieldInputMessenger);
		textFieldInputMessenger.setColumns(10);
		
		JButton btnSendChat = new JButton("Send Chat");
		btnSendChat.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnSendChat.setBounds(1410, 123, 120, 30);
		btnSendChat.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (!textFieldInputMessenger.getText().isEmpty()) {
					RequestSendMessenger requestSendMessenger = new RequestSendMessenger(accountName, textFieldInputMessenger.getText());
					DataPacket dataPacket = (DataPacket) requestSendMessenger;
					
					clientThread.doWrite(dataPacket);
					
					String string = "You: " + textFieldInputMessenger.getText().toString() + " \n";
					//System.out.println(string);
					logMessenger.append(string);
					//System.out.println(logMessenger);
					textPaneDisplayChat.setText(logMessenger.toString());
					textFieldInputMessenger.setText("");
				}
			}
		});
		contentPane.add(btnSendChat);
		
		
		textPaneDisplayChat = new JTextPane();
		textPaneDisplayChat.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textPaneDisplayChat.setBounds(833, 10, 697, 103);
		textPaneDisplayChat.setFocusable(false);
		textPaneDisplayChat.setEditable(false);
		contentPane.add(textPaneDisplayChat);
		
		winnerResultSlotOne = new JLabel("");
		winnerResultSlotOne.setFont(new Font("Tahoma", Font.PLAIN, 17));
		winnerResultSlotOne.setBounds(139, 191, 120, 30);
		contentPane.add(winnerResultSlotOne);
		
		winnerResultSlotTwo = new JLabel("");
		winnerResultSlotTwo.setFont(new Font("Tahoma", Font.PLAIN, 17));
		winnerResultSlotTwo.setBounds(672, 31, 120, 30);
		contentPane.add(winnerResultSlotTwo);
		
		winnerResultSlotThree = new JLabel("");
		winnerResultSlotThree.setFont(new Font("Tahoma", Font.PLAIN, 17));
		winnerResultSlotThree.setBounds(1245, 191, 120, 30);
		contentPane.add(winnerResultSlotThree);
		
		scrollPane = new JScrollPane(textPaneDisplayChat);
		scrollPane.setBounds(833, 10, 697, 103);
		contentPane.add(scrollPane);
		
		JButton btnSendFile = new JButton();
		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileExplore fileExplore = new FileExplore();
				fileExplore.setClientGameplay(ClientGameplay.this);
				fileExplore.setVisible(true);
			}
		});
		btnSendFile.setBounds(794, 123, 29, 30);
		btnSendFile.setIcon(createFormatImageIcon(resourcePath + "sendfileicon.png", btnSendFile.getWidth(), btnSendFile.getHeight()));
		contentPane.add(btnSendFile);
		
		JLabel lblNewLabel_3 = new JLabel("Phòng game số:");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lblNewLabel_3.setBounds(10, 10, 165, 30);
		contentPane.add(lblNewLabel_3);
		
		lblRoomNumber = new JLabel("");
		lblRoomNumber.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lblRoomNumber.setBounds(173, 10, 55, 30);
		contentPane.add(lblRoomNumber);
		
		
		try {
			BufferedImage bufferdImageMic = ImageIO.read(new File("F://ecilpse-workspace//PlayingCardGame//Resource//turnmicon.png"));
			Image imageScaled = bufferdImageMic.getScaledInstance(32, 29, Image.SCALE_SMOOTH);
			imageScaled.flush();
			ImageIcon imageIcon = new ImageIcon(imageScaled);
		} catch (Exception ex) {
		    System.out.println(ex);
		}
		
		// create list original 52 cards
		CardFactory cardFactory = new CardFactory();
		cardFactory.createOriginalCard();
		originalCardList = cardFactory.getOriginalCardList();
		System.out.println(originalCardList);
		
		// create list original 52 cards được chosed (chọn)
		cardFactory.createPickedCard();
		originalCardChosedList = cardFactory.getOriginalPickedCardList();
		
		// set all slot status is 0, it mean all is no pick yet
		for (int i = 0; i < 13; i++) {
			slotStatus[i] = 0;
		}
		
		try {
			// hãy kết nối kiểm tra trạng thái port room ở database tại đây.
			
			socket = new Socket("localhost", 9999);
			//System.out.println("go here");
			clientThread = new ClientThread(ClientGameplay.this);
			clientThread.start();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	JLabel lblHiddenCardSlotOne;
	JLabel lblHiddenCardSlotTwo;
	JLabel lblHiddenCardSlotThree;
	
	JLabel lblAnotherClientSlotOne;
	JLabel lblAnotherClientSlotTwo;
	JLabel lblAnotherClientSlotThree;
	
	JLabel lblLuotBaiCua;
	
	JLabel numberCardSlotOne;
	JLabel numberCardSlotTwo;
	JLabel numberCardSlotThree;
	
	int numCardClientOne   = 13;
	int numCardClientTwo   = 13;
	int numCardClientThree = 13;
	int numCardClientFour  = 13;
	
	JLabel winnerResultSlotOne;
	JLabel winnerResultSlotTwo;
	JLabel winnerResultSlotThree;
	
	public JTextPane textPaneDisplayChat;
	public JTextField textFieldInputMessenger;
	public StringBuilder logMessenger = new StringBuilder();
	
	// hàm này để kiểm tra xem các lá bài client chọn để xuất có hợp lệ không
	public boolean isRightDispatchDeck() {
		if (cardChosedList.size() == 13) {
			for (int i = 0; i < cardChosedList.size() - 1; i++) {
				//cardChosedList
			}
		}
		
		if (cardChosedList.size() == 12) {
			
		}
		return false;
	}
	
	public void removeLabelFiredCard() {
		jLabelSlotList.clear();
		
		for (int i = 0; i < deckShuffledList.size(); i++) {
			for (int j = 0; j < firedCardList.size(); j++) {
				if (deckShuffledList.get(i).equals(firedCardList.get(j))) {
					int index = i + 1;
					System.out.println("lá bài mà client này đã dánh đi: " + index);
					switch (index) {
					case 1:
						lblSlotCard1.setVisible(false);
						break;
					case 2:
						lblSlotCard2.setVisible(false);
						break;
					case 3:
						lblSlotCard3.setVisible(false);
						break;
					case 4:
						lblSlotCard4.setVisible(false);
						break;
					case 5:
						lblSlotCard5.setVisible(false);
						break;
					case 6:
						lblSlotCard6.setVisible(false);
						break;
					case 7:
						lblSlotCard7.setVisible(false);
						break;
					case 8:
						lblSlotCard8.setVisible(false);
						break;
					case 9:
						lblSlotCard9.setVisible(false);
						break;
					case 10:
						lblSlotCard10.setVisible(false);
						break;
					case 11:
						lblSlotCard11.setVisible(false);
						break;
					case 12:
						lblSlotCard12.setVisible(false);
						break;
					case 13:
						lblSlotCard13.setVisible(false);
						break;
					}
				}
			}
		}
		
		firedCardList.clear();
//		for (int i = 0; i < slotStatus.length; i++) {
//			slotStatus[i] = 0;
//		}
		reconstructSlotStatus();
	}
	
	// hàm này tái bố trí lại mảng slotSatus theo đúng với với các lá bài sau khi xếp bài
	public void reconstructSlotStatus() {
		for (int i = 0; i < deckShuffledList.size(); i++) {
			// chú ý lúc này deckShuffledList đã được sort
			
			for (int j = 0; j < firedCardList.size(); j++) {
				if (deckShuffledList.get(i).equals(firedCardList.get(j))) {
					// nếu bằng thì hidden lá bài đó
					slotStatus[i] = 1;
				} else {
					slotStatus[i] = 0;
				}
			}
		}
	}
	
	// hàm display các lá bài trên bàn, hàm được gọi khi nhấn button đánh bài
	ArrayList<JLabel> jLabelCardOnTable = new ArrayList<JLabel>();
	
	public void displayCardOnTable() {
		
		// nhớ các lá bài từ cardChoseList
		//cardOnTable = CardFactory.sortDeck(cardChosedList);
		
		System.out.println("kich thuoc cua cardOnTable: " + cardOnTable.size());
		// ? ai clear jLabelCardOnTable ??????
		// xóa các jLabel Card trên bàn hiện có trong contentPanel
		if (jLabelCardOnTable.size() != 0) {
			System.out.println("dang thuc hien xoa cache jLabelCardOnTable");
			for (int i = 0; i < jLabelCardOnTable.size(); i++) {
				contentPane.remove(jLabelCardOnTable.get(i));
			}
			jLabelCardOnTable.clear();
		}
		
		// lưu trữ đối tượng imageIcon mới, dựa trên index lấy từ cardOnTable, hoặc clientThread gọi
		for (int i = 0; i < cardOnTable.size(); i++) {
			
			BufferedImage bufferdImage;
			try {
				bufferdImage = ImageIO.read(new File(cardOnTable.get(i).getUrl()));
				Image imageScaled = bufferdImage.getScaledInstance(61, 90, Image.SCALE_SMOOTH);
				ImageIcon imageIcon = new ImageIcon(imageScaled);

				
				imageIconOnTableList.add(imageIcon);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// định nghĩa các đối tượng label của card on table
		int x = 200;
		int y = 222;
		int w = 61;
		int h = 90;
		for (int i = 0; i < cardOnTable.size(); i++) {
			// khoảng các giữa tọa độ x giữa các card
			if (i != 0) {
				x += 100;
			}
			JLabel jLabelCard = new JLabel();
			jLabelCard.setBounds(x, y, w, h);
			jLabelCard.setIcon(imageIconOnTableList.get(i));
			jLabelCardOnTable.add(jLabelCard);
			contentPane.add(jLabelCard);
		}
		repaint();
		//repaint();
		// test
		
		// lưu ý phải xóa hẳn các lá bài mà người dùng đã đánh, chỉ để lại cardOnTable để so sánh tính login 
		
		//drawRestOfClientDeck(cardChosedList);
		
	}
	
	// hàm vẽ lại sấp bài của client sau khi client đánh bài (hàm này chỉ gọi khi bài đánh lên hợp lệ)
//	public void drawRestOfClientDeck(ArrayList<Card> ListOfCardGone) {
//		
//		
//		// xóa đi các lá bài đã đánh khỏi sấp bài của client
//		for (int i = 0; i < ListOfCardGone.size(); i++) {
//			int index = ListOfCardGone.get(i).getIndex();
//			for (int j = 0; j < deckShuffledList.size(); j++) {
//				if (deckShuffledList.get(j).getIndex() == index) {
//					deckShuffledList.remove(j);
//				}
//			}
//		}
//		int howManyRestCard = deckShuffledList.size();
//		
//		imageIconList.clear();
//		
//		// lưu trữ đối tượng imageIcon
//		for (int i = 0; i < deckShuffledList.size(); i++) {
//			
//			BufferedImage bufferdImage;
//			try {
//				bufferdImage = ImageIO.read(new File(deckShuffledList.get(i).getUrl()));
//				Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
//				ImageIcon imageIcon = new ImageIcon(imageScaled);
//
//				
//				imageIconList.add(imageIcon);
//
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		if (howManyRestCard >= 1) {
//			contentPane.remove(lblSlotCard1);
//		}
//		if (howManyRestCard >= 2) {
//			contentPane.remove(lblSlotCard2);
//		}
//		if (howManyRestCard >= 3) {
//			contentPane.remove(lblSlotCard3);
//		}
//		if (howManyRestCard >= 4) {
//			contentPane.remove(lblSlotCard4);
//		}
//		if (howManyRestCard >= 5) {
//			contentPane.remove(lblSlotCard5);
//		}
//		if (howManyRestCard >= 6) {
//			contentPane.remove(lblSlotCard6);
//		}
//		if (howManyRestCard >= 7) {
//			contentPane.remove(lblSlotCard7);
//		}
//		if (howManyRestCard >= 8) {
//			contentPane.remove(lblSlotCard8);
//		}
//		if (howManyRestCard >= 9) {
//			contentPane.remove(lblSlotCard9);
//		}
//		if (howManyRestCard >= 10) {
//			contentPane.remove(lblSlotCard10);
//		}
//		if (howManyRestCard >= 11) {
//			contentPane.remove(lblSlotCard11);
//		}
//		if (howManyRestCard >= 12) {
//			contentPane.remove(lblSlotCard12);
//		}
//		if (howManyRestCard == 13) {
//			contentPane.remove(lblSlotCard13);
//		}
//		
//		//createDeckGUI(howManyRestCard);
//	}
	
	
	// arrayList lưu vị trí lblSlotCard của các card đã đánh
	ArrayList<Integer> positionLblSlotCardToRemove = new ArrayList<Integer>();
	private JScrollPane scrollPane;
	public JLabel lblRoomNumber;
	
	public void displayRestDeckOfClient() {
		
		// hay là chưa nhỉ ?
		contentPane.remove(lblSlotCard1);
		contentPane.remove(lblSlotCard2);
		contentPane.remove(lblSlotCard3);
		contentPane.remove(lblSlotCard4);
		contentPane.remove(lblSlotCard5);
		contentPane.remove(lblSlotCard6);
		contentPane.remove(lblSlotCard7);
		contentPane.remove(lblSlotCard8);
		contentPane.remove(lblSlotCard9);
		contentPane.remove(lblSlotCard10);
		contentPane.remove(lblSlotCard11);
		contentPane.remove(lblSlotCard12);
		contentPane.remove(lblSlotCard13);
		
		if (isYourTurn == true) {
			lblYourTurn.setText("Đây là lượt của bạn");
		} else {
			lblYourTurn.setText("Chưa tới lượt của bạn");
		}
		
		
		// trước khi gọi hàm này thì đã xóa cache của lblSlot rồi
		int howManyCardLeft = deckShuffledList.size() - cardChosedList.size();
		
		// xóa đi các lá bài đã dánh khỏi deckShuffed của client
		for (int i = 0; i < cardChosedList.size(); i++) {
			// lấy element để tiến hành remove
			Card card = cardChosedList.get(i);
			for (int j = 0; j < deckShuffledList.size(); j++) {
				if (deckShuffledList.get(j).equals(card)) {
					int index = j;
					deckShuffledList.remove(index);
				}
			}
		}
		
		// xóa bớt imageIconList
		imageIconList.clear();
		
		// tạo dữ liệu cho imageIconList
		// lưu trữ đối tượng imageIcon
		for (int i = 0; i < deckShuffledList.size(); i++) {
			
			BufferedImage bufferdImage;
			try {
				bufferdImage = ImageIO.read(new File(deckShuffledList.get(i).getUrl()));
				Image imageScaled = bufferdImage.getScaledInstance(92, 113, Image.SCALE_SMOOTH);
				ImageIcon imageIcon = new ImageIcon(imageScaled);

				
				imageIconList.add(imageIcon);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		// lấy ra các vị trí lblslot được đánh dấu là có bài đã được đánh
		// có một điều đặt biệt là thứ tự của card lấy từ cardChoseList trong deckShuffedList 
		// cũng trùng với thứ tự của card đó trong lblslotCard
		
		for (int i = 0; i < cardChosedList.size(); i++) {
			Card card = cardChosedList.get(i);
			
			// tính toán vị trí của card này trong deckShuffedList
			for (int j = 0; j < deckShuffledList.size(); j++) {
				if (deckShuffledList.get(j) == card) {
					positionLblSlotCardToRemove.add(j);
				}
			}
		}
		
	}
	
	public void fixThisBug() {
		cardChosedList.clear();
	}
	
	// Image Engine ----------------------------------
	public ImageIcon createFormatImageIcon(String url, int width, int height) {
		BufferedImage bufferdImage;
		try {
			bufferdImage = ImageIO.read(new File(url));
			Image imageScaled = bufferdImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			imageScaled.flush();
			return new ImageIcon(imageScaled);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
