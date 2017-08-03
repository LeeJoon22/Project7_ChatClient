package assignment7;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread implements Runnable{

	Socket socket;
	BufferedReader br;
	PrintWriter pr;
	String in;
	String out;


	public ServerThread(Socket s){
		socket = s;
	}

	public void run() {
		try {/*
			do{
				br = new BufferedReader(new InputStreamReader(System.in));
				pr = new PrintWriter(socket.getOutputStream(), true);
				in = br.readLine();
				pr.println(in);
			} while (!in.equals("Quit"));*/
			while(true){
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = br.readLine();
				System.out.println(out);
			}
		} catch (Exception e) {
		}
	}
}
