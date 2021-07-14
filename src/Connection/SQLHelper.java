package Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Storage.Save;

public class SQLHelper {
	
	private Connection cont = JDBCconnection.getJDBCConnection();
	
	public boolean validateUser(UserModel userModel) {
		String sql1 = "select*from user where username = ? and password = ?";
		try {
			PreparedStatement ps = cont.prepareStatement(sql1);
			ps.setString(1, userModel.getUsername());
			ps.setString(2, userModel.getPassword());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				//JOptionPane jo = new JOptionPane();
				//JOptionPane.showMessageDialog(jo, "Đăng nhập thành công");
				
				// Lưu session tên user đăng nhập vào
				//UserInfor.sessionUsername = rs.getString("username");
				//UserInfor.iduser          = rs.getInt("iduser");
				
				return true;
			}
			return false;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}	
	}
	
	public void insertNewUserToDatabase(UserModel userModel) {
		String sql1 = "INSERT INTO user (iduser, username, password)" + 
				" VALUES (NULL, ?, ?)";
		try {
			PreparedStatement ps = cont.prepareStatement(sql1);
			ps.setString(1, userModel.getUsername());
			ps.setString(2, userModel.getPassword());
			
			ps.executeUpdate();
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	public boolean checkingUserExist(String username) {
		String sql1 = "select*from user where username = ?";
		try {
			PreparedStatement ps = cont.prepareStatement(sql1);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return true;
				
			}
			return false;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			return false;
		}
	}
	
	public boolean checkingStatusOnline() {
		if(cont != null) {
			return true;
		}
		return false;
	}
}
