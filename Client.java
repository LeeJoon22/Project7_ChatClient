package assignment7;

import java.io.*;
import java.util.*;
import java.net.*;

public class Client implements Runnable, Observer {

BufferedReader br1, br2;
PrintWriter pr1;
Socket socket;
Thread t1, t2;
String in = "", out = "";
Scanner pw;


public Client() {
    try {
    	// System.out.println("Enter Server Password: ");		Figure out password configuration
        t1 = new Thread(this);
        t2 = new Thread(this);
        pw = new Scanner(System.in);
        socket = new Socket("localhost", 5000);
        t1.start();;
        t2.start();

    } catch (Exception e) {
    }
}

public void run() {

    try {
        if (Thread.currentThread() == t2) {
            do {
                br1 = new BufferedReader(new InputStreamReader(System.in));
                pr1 = new PrintWriter(socket.getOutputStream(), true);
                in = br1.readLine();
                pr1.println(in);
            } while (!in.equals("Quit"));
        } else {
            do {
                br2 = new BufferedReader(new   InputStreamReader(socket.getInputStream()));
                out = br2.readLine();
                System.out.println("Server: " + out);
            } while (!out.equals("Quit"));
        }
    } catch (Exception e) {
    }

 	}

@Override
public void update(Observable o, Object arg) {
	// TODO Auto-generated method stub
	
}
 }
