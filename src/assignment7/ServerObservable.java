package assignment7;

/*
 * Project 7 : Socket Programming
 * John Lee jol322
 * Benjamin Thorell blt895
 * 
 * Slip Days Used: 2 (using one here)
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

public class ServerObservable extends Observable{
	private ArrayList<String> peopleOnline = new ArrayList<String>();
	
	/*
	public static void main(String[] args) {
		try {
			new ServerMain().setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	//set up server networking to port 4242
	public void setUpNetworking() throws IOException{
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(5000);
		while (true) {
			Socket clientSocket = serverSock.accept();
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
			this.addObserver(writer);
			ServerMain.print("got a connection");
			//System.out.println("got a connection");
		}
	}


	class ClientHandler implements Runnable {
		private BufferedReader reader;

		public ClientHandler(Socket clientSocket) throws IOException {
			Socket sock = clientSocket;
			try{
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		public synchronized void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {

					if(message.charAt(0) != '+'){

						//if an account logs in, add username to list of currently online accounts
						if(message.contains("nowOnline()\\")){
							String username = message.substring(message.indexOf('\\')+1);
							if(!peopleOnline.contains(username)){
								peopleOnline.add(username);
								ServerMain.print(username + " has logged on!");
								message = "?";
								for(int i = 0; i < peopleOnline.size(); i++){
									message += (peopleOnline.get(i) + "/");
								}
							}

						}

						//if an account logs of, remove username from list of currently online accounts
						else if (message.contains("nowOffline()\\")){
							String username = message.substring(message.indexOf('\\')+1);
							if(peopleOnline.contains(username)){
								peopleOnline.remove(username);
								ServerMain.print(username + " has logged off!");
								message = "?";
								for(int i = 0; i < peopleOnline.size(); i++){
									message += (peopleOnline.get(i) + "/");
								}
							}
						}

						//sends a list of all accounts currently online to the clients
						else if(message.contains("peopleOnline()\\")){
							message = "?";
							for(int i = 0; i < peopleOnline.size(); i++){
								message += (peopleOnline.get(i) + "/");
							}
						}
					}
					System.out.println("server read " + message);
					setChanged();
					notifyObservers(message); //notify clientObserver
				}
			} catch (IOException e) {
				ServerMain.print("error in server");
				//System.out.println("error in server");
				e.printStackTrace();
			}
		}
	}

}
