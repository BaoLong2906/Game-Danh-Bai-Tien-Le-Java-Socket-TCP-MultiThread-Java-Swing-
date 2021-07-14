package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	public int countRoom = 0;
	public static ArrayList<ArrayList<HandleThread>> roomList = new ArrayList<ArrayList<HandleThread>>();
	
	ServerSocket serverSocket;
	
	public void go() {
		try {
			serverSocket = new ServerSocket(9999);
			
			int countThreadBeat = 0;
			roomList.add(new ArrayList<HandleThread>());
			
			while (true) {
				Socket socket = serverSocket.accept();
				countThreadBeat++;
				
				if (countThreadBeat > 4) {
					countThreadBeat = 1;
					countRoom++;
					roomList.add(new ArrayList<HandleThread>());
				}
				
				HandleThread handleThread = new HandleThread(socket, countRoom);
				roomList.get(countRoom).add(handleThread);
				handleThread.start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.go();
	}	
}


