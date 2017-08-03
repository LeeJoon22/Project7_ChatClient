package assignment7;

import java.io.*;
import java.util.*;
import java.net.*;

public class Server extends Observable {

	Socket socket;
	
	public Server() {		
		try {
			ServerSocket serverSocket = new ServerSocket(5000);
			System.out.println("Server is waiting. . . . ");
			while(true){
				socket = serverSocket.accept();
				ServerMain.updateText("Client connected with Ip " + socket.getInetAddress().getHostAddress());
				//System.out.println("Client connected with Ip " + socket.getInetAddress().getHostAddress());
				new ServerThread(socket).start();
			}
		} catch (Exception e) {
			System.out.println("We had an error! D:");
			e.printStackTrace();
		}
	}
}
