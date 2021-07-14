package Connection;

public class UserModel {
	private int idUser;
	private String username;
	private String password;
	
	public UserModel(int idUser, String username, String password) {
		super();
		this.idUser = idUser;
		this.username = username;
		this.password = password;
	}
	
	public UserModel(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
