package Client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Connection.SQLHelper;
import Connection.UserModel;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class ClientSignFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldUsername;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientSignFrame frame = new ClientSignFrame();
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
	public ClientSignFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 570, 392);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nh\u1EADp c\u00E1c th\u00F4ng tin sau:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 17));
		lblNewLabel.setBounds(169, 48, 229, 43);
		contentPane.add(lblNewLabel);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 17));
		lblUsername.setBounds(10, 101, 118, 43);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 17));
		lblPassword.setBounds(10, 164, 118, 43);
		contentPane.add(lblPassword);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textFieldUsername.setBounds(136, 101, 381, 43);
		contentPane.add(textFieldUsername);
		textFieldUsername.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 17));
		passwordField.setBounds(136, 164, 381, 43);
		contentPane.add(passwordField);
		
		JButton btnSign = new JButton("\u0110\u0103ng k\u00FD");
		btnSign.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnSign.setBounds(210, 240, 133, 51);
		btnSign.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (textFieldUsername.getText().isEmpty() || passwordField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(ClientSignFrame.this, "Các trường thông tin chưa hợp lệ.");
					return;
				}
				
				if (isThisUsernameExist(textFieldUsername.getText().trim())) {
					JOptionPane.showMessageDialog(ClientSignFrame.this, "Username này đã tồn tại, hãy chọn tên username khác.");
					return;
				}
				
				UserModel userModel = new UserModel(textFieldUsername.getText().trim(), passwordField.getText().trim());
				insertNewUser(userModel);
				
				JOptionPane.showMessageDialog(ClientSignFrame.this, "Tạo tài khoản mới thành công.");
				dispose();
			}
		});
		contentPane.add(btnSign);
	}
	
	public boolean isThisUsernameExist(String username) {
		SQLHelper sqlHelper = new SQLHelper();
		if (sqlHelper.checkingUserExist(username)) {
			return true;
		}
		return false;
	}
	
	public void insertNewUser(UserModel userModel) {
		SQLHelper sqlHelper = new SQLHelper();
		sqlHelper.insertNewUserToDatabase(userModel);
	}
}
