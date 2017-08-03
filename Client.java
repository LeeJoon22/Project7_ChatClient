package assignment7;

import java.io.*;
import java.util.*;
import java.net.*;

public class Client implements Observer {

	BufferedReader br1, br2;
	PrintWriter pr1;
	Socket socket;
	Thread t1, t2;
	String in = "", out = "";
	Scanner pw;

	public String m = "";

	public Client() {
		try {
			// System.out.println("Enter Server Password: ");		Figure out password configuration
			pw = new Scanner(System.in);
			socket = new Socket("localhost", 5000);
			new ClientThread(socket, this).start();
		} catch (Exception e) {
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}
}
