package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Connection.SQLHelper;
import Connection.UserModel;
import DataPacket.DataPacket;
import DataPacket.RequestSendMessenger;
import Storage.UserInfor;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPasswordField;

public class ClientLoginFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldAccountName;
	private JPasswordField passwordField;
	private JLabel lblStatusConnection;
	SQLHelper sqlHelper = new SQLHelper();
	
//	Socket socket;
//	ObjectInputStream objectInputStream;
//	ObjectOutputStream objectOutputStream;
	//ClientThread clientThread;

	/**
	 * Launch the application.
	 */
	public int doAuth(UserModel userModel) {
		SQLHelper sqlHelper = new SQLHelper();
		if(!sqlHelper.validateUser(userModel)) {
			return -1;
		}
		return 1;
	}
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientLoginFrame frame = new ClientLoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientLoginFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1080, 417);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Ch\u01B0\u01A1ng tr\u00ECnh ch\u01A1i b\u00E0i ti\u1EBFn l\u00EAn online");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lblNewLabel.setBounds(10, 10, 592, 36);
		contentPane.add(lblNewLabel);
		
		JLabel lblHunhCaoBo = new JLabel("Hu\u1EF3nh Cao B\u1EA3o Long - 18it3");
		lblHunhCaoBo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lblHunhCaoBo.setBounds(10, 52, 592, 36);
		contentPane.add(lblHunhCaoBo);
		
		textFieldAccountName = new JTextField();
		textFieldAccountName.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textFieldAccountName.setBounds(755, 75, 289, 36);
		textFieldAccountName.addKeyListener(new KeyListener() {
			
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
					if (!textFieldAccountName.getText().isEmpty() || !passwordField.getText().isEmpty()) {
						
						UserModel userModel = new UserModel(textFieldAccountName.getText().trim(), passwordField.getText().trim());
						if (doAuth(userModel) == -1) {
							JOptionPane.showMessageDialog(ClientLoginFrame.this, "Đăng nhập thấp bại.");
							return;
						}
						
		
						UserInfor.accountName = textFieldAccountName.getText().trim();
						
						ClientGameplay clientGameplay = new ClientGameplay();
						//clientGameplay.setAccountName(textFieldAccountName.getText().trim());
						clientGameplay.setVisible(true);
						
						//ClientLoginFrame.this.setVisible(false);
						dispose();
					} else {
						JOptionPane.showMessageDialog(ClientLoginFrame.this, "Các trường thông tin chưa hợp lệ.");
					}
				}
			}
		});
		contentPane.add(textFieldAccountName);
		textFieldAccountName.setColumns(10);
		
		JButton btnLogin = new JButton("\u0110\u0103ng Nh\u1EADp");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnLogin.setBounds(793, 185, 140, 51);
		btnLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (textFieldAccountName.getText().isEmpty() || passwordField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(ClientLoginFrame.this, "Các trường thông tin chưa hợp lệ.");
					return;
				}
				UserModel userModel = new UserModel(textFieldAccountName.getText().trim(), passwordField.getText().trim());
				if (doAuth(userModel) == -1) {
					JOptionPane.showMessageDialog(ClientLoginFrame.this, "Đăng nhập thấp bại.");
					return;
				}
				
				UserInfor.accountName = textFieldAccountName.getText().trim();
				
				// start audio thread in clientGamplay too
				
				ClientGameplay clientGameplay = new ClientGameplay();
				//clientGameplay.setAccountName(textFieldAccountName.getText().trim());
				clientGameplay.setVisible(true);
				
				//ClientLoginFrame.this.setVisible(false);
				dispose();
				
				
			}
			
		});
		contentPane.add(btnLogin);
		
		JLabel lblNewLabel_1 = new JLabel("\u00A9 Copyright 2020 - 2021.");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel_1.setBounds(635, 305, 199, 21);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("All rights reserved.");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel_1_1.setBounds(635, 328, 187, 21);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_2 = new JLabel("Version 1.0");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel_2.setBounds(635, 355, 88, 15);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("Connection status:");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel_2_1.setBounds(856, 355, 151, 15);
		contentPane.add(lblNewLabel_2_1);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lblLogin.setBounds(818, 24, 122, 36);
		contentPane.add(lblLogin);
		
		lblStatusConnection = new JLabel("");
		lblStatusConnection.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 17));
		lblStatusConnection.setBounds(1001, 355, 55, 15);
		if (sqlHelper.checkingStatusOnline() == true) {
			lblStatusConnection.setForeground(Color.green);
			lblStatusConnection.setText("Online");
		} else {
			lblStatusConnection.setForeground(Color.red);
			lblStatusConnection.setText("Offline");
		}
		contentPane.add(lblStatusConnection);
		
		JLabel lblNewLabel_1_2 = new JLabel("Don't have account ?");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1_2.setBounds(755, 160, 122, 15);
		contentPane.add(lblNewLabel_1_2);
		
		JLabel lblSign = new JLabel("Sign in");
		lblSign.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSign.setBounds(875, 160, 45, 15);
		lblSign.setForeground(Color.blue);
		lblSign.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				ClientSignFrame clientSignFrame = new ClientSignFrame();
				clientSignFrame.setVisible(true);
			}
		});
		contentPane.add(lblSign);
		
		JLabel lblBackGround = new JLabel();
		BufferedImage bufferdImageBackground;
		try {
			bufferdImageBackground = ImageIO.read(new File("F:\\ecilpse-workspace\\PlayingCardGame\\Resource\\Happy.jpg"));
			Image imageScaled = bufferdImageBackground.getScaledInstance(625, 417, Image.SCALE_SMOOTH);
			imageScaled.flush();
			ImageIcon imageIcon = new ImageIcon(imageScaled);
			lblBackGround.setIcon(imageIcon);
			lblBackGround.setBounds(0, 0, 625, 417);
			contentPane.add(lblBackGround);
			
			JLabel lblUsername = new JLabel("Username");
			lblUsername.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
			lblUsername.setBounds(635, 73, 122, 36);
			contentPane.add(lblUsername);
			
			JLabel lblPassword = new JLabel("Password");
			lblPassword.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
			lblPassword.setBounds(635, 123, 122, 36);
			contentPane.add(lblPassword);
			
			passwordField = new JPasswordField();
			passwordField.setBounds(755, 121, 289, 36);
			contentPane.add(passwordField);
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
