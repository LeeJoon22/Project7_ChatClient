package assignment7;

import java.io.*;
import java.net.*;

public class ClientThread extends Thread implements Runnable{

	PrintWriter pr;
	BufferedReader br;
	Socket socket;
	String out;
	Client c;

	public ClientThread(Socket s, Client c){
		socket = s;
		this.c = c;
	}

	public void run() {
		try {
			/*do {
				pr = new PrintWriter(socket.getOutputStream(), true);
				pr.println(ClientMain.t.getText());
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = br.readLine();
				ServerMain.updateText("Client: " + out);
				//System.out.println("Client: " + out);
			} while (!out.equals("Quit"));*/
			while(true){
				if(temp.message.length() > 0){
					pr = new PrintWriter(socket.getOutputStream(), true);
					pr.println(temp.message);
					temp.message = "";
				}
			}
		} catch (Exception e) {
		}
	}
}
