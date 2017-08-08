package assignment7;

/*
 * Project 7 : Socket Programming
 * John Lee jol322
 * Benjamin Thorell blt895
 * 
 * Slip Days Used: 2 (using one here)
 */

import java.io.*;
import java.util.*;

import java.nio.file.*;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

public class ClientObserver extends PrintWriter implements Observer {

	public ClientObserver(OutputStream out) {
		super(out);
	}

	@Override
	public synchronized void update(Observable arg0, Object arg) {
		try{

			//checks if password is sent
			String message = (String) arg;

			/*checks if input is a message or server command-
		lines beginning with '+' are messages
			 */
			if (message.charAt(0) != '+') {
				if (message.contains("existingUsernames()\\")) {
					arg = "=";

					File file = new File("accounts/");
					File[] files = file.listFiles();
					for (File f : files) {
						arg += (f.getName() + "/");
						String username = f.getName();
						File checkFile = new File("accounts/" + username);

						try {
							Scanner input = new Scanner(checkFile);
							arg += input.next() + "/";
							input.close();
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					}

				}

				//sends list of friends to client
				else if (message.contains("getFriends()\\")) {
					String username = message.substring(message.indexOf('\\')+1);
					arg = "@" + username + "\\";
					File fileAccount = new File("accounts/" + username);
					try {
						Scanner scan = new Scanner(fileAccount);
						scan.nextLine();
						while (scan.hasNextLine()) {
							arg += scan.nextLine() + "/";
							System.out.println("getfriends" + arg);
						}
						scan.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

				}

				//Changes password for an account
				else if (message.contains("changePassword()\\")){
					arg = "";
					String username = message.substring(message.indexOf('\\')+1, message.indexOf('/'));
					String newPassword = message.substring(message.indexOf('/')+1);
					File account = new File("accounts/" + username);
					String oldPassword = "";
					try{
						Scanner scan = new Scanner(account);
						oldPassword = scan.nextLine();

					}catch(Exception e){
						System.out.println("change password failed");
					}

					List<String> fileContent = new ArrayList<>();
					try{
						int lineNumber = 1;
						for (String line : Files.readAllLines(Paths.get("accounts/" + username))) {
							if (line.contains(oldPassword) && lineNumber == 1) {
								fileContent.add(line.replace(oldPassword, newPassword));
							} else {
								fileContent.add(line);
							}
							lineNumber++;
						}

						Files.write(Paths.get("accounts/" + username), fileContent);

					}catch(Exception e){
						System.out.println("password change failed");
					}

				}

				//deletes an account
				else if(message.contains("deleteAccount()\\")){
					arg="";
					String username = message.substring(message.indexOf('\\')+1);
					try {
						Files.delete(Paths.get("accounts/" + username));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				//adds a friend to an account
				else if (message.contains("addFriend()\\")){
					arg = "-";
					String whole = message.substring(message.indexOf('\\')+1);
					String username = whole.substring(0, whole.indexOf('\\'));
					String friend = whole.substring(whole.indexOf('\\')+1);

					File fileAccount = new File("accounts/" + username);
					boolean containsFriend = false;
					try {
						Scanner scanner = new Scanner(fileAccount);

						while (scanner.hasNextLine()) {
							String line = scanner.nextLine();
							if(line.equals(friend)) {
								containsFriend = true;
							}
						}
					} catch(FileNotFoundException e) {

					}
					if(!containsFriend){
						try{
							FileWriter fw = new FileWriter(fileAccount, true);
							fw.write(friend + "\n");
							fw.close();
						}catch(Exception e){
							System.out.println("add friend failed");
							e.printStackTrace();
						}
					}
				}

				//creates a new account
				else if (message.contains("createAccount()\\")) {
					arg = "#";

					String userPass = message.substring(message.indexOf('\\') + 1);
					String username = userPass.substring(0, userPass.indexOf('\\'));
					String password = userPass.substring(userPass.indexOf('\\') + 1);
					File fileAccount = new File("accounts/" + username);
					try {
						PrintWriter output = new PrintWriter(fileAccount);
						output.println(password);
						output.close();
					} catch (Exception e) {
						System.out.println("New File not created!");
						e.printStackTrace();
					}

				}

				else{

				}

			}

			System.out.println(arg);
			this.println(arg);
			this.flush();
		}catch(Exception e1){
			System.out.println("file not found");
		}

	}


	//Finds if file exists and is Case Sensitive
	private static boolean fileExistsCaseSensitive(String path) {
		try {
			File file = new File(path);
			return file.exists() && file.getCanonicalFile().getName().equals(file.getName());
		} catch (IOException e) {
			return false;
		}
	}
}