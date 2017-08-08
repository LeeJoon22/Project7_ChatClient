package assignment7;

import java.io.*;
import java.net.*;
import java.util.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class ClientMain extends Application{

	private ArrayList<String> access = new ArrayList<String>();
	private ArrayList<String> subAccess = new ArrayList<String>();
	private ArrayList<String> allFriends = new ArrayList<String>();
	private HashMap<String, String> allAccounts = new HashMap<String, String>();
	private ArrayList<String> online = new ArrayList<String>();

	private String username;
	private boolean privateChat = false;
	private boolean active = false;
	private boolean activeStat = true;
	private boolean connected = false;

	private BufferedReader reader;
	private PrintWriter writer;

	BorderPane allChat = new BorderPane();
	VBox incomingChat = new VBox(2);
	VBox myChat = new VBox(2);

	private ImageView[] emojis = {
			new ImageView(new Image("file:emoji/smile.png", 35, 35, true, true)),
			new ImageView(new Image("file:emoji/frown.png", 35, 35, true, true)),
			new ImageView(new Image("file:emoji/laugh.png", 35, 35, true, true)),
			new ImageView(new Image("file:emoji/cry.png", 35, 35, true, true)),
			new ImageView(new Image("file:emoji/angry.png", 35, 35, true, true)),
	};

	public static void main(String[] args) {
		try{
			new ClientMain().run(args);

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void run(String [] args) throws Exception{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

	//	setUpNetworking();
	//	existingUsernames();

		//------------------------------------TITLE SCREEN----------------------------------------//

		VBox titleScreen = new VBox();
		titleScreen.setAlignment(Pos.CENTER);
		titleScreen.setPrefHeight(140);
		titleScreen.setPadding(new Insets(20, 5, 30, 5));

		//setting up background image of title screen
		Image bImage = new Image("file:images/background4.jpg", 2050, 605, false, true);
		BackgroundSize backgroundSize = new BackgroundSize(titleScreen.getWidth(), titleScreen.getHeight(), true, true, true, false);
		BackgroundImage backgroundImage = new BackgroundImage(bImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
		Background background = new Background(backgroundImage);
		titleScreen.setBackground(background);

		//Displays Welcome
		Label title = new Label("Greetings, Summoner!");
		title.setStyle("-fx-font: bold italic 20pt 'Castellar'; -fx-text-fill: gold;");

		//Label for titlescreen notifications
		Label titleNote = new Label();
		titleNote.setPadding(new Insets(5, 5, 5, 5));

		//Testing
		/*final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
		Hyperlink link = new Hyperlink();
		link.setText("https://amazon.com");
		link.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                webEngine.load(link.getText());
        	    VBox vbox = new VBox();
        	    vbox.getChildren().addAll(browser);
        	    Scene scene2 = new Scene(vbox);
        	    Stage secondStage = new Stage();
        	    secondStage.setScene(scene2);
        	    secondStage.show();
            }
        });*/
		//End Testing

		titleScreen.getChildren().addAll(title, titleNote);

		//File Chooser for choosing background image
		final FileChooser fileChooser = new FileChooser();

		title.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent t){
				if(t.getButton() == MouseButton.PRIMARY){
					try{
						//Get image URL
						File file = fileChooser.showOpenDialog(primaryStage);
						if(file.isFile() &&
								(file.getName().contains(".jpg") || file.getName().contains(".png") ||
										file.getName().contains(".bmp") || file.getName().contains(".gif"))){

							String thumbURL = file.toURI().toURL().toString();

							Image imgLoad = new Image(thumbURL, 2050, 605, false, true);
							BackgroundSize backgroundSize = new BackgroundSize(titleScreen.getWidth(), titleScreen.getHeight(), true, true, true, false);
							BackgroundImage backgroundImage = new BackgroundImage(imgLoad, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
							Background background = new Background(backgroundImage);
							titleScreen.setBackground(background);
						}

					} catch(MalformedURLException|NullPointerException ex){
						System.out.println("File Not Found");
					}
				}
			}
		});


		//Login info
		HBox loginAll = new HBox(1);
		loginAll.setPadding(new Insets(1, 0, 1, 0));
		loginAll.setStyle("-fx-background-color: black; ");
		loginAll.setAlignment(Pos.CENTER);
		TextField username = new TextField("Username");
		username.setStyle("-fx-border-color: black; -fx-border-radius: 5.0; -fx-background-radius: 5.0;");
		HBox.setHgrow(username, Priority.SOMETIMES);

		PasswordField password = new PasswordField();
		password.setText("Password");
		password.setStyle("-fx-border-color: black; -fx-border-radius: 5.0; -fx-background-radius: 5.0;");
		HBox.setHgrow(password, Priority.SOMETIMES);

		TextField passReveal = new TextField();
		passReveal.setStyle("-fx-border-color: black; -fx-border-radius: 5.0; -fx-background-radius: 5.0;");
		HBox.setHgrow(passReveal, Priority.SOMETIMES);
		passReveal.setManaged(false);
		passReveal.setVisible(false);

		//Bind password field and textfield
		passReveal.textProperty().bindBidirectional(password.textProperty());


		Button loginbtn = new Button("Login");
		loginbtn.setStyle("-fx-font: 12pt 'Castellar'; -fx-text-fill: gold; -fx-border-color: white; "
				+ "-fx-border-radius: 5.0; -fx-background-color: blue; -fx-background-radius: 5.0;");
		loginbtn.setAlignment(Pos.BASELINE_CENTER);
		loginbtn.setDisable(true);

		Button createbtn = new Button("Create");
		createbtn.setStyle("-fx-font: 12pt 'Castellar'; -fx-text-fill: gold; -fx-border-color: white; "
				+ "-fx-border-radius: 5.0; -fx-background-color: blue; -fx-background-radius: 5.0;");
		createbtn.setAlignment(Pos.BASELINE_CENTER);


		//Settings button
		Image img = new Image("file:images/settings.png", 18, 18, true, true);
		ImageView setIcon = new ImageView(img);
		Button settingbtn = new Button();
		HBox.setHgrow(settingbtn, Priority.SOMETIMES);
		settingbtn.setPrefHeight(20);
		settingbtn.setStyle("-fx-border-color: grey; "
				+ "-fx-border-radius: 5.0; -fx-background-radius: 5.0;");
		settingbtn.setGraphic(setIcon);

		loginAll.getChildren().addAll(settingbtn, username, password, passReveal, loginbtn, createbtn);


		//Event handlers for login
		createbtn.setOnAction(e ->{
			try{
				if(titleScreen.getChildren().size() == 3){
					titleScreen.getChildren().remove(2);
				}

				//Check to see if acc already exists/username taken else create new acc
				if(allAccounts.containsKey(username.getText())){
					titleNote.setText("Username Taken!");
					titleNote.setStyle("-fx-text-fill: red;");

				} else{
					if(password.getText().equals(null)){
						titleNote.setText("Please Enter a Password!");
						titleNote.setStyle("-fx-text-fill: red;");

					} else{
						createAccount(username.getText(), password.getText());

						titleNote.setText("Account Created!");
						titleNote.setStyle("-fx-text-fill: green;");

						existingUsernames();
					}
				}

			} catch (Exception e1) {
				titleNote.setText("Invalid Username or Password!");
				titleNote.setStyle("-fx-text-fill: red;");
			}
		});


		//Check if Username exists and prints welcome
		username.setOnAction(e ->{
			peopleOnline();

			System.out.println("People Online: " + online);

			try{
				Thread.sleep(1);
			} catch(Exception ex){

			}

			if(titleScreen.getChildren().size() == 3){
				titleScreen.getChildren().remove(2);
			}

			this.username = username.getText();
			loginbtn.setDisable(true);
			password.setDisable(false);

			if(online.contains(username.getText())){
				title.setText("Welcome!");

				titleNote.setText("This User is already online!");
				titleNote.setStyle("-fx-text-fill: red;");

				createbtn.setDisable(true);
				password.setDisable(true);

			} else if(allAccounts.containsKey(username.getText())){
				titleNote.setText("");
				title.setText("Greetings, " + username.getText() + "!");
				getFriends(username.getText());
				createbtn.setDisable(true);

			} else{
				titleNote.setText("");
				title.setText("Welcome!");
				createbtn.setDisable(false);
			}
		});


		//Checks for valid password connected to Username
		Button forgotbtn = new Button("Forgot Password?");
		forgotbtn.setStyle("-fx-font: italic 8pt 'Georgia'; ");

		//Checks for correct Password
		password.setOnAction(e ->{

			if(titleScreen.getChildren().size() == 3){
				titleScreen.getChildren().remove(2);
			}

			if(allAccounts.containsKey(this.username) && password.getText().equals(allAccounts.get(this.username))){
				titleNote.setText("Correct Password!");
				titleNote.setStyle("-fx-text-fill: green; ");
				loginbtn.setDisable(false);

			} else if(!allAccounts.containsKey(this.username)){
				titleNote.setText("Create a new account!");
				titleNote.setStyle("-fx-text-fill: green; ");
				loginbtn.setDisable(true);

			} else{
				titleNote.setText("Wrong Password!");
				titleNote.setStyle("-fx-text-fill: red; ");
				titleScreen.getChildren().add(forgotbtn);
				loginbtn.setDisable(true);
			}
		});

		//Reveals password
		title.setOnMouseEntered(e -> {
			password.setManaged(false);
			password.setVisible(false);
			passReveal.setManaged(true);
			passReveal.setVisible(true);
		});

		title.setOnMouseExited(e -> {
			passReveal.setManaged(false);
			passReveal.setVisible(false);
			password.setManaged(true);
			password.setVisible(true);
		});


		//Constructing titleScreen window
		BorderPane welcomePage = new BorderPane();
		welcomePage.setTop(loginAll);
		welcomePage.setBottom(titleScreen);
		Scene loginPage = new Scene(welcomePage);
		primaryStage.setScene(loginPage);
		primaryStage.setTitle("Ben & John's Talk Show");
		primaryStage.show();

		primaryStage.setOnCloseRequest(e -> {
			System.out.println("Chat Closed");
			Platform.exit();
			nowOffline();
			//System.exit(0);
		});





		//--------------------------------------CHAT WINDOW--------------------------------------------//

		//Friends list
		VBox friendList = new VBox();
		friendList.setPrefHeight(300);
		friendList.setAlignment(Pos.CENTER);
		friendList.setStyle("-fx-border-color: burlywood; -fx-background-color: white; -fx-border-radius: 5.0; ");

		Button listLabel = new Button("Friends List");
		listLabel.setPadding(new Insets(5, 5, 5, 5));
		listLabel.setAlignment(Pos.CENTER);
		listLabel.setPrefWidth(100);
		listLabel.setStyle("-fx-font: bold italic 12pt 'Old English Text MT'; -fx-text-fill: gold; -fx-border-color: black; "
				+ "-fx-border-radius: 5.0; -fx-background-color: blue; -fx-background-radius: 5.0; ");
		friendList.getChildren().add(listLabel);


		//Color and message input
		HBox chatBox = new HBox(2);
		chatBox.setStyle("-fx-border-color: brown; -fx-border-radius: 5.0; ");
		chatBox.setPadding(new Insets(2, 5, 2, 5));
		chatBox.setAlignment(Pos.CENTER);

		TextField chatField = new TextField();
		chatField.setPrefWidth(400);
		chatField.setStyle("-fx-border-color: burlywood; -fx-border-radius: 5.0; ");
		HBox.setHgrow(chatField, Priority.ALWAYS);

		Label colorLabel = new Label("Color:");
		colorLabel.setStyle("-fx-font: italic 10pt 'Arial'; ");
		colorLabel.setPadding(new Insets(3, 3, 3, 3));
		colorLabel.setAlignment(Pos.CENTER);

		TextField colorField = new TextField();
		colorField.setPrefWidth(100);
		colorField.setStyle("-fx-border-color: burlywood; -fx-border-radius: 5.0; ");


		//Change background button
		Image bg = new Image("file:images/League.png", 35, 35, true, true);
		ImageView bgImage = new ImageView(bg);

		Label bgLabel = new Label();
		bgLabel.setPrefWidth(50);
		bgLabel.setAlignment(Pos.CENTER);
		bgLabel.setPadding(new Insets(2, 2, 2, 2));
		HBox.setHgrow(bgLabel, Priority.SOMETIMES);
		bgLabel.setStyle("-fx-border-radius: 5.0; -fx-border-color: burlywood; "
				+ "-fx-background-color: white; -fx-background-radius: 5.0;");
		bgLabel.setGraphic(bgImage);


		//Stay Hidden option
		Image inactiveImage = new Image("file:images/toggleOff.png", 35, 35, true, true);
		Image activeImage = new Image("file:images/toggleOn.png", 35, 35, true, true);
		ImageView setInActive = new ImageView(inactiveImage);
		ImageView setActive = new ImageView(activeImage);

		Label activeLabel = new Label();
		activeLabel.setPrefSize(25, 25);
		activeLabel.setPadding(new Insets(0, 3, 0, 3));
		activeLabel.setAlignment(Pos.CENTER);
		HBox.setHgrow(activeLabel, Priority.SOMETIMES);
		activeLabel.setGraphic(setActive);

		chatBox.getChildren().addAll(colorLabel, colorField, chatField, activeLabel, bgLabel);


		//Chat History
		incomingChat.setPadding(new Insets(2, 2, 2, 2));
		incomingChat.setAlignment(Pos.TOP_LEFT);

		myChat.setPadding(new Insets(2, 2, 2, 2));
		myChat.setAlignment(Pos.TOP_RIGHT);

		allChat.setLeft(incomingChat);
		allChat.setRight(myChat);


		//Event handler for Change background button
		bgLabel.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent t){
				if(t.getButton() == MouseButton.PRIMARY){
					try{
						//Get image URL
						File file = fileChooser.showOpenDialog(primaryStage);
						if(file.isFile() &&
								(file.getName().contains(".jpg") || file.getName().contains(".png") ||
										file.getName().contains(".bmp") || file.getName().contains(".gif"))){

							String thumbURL = file.toURI().toURL().toString();

							Image imgLoad = new Image(thumbURL, allChat.getWidth(), allChat.getHeight(), false, true);
							BackgroundSize backgroundSize = new BackgroundSize(allChat.getWidth(), allChat.getHeight(), true, true, true, false);
							BackgroundImage backgroundImage = new BackgroundImage(imgLoad, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
							Background background = new Background(backgroundImage);
							allChat.setBackground(background);
						}

					} catch(MalformedURLException|NullPointerException ex){
						System.out.println("File Not Found");
					}
				}
			}
		});


		//Notifications
		HBox topContainer = new HBox(2);
		topContainer.setAlignment(Pos.CENTER);
		topContainer.setPadding(new Insets(1, 1, 1, 1));

		HBox notify = new HBox(2);
		notify.setPadding(new Insets(2,2,2,2));
		notify.setPrefHeight(30);
		notify.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(notify, Priority.ALWAYS);
		notify.setStyle("-fx-font: bold italic 10pt 'Times New Roman'; -fx-border-color: brown; "
				+ "-fx-border-radius: 5.0; -fx-background-color: burlywood; -fx-background-radius: 5.0; ");
		Label notes = new Label("Welcome to the Chat Room!");
		notify.getChildren().addAll(notes);


		//Private Chat option
		Image lock = new Image("file:images/lock.png", 25, 25, true, true);
		Image unlock = new Image("file:images/unlock.png", 25, 25, true, true);
		ImageView lockIcon = new ImageView(lock);
		ImageView unlockIcon = new ImageView(unlock);
		Button lockingbtn = new Button();
		lockingbtn.setAlignment(Pos.CENTER);
		HBox.setHgrow(lockingbtn, Priority.SOMETIMES);
		lockingbtn.setStyle("-fx-border-color: brown; "
				+ "-fx-border-radius: 5.0; -fx-background-radius: 5.0; ");
		lockingbtn.setGraphic(unlockIcon);


		//Log off function
		Image logOffImage = new Image("file:images/powerOffv2.png", 35, 30, true, true);
		Image logOnImage = new Image("file:images/powerOnv2.png", 35, 30, true, true);
		ImageView logOff = new ImageView(logOffImage);
		ImageView logOn = new ImageView(logOnImage);

		Label logLabel = new Label();
		logLabel.setPrefWidth(60);
		logLabel.setPadding(new Insets(1,1,1,1));
		logLabel.setStyle("-fx-border-color: brown; -fx-background-color: burlywood; "
				+ "-fx-border-radius: 5.0; -fx-background-radius: 5.0; ");
		logLabel.setAlignment(Pos.CENTER);
		HBox.setHgrow(logLabel, Priority.SOMETIMES);
		logLabel.setGraphic(logOn);

		topContainer.getChildren().addAll(notify, lockingbtn, logLabel);
		allChat.setTop(topContainer);


		//Event handler for logging off
		logLabel.setOnMouseEntered(e -> {
			logLabel.setGraphic(logOff);
			notes.setText("Log Off!");
			notes.setStyle("-fx-text-fill: black;");
		});

		logLabel.setOnMouseExited(e -> {
			logLabel.setGraphic(logOn);
			notes.setText("Welcome to the Chat Room!");
			notes.setStyle("-fx-text-fill: black;");
		});

		logLabel.setOnMouseClicked(e -> {
			nowOffline();
			System.out.println(username + "Logged off");
			nowOffline();
			Platform.exit();
			//System.exit(0);
		});


		//Event handler for locking button
		lockingbtn.setOnAction(e -> {
			if(privateChat == false){
				privateChat = true;
				lockingbtn.setGraphic(lockIcon);

				notes.setText("Chat History Locked!");
				notes.setStyle("-fx-text-fill: red;");

			} else{
				privateChat = false;
				lockingbtn.setGraphic(unlockIcon);

				notes.setText("Chat History Unlocked!");
				notes.setStyle("-fx-text-fill: green;");
			}
		});


		//Event handler for set Active stat button
		activeLabel.setOnMouseClicked(e -> {
			if(activeStat == true){

				//Finds sound file path
				/*try{
					FileChooser fc = new FileChooser();
					//fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.mp3"));
					File file = fc.showOpenDialog(null);
					String path = file.getAbsolutePath();
					path = path.replace("\\", "/");
					System.out.println(path);
				} catch(Exception ex){
					System.out.println("File no found");
				}*/

				nowOffline();

				//Plays sound
				try{
					String musicFile = "sounds/NoteBar1.mp3";
					Media sound = new Media(new File(musicFile).toURI().toString());
					MediaPlayer mediaPlayer = new MediaPlayer(sound);
					mediaPlayer.play();

				} catch(Exception ex){
					System.out.println("Sound file not found");
				}

				activeLabel.setGraphic(setInActive);
				activeStat = false;
				notes.setText("Your Status is now Inactive!");
				notes.setStyle("-fx-text-fill: red;");

			} else{
				nowOnline();

				try{
					String musicFile = "sounds/NoteBar1.mp3";
					Media sound = new Media(new File(musicFile).toURI().toString());
					MediaPlayer mediaPlayer = new MediaPlayer(sound);
					mediaPlayer.play();

				} catch(Exception ex){
					System.out.println("Sound file not found");
				}

				activeLabel.setGraphic(setActive);
				activeStat = true;
				notes.setText("Your Status is now Active!");
				notes.setStyle("-fx-text-fill: green;");
			}
		});

		activeLabel.setOnMouseEntered(e -> {
			notes.setText("Click to set Online Status!");
			notes.setStyle("-fx-text-fill: black;");
		});

		activeLabel.setOnMouseExited(e -> {
			notes.setText("Welcome to the Chat Room!");
			notes.setStyle("-fx-text-fill: black;");
		});


		//Event handler for Change background button
		bgLabel.setOnMouseEntered(e -> {
			bgLabel.setStyle("-fx-border-radius: 5.0; -fx-border-color: burlywood; "
					+ "-fx-background-color: brown; -fx-background-radius: 5.0;");
			notes.setText("Click to set New Background!");
			notes.setStyle("-fx-text-fill: black;");
		});

		bgLabel.setOnMouseExited(e -> {
			bgLabel.setStyle("-fx-border-radius: 5.0; -fx-border-color: burlywood; "
					+ "-fx-background-color: white; -fx-background-radius: 5.0;");
			notes.setText("Welcome to the Chat Room!");
			notes.setStyle("-fx-text-fill: black;");
		});


		//Construct Entire Chat Window
		BorderPane chatWindow = new BorderPane();
		chatWindow.setStyle("-fx-border-color: black; -fx-border-radius: 5.0; ");
		chatWindow.setLeft(friendList);
		chatWindow.setCenter(allChat);
		chatWindow.setBottom(chatBox);


		//Setting up Friends List
		ListView<String> myFriends = new ListView<>
				(FXCollections.observableArrayList(allFriends));
		myFriends.setPrefWidth(100);
		myFriends.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		TextField addFriends = new TextField("Add Friends");
		addFriends.setPrefWidth(100);
		friendList.getChildren().addAll(new ScrollPane(myFriends), addFriends);


		//EventHandlers for adding and selecting friends
		addFriends.setOnAction(e -> {
			String friend = addFriends.getText();

			System.out.println("All Friends: " + allFriends);
			//System.out.println(allFriends.contains(friend));

			if(allFriends.contains(friend)){
				notes.setText(friend + " is already your friend");
				notes.setStyle("-fx-text-fill: red;");

			} else if(!allFriends.contains(friend) && !friend.equals(this.username) && allAccounts.containsKey(friend)){
				allFriends.add(friend);
				myFriends.getItems().add(friend);
				addFriend(this.username, friend);

				notes.setText(friend + " was added to your friends list!");
				notes.setStyle("-fx-text-fill: green;");

			} else{
				notes.setText("The person named " + friend + " was not found");
				notes.setStyle("-fx-text-fill: red;");
			}
		});


		//Event Handler for Active friends button
		listLabel.setOnAction(e ->{
			if(active == false){
				active = true;
				listLabel.setText("Active");
				System.out.println("Online: " + online);

				ArrayList<String> temp = new ArrayList<String>();

				for(int i = 0; i < myFriends.getItems().size(); i++){
					if(online.contains(myFriends.getItems().get(i))){
						temp.add(myFriends.getItems().get(i));
					}
				}

				System.out.println("My Friends online: " + temp);

				myFriends.getSelectionModel().clearSelection();
				myFriends.getItems().clear();
				myFriends.getItems().addAll(temp);
				myFriends.refresh();

			} else{
				active = false;
				listLabel.setText("Friends List");
				System.out.println("All Friends: " + allFriends);

				myFriends.getSelectionModel().clearSelection();
				myFriends.getItems().clear();
				myFriends.getItems().addAll(allFriends);
				myFriends.refresh();
			}
		});


		//Select Friends you want to talk to
		myFriends.getSelectionModel().selectedIndexProperty().addListener(ov -> {
			System.out.println("Selected");

			String listOfPeople = "";
			access.clear();
			for(String selected: myFriends.getSelectionModel().getSelectedItems()){
				System.out.println(selected);

				if(allAccounts.containsKey(selected) && online.contains(selected)){
					connected = true;
					listOfPeople += selected + ", ";
					access.add(selected);

				} else{
					connected = false;
					notes.setText(selected + " is not online at the moment");
					notes.setStyle("-fx-text-fill: red;");
				}
			}
			if(connected == true){
				if(listOfPeople.length()>2){
					notes.setText("You are now connected to " + listOfPeople.substring(0, listOfPeople.length()-2) + "!");
					notes.setStyle("-fx-text-fill: green;");
				}
			}
		});


		Scene mainChat = new Scene(chatWindow);


		//EventHandler for taking in message
		chatField.setOnAction(e -> {

			//Testing emojis
			String chatText = chatField.getText().trim();
			if(chatText.equals(":)") || chatText.equals(":(") || chatText.equals("xD") || chatText.equals(":'(") || chatText.equals(":/")){

				Label myEmoji = new Label();
				myEmoji.setStyle("-fx-font: 10pt 'Arial';");

				switch(chatText){
					case ":)":	ImageView cloneImage = new ImageView();
						cloneImage.setImage(SwingFXUtils.toFXImage(SwingFXUtils.fromFXImage(emojis[0].getImage(), null), null));
						myEmoji.setGraphic(cloneImage);
						break;

					case ":(":	ImageView cloneImage2 = new ImageView();
						cloneImage2.setImage(SwingFXUtils.toFXImage(SwingFXUtils.fromFXImage(emojis[1].getImage(), null), null));
						myEmoji.setGraphic(cloneImage2);
						break;

					case "xD":	ImageView cloneImage3 = new ImageView();
						cloneImage3.setImage(SwingFXUtils.toFXImage(SwingFXUtils.fromFXImage(emojis[2].getImage(), null), null));
						myEmoji.setGraphic(cloneImage3);
						break;

					case ":'(":	ImageView cloneImage4 = new ImageView();
						cloneImage4.setImage(SwingFXUtils.toFXImage(SwingFXUtils.fromFXImage(emojis[3].getImage(), null), null));
						myEmoji.setGraphic(cloneImage4);
						break;

					case ":/":	ImageView cloneImage5 = new ImageView();
						cloneImage5.setImage(SwingFXUtils.toFXImage(SwingFXUtils.fromFXImage(emojis[4].getImage(), null), null));
						myEmoji.setGraphic(cloneImage5);
						break;
				}

				myChat.getChildren().add(myEmoji);

				Label space = new Label();
				space.setMinHeight(35);
				incomingChat.getChildren().add(space);

				//Setting up message for sending
				String messageSetUp = "";
				for(int i = 0; i < access.size(); i++){
					messageSetUp += (access.get(i) + "|");
				}

				writer.println("+" + this.username + "/" + messageSetUp + "\\" + chatField.getText());
				writer.flush();
				chatField.setText("");

			} else{
				//String timestamp = new java.text.SimpleDateFormat("h:mm:ss a").format(new Date());
				Label inputText = new Label(chatField.getText());

				String text = colorField.getText();
				Paint color;

				try{
					color = Paint.valueOf(text);

				} catch(Exception ex){
					color = Paint.valueOf("burlywood");
				}

				inputText.setStyle("-fx-font: 10pt 'Times New Roman';");
				inputText.setBackground(new Background(new BackgroundFill(color, new CornerRadii(5.0), new Insets(0, 0, 0, 0))));
				inputText.setPadding(new Insets(2,2,2,2));
				Label space = new Label();
				space.setPrefHeight(20);
				incomingChat.getChildren().add(space);

				myChat.getChildren().add(inputText);

				//Setting up message for sending
				String messageSetUp = "";
				for(int i = 0; i < access.size(); i++){
					messageSetUp += (access.get(i) + "|");
				}

				writer.println("+" + this.username + "/" + messageSetUp + "\\" + chatField.getText());
				writer.flush();
				chatField.setText("");

				try{
					String musicFile = "sounds/Note1.mp3";
					Media sound = new Media(new File(musicFile).toURI().toString());
					MediaPlayer mediaPlayer = new MediaPlayer(sound);
					mediaPlayer.play();

				} catch(Exception ex){
					System.out.println("Sound file not found");
				}
			}
		});

		//Login to switch Panes
		loginbtn.setOnAction(e -> {
			myFriends.getItems().addAll(allFriends);
			//setUpNetworking();
			existingUsernames();
			primaryStage.setScene(mainChat);
			primaryStage.setTitle(this.username);
			primaryStage.show();
			nowOnline();
			try{
				Thread.sleep(1);
			} catch(Exception ex){

			}
		});


		//-----------------------------------SETTINGS WINDOW---------------------------------------//

		//Settings Label display
		HBox settingDisplay = new HBox();
		settingDisplay.setStyle("-fx-border-color: black; -fx-border-radius: 5.0; "
				+ "-fx-background-color: black; -fx-background-radius: 5.0;");
		settingDisplay.setPadding(new Insets(5, 5, 5, 5));
		settingDisplay.setPrefWidth(300);
		settingDisplay.setAlignment(Pos.CENTER_LEFT);

		Label settingLabel = new Label("~Settings~");
		settingLabel.setStyle("-fx-font: bold italic 15pt 'Castellar'; -fx-text-fill: White; ");
		HBox.setHgrow(settingLabel, Priority.ALWAYS);

		settingDisplay.getChildren().addAll(settingLabel);


		//All Options
		VBox options = new VBox(5);
		options.setPadding(new Insets(5,5,5,5));


		//Password Reset
		VBox resetBox = new VBox(5);
		resetBox.setPadding(new Insets(5,5,5,5));
		resetBox.setStyle("-fx-border-color: black; -fx-border-radius: 5.0; "
				+ "-fx-background-radius: 5.0;");

		HBox resetTitle = new HBox(80);
		resetTitle.setAlignment(Pos.CENTER_LEFT);

		Label resetLabel = new Label("Password Reset");
		HBox.setHgrow(resetLabel, Priority.ALWAYS);
		resetLabel.setStyle("-fx-font: bold italic 10pt 'Times New Roman'; -fx-text-fill: black; -fx-underline: true; ");
		Button deletebtn = new Button("Delete Account");
		deletebtn.setStyle("-fx-font: italic 8pt 'Times New Roman'; ");
		deletebtn.setDisable(true);
		resetTitle.getChildren().addAll(resetLabel, deletebtn);

		HBox userName = new HBox(2);
		Label userLabel = new Label("Enter Username: ");
		TextField userText = new TextField("Username");
		HBox.setHgrow(userName, Priority.ALWAYS);
		userName.getChildren().addAll(userLabel, userText);

		HBox confirmPass = new HBox(2);
		Label confirm = new Label("Enter old password: ");
		TextField oldPass = new TextField("Old Password");
		oldPass.setDisable(true);
		HBox.setHgrow(oldPass, Priority.ALWAYS);
		confirmPass.getChildren().addAll(confirm, oldPass);

		HBox resetPass = new HBox(2);
		Label reset = new Label("Enter new password: ");
		TextField newPass = new TextField("New Password");
		newPass.setDisable(true);
		HBox.setHgrow(newPass, Priority.ALWAYS);
		resetPass.getChildren().addAll(reset, newPass);

		//Notifications Label for change status
		Label holder = new Label("");
		holder.setStyle("-fx-font: italic 8pt 'Times New Roman'; ");
		holder.setPadding(new Insets(5, 5, 5, 5));

		//Notifications for Forgot Box
		Label holder2 = new Label("");
		holder2.setStyle("-fx-font: italic 8pt 'Times New Roman'; ");
		holder2.setPadding(new Insets(5, 5, 5, 5));

		resetBox.getChildren().addAll(resetTitle, userName, confirmPass, resetPass, holder);


		//Forgot Password
		VBox forgotBox = new VBox(5);
		forgotBox.setPadding(new Insets(5,5,5,5));
		forgotBox.setStyle("-fx-border-color: black; -fx-border-radius: 5.0; "
				+ "-fx-background-radius: 5.0;");

		Label forgotLabel = new Label("Forgot Password");
		forgotLabel.setStyle("-fx-font: bold italic 10pt 'Times New Roman'; -fx-text-fill: black; -fx-underline: true; ");

		HBox userNameF = new HBox(2);
		Label userLabelF = new Label("Enter Username: ");
		TextField userTextF = new TextField("Username");
		HBox.setHgrow(userNameF, Priority.ALWAYS);
		userNameF.getChildren().addAll(userLabelF, userTextF);

		HBox resetPassF = new HBox(2);
		Label resetF = new Label("Enter new password: ");
		TextField newPassF = new TextField("New Password");
		newPassF.setDisable(true);
		HBox.setHgrow(newPassF, Priority.ALWAYS);
		resetPassF.getChildren().addAll(resetF, newPassF);

		forgotBox.getChildren().addAll(forgotLabel, userNameF, resetPassF, holder2);


		//Event Handler for password reset
		userText.setOnAction(e -> {
			//clear settings for forgot
			userTextF.setText("Username");
			newPassF.setText("New Password");
			newPassF.setDisable(true);
			holder2.setText("");

			try{
				if(allAccounts.containsKey(userText.getText())){
					holder.setText("Username Confirmed!");
					holder.setStyle("-fx-font: italic 8pt 'Times New Roman'; -fx-text-fill: green; ");
					oldPass.setDisable(false);
					newPass.setDisable(true);
					deletebtn.setDisable(true);

				} else{
					holder.setText("Invalid Username!");
					holder.setStyle("-fx-font: italic 8pt 'Times New Roman'; -fx-text-fill: red; ");
					oldPass.setDisable(true);
					newPass.setDisable(true);
					deletebtn.setDisable(true);
				}

			} catch(Exception ex){
				holder.setText("Invalid Username or Password!");
				holder.setStyle("-fx-font: italic 8pt 'Times New Roman'; -fx-text-fill: red; ");
			}
		});

		oldPass.setOnAction(e -> {
			try{
				if(oldPass.getText().equals(allAccounts.get(userText.getText()))){
					holder.setText("Password Confirmed!");
					holder.setStyle("-fx-font: italic 8pt 'Times New Roman'; -fx-text-fill: green; ");
					newPass.setDisable(false);
					deletebtn.setDisable(false);

				} else{
					holder.setText("Invalid Password!");
					holder.setStyle("-fx-font: italic 8pt 'Times New Roman'; -fx-text-fill: red; ");
					newPass.setDisable(true);
					deletebtn.setDisable(true);
				}

			} catch(Exception ex){
				holder.setText("Invalid Username or Password!");
				holder.setStyle("-fx-font: italic 8pt 'Times New Roman'; -fx-text-fill: red; ");
			}
		});

		newPass.setOnAction(e -> {
			try{
				holder.setText("Password changed!");
				holder.setStyle("-fx-font: italic 8pt 'Times New Roman'; -fx-text-fill: green; ");
				changePassword(userText.getText(), newPass.getText());
				oldPass.setDisable(true);
				newPass.setDisable(true);
				existingUsernames();

			} catch(Exception ex){
				System.out.println("Change Failed");
			}
		});

		deletebtn.setOnAction(e -> {
			deleteAccount(userText.getText());
			holder.setText("Account Deleted!");
			holder.setStyle("-fx-font: italic 8pt 'Times New Roman'; -fx-text-fill: red; ");
			existingUsernames();
		});


		//Event Handler for forgot password
		userTextF.setOnAction(e -> {
			//clear settings for reset
			userText.setText("Username");
			oldPass.setText("Old Password");
			oldPass.setDisable(true);
			newPass.setText("New Password");
			newPass.setDisable(true);
			deletebtn.setDisable(true);
			holder.setText("");

			try{
				if(allAccounts.containsKey(userTextF.getText())){
					holder2.setText("Username Confirmed");
					holder2.setStyle("-fx-font: italic 8pt 'Times New Roman'; -fx-text-fill: green; ");
					newPassF.setDisable(false);

				} else{
					holder2.setText("Invalid Username!");
					holder2.setStyle("-fx-font: italic 8pt 'Times New Roman'; -fx-text-fill: red; ");
					newPassF.setDisable(true);
				}

			} catch(Exception ex){
				holder2.setText("Invalid Username or Password!");
				holder2.setStyle("-fx-font: italic 8pt 'Times New Roman'; -fx-text-fill: red; ");
			}
		});

		newPassF.setOnAction(e -> {
			try{
				changePassword(userTextF.getText(), newPassF.getText());
				holder2.setText("Password changed!");
				holder2.setStyle("-fx-font: italic 8pt 'Times New Roman'; -fx-text-fill: green; ");
				newPassF.setDisable(true);
				existingUsernames();

			} catch(Exception ex){
				System.out.println("Change Failed");
			}
		});


		//Contains all options
		options.getChildren().addAll(resetBox, forgotBox);


		//Contructs Settings Window
		BorderPane settingPane = new BorderPane();
		settingPane.setTop(settingDisplay);
		settingPane.setCenter(options);

		//New Scene and Stage for Settings
		Scene settingScene = new Scene(settingPane);
		Stage stage = new Stage();
		stage.setTitle("Ben and John's Talk Show");

		//EventHandler for settings button
		settingbtn.setOnAction(e -> {
			if(stage.isShowing() == true){

				//clear settings for reset
				userText.setText("Username");
				oldPass.setText("Old Password");
				oldPass.setDisable(true);
				newPass.setText("New Password");
				newPass.setDisable(true);
				deletebtn.setDisable(true);
				holder.setText("");

				//clear settings for forgot
				userTextF.setText("Username");
				newPassF.setText("New Password");
				newPassF.setDisable(true);
				holder2.setText("");

				stage.close();
			} else{
				stage.setScene(settingScene);
				stage.show();
			}
		});

		//EventHandler for settings button
		forgotbtn.setOnAction(e -> {
			stage.setScene(settingScene);
			stage.show();
		});

		VBox ipBox = new VBox();
		TextField ipField = new TextField("127.0.0.1");
		ipBox.getChildren().add(ipField);
		Scene ipWindow = new Scene(ipBox);
		ipField.setOnAction(e ->{
			setUpNetworking(ipField.getText());
			existingUsernames();
			primaryStage.setScene(loginPage);
		});
		primaryStage.setScene(ipWindow);

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

	//All accounts
	private void existingUsernames(){
		writer.println("existingUsernames()\\");
		writer.flush();
	}

	//Creating an account
	private void createAccount(String username, String password){
		writer.println("createAccount()\\" + username + "\\" + password);
		writer.flush();
	}

	//Adding Friends
	private void addFriend(String username, String friend){
		writer.println("addFriend()\\" + username + "\\" + friend);
		writer.flush();
	}

	//All accounts online
	private void peopleOnline(){
		writer.println("peopleOnline()\\");
		writer.flush();
	}

	//Set online stat when logged in
	private void nowOnline(){
		writer.println("nowOnline()\\" + username);
		writer.flush();
	}

	//Logs off account
	private void nowOffline(){
		writer.println("nowOffline()\\" + username);
		writer.flush();
	}

	//Gets Friends
	private void getFriends(String username){
		writer.println("getFriends()\\" + username);
		writer.flush();
	}

	//Change password
	private void changePassword(String username, String password){
		writer.println("changePassword()\\" + username + "/"  + password);
		writer.flush();
	}

	//Delete account
	private void deleteAccount(String deleteUser){
		writer.println("deleteAccount()\\" + deleteUser);
		writer.flush();
	}


	private void setUpNetworking(String ip){
		try{
			@SuppressWarnings("resource")
			Socket sock = new Socket(ip, 5000); //"127.0.0.1" or "192.168.43.10"
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("networking established");
			Thread readerThread = new Thread(new IncomingReader());
			readerThread.start();

		}catch(Exception e){
			System.out.println("network setup failed");
			e.printStackTrace();
		}
	}


	//Incoming messages
	class IncomingReader implements Runnable {
		public void run() {
			String message;

			try {
				while ((message = reader.readLine()) != null) {

					//Message
					if(message.length() > 1){

						if(message.charAt(0) == '+'){
							subAccess.clear();
							storeMessage();
							String substringAccess = message.substring(message.indexOf("/") + 1, message.indexOf("\\"));

							while(!substringAccess.equals("")){
								String add = substringAccess.substring(0, substringAccess.indexOf("|"));
								subAccess.add(add);
								substringAccess = substringAccess.substring(substringAccess.indexOf("|") + 1);
							}

							if(subAccess.contains(username)){
								displaymsg(message);
								storeMessage();
							}

						} else if(message.charAt(0) == '='){	//sets all existing usernames and their passwords
							allAccounts.clear();
							String list = message.substring(1);

							while(!list.equals("")){
								String username = list.substring(0, list.indexOf('/'));
								list = list.substring(list.indexOf('/') + 1);
								String password = list.substring(0, list.indexOf('/'));
								list = list.substring(list.indexOf('/') + 1);
								allAccounts.put(username, password);
								System.out.println(allAccounts);
							}

						} else if(message.charAt(0) == '@'){	//sets friends list usernames
							String username1 = message.substring(1, message.indexOf('\\'));

							if(username1.equals(username)) {
								if (!allFriends.isEmpty()){
									allFriends.clear();
								}
								String friendsList = message.substring(message.indexOf('\\') + 1);

								while (!friendsList.equals("")) {
									allFriends.add(friendsList.substring(0, friendsList.indexOf('/')));
									friendsList = friendsList.substring(friendsList.indexOf('/') + 1);
								}
							}
						} else if(message.charAt(0) == '#'){
							existingUsernames();

						} else if(message.charAt(0) == '-'){
							getFriends(username);

						} else if(message.charAt(0) == '?'){	//Creates list of all people online
							online.clear();
							String all = message.substring(1);
							while(!all.equals("")){
								online.add(all.substring(0, all.indexOf('/')));
								all = all.substring(all.indexOf('/')+1);
							}

						} else{

						}
					}
				}

			} catch (IOException ex) {
				System.out.println("Error with message");
				ex.printStackTrace();
			}
		}

		//Displays incoming message
		private void displaymsg(String message){
			Platform.runLater(() -> {
				String user = message.substring(1, message.indexOf("/"));
				String incomingMessage = message.substring(message.indexOf("\\") + 1, message.length()).trim();

				if(incomingMessage.equals(":)") || incomingMessage.equals(":(") || incomingMessage.equals("xD") ||
						incomingMessage.equals(":'(") || incomingMessage.equals(":/")){

					Label inputEmoji = new Label(user + ":");
					inputEmoji.setStyle("-fx-font: 10pt 'Arial'; -fx-border-radius: 5.0; -fx-background-color: white; "
							+ "-fx-background-radius: 5.0;");

					switch(incomingMessage){
						case ":)":	ImageView cloneImage = new ImageView();
							cloneImage.setImage(SwingFXUtils.toFXImage(SwingFXUtils.fromFXImage(emojis[0].getImage(), null), null));
							inputEmoji.setGraphic(cloneImage);
							break;

						case ":(":	ImageView cloneImage2 = new ImageView();
							cloneImage2.setImage(SwingFXUtils.toFXImage(SwingFXUtils.fromFXImage(emojis[1].getImage(), null), null));
							inputEmoji.setGraphic(cloneImage2);
							break;

						case "xD":	ImageView cloneImage3 = new ImageView();
							cloneImage3.setImage(SwingFXUtils.toFXImage(SwingFXUtils.fromFXImage(emojis[2].getImage(), null), null));
							inputEmoji.setGraphic(cloneImage3);
							break;

						case ":'(":	ImageView cloneImage4 = new ImageView();
							cloneImage4.setImage(SwingFXUtils.toFXImage(SwingFXUtils.fromFXImage(emojis[3].getImage(), null), null));
							inputEmoji.setGraphic(cloneImage4);
							break;

						case ":/":	ImageView cloneImage5 = new ImageView();
							cloneImage5.setImage(SwingFXUtils.toFXImage(SwingFXUtils.fromFXImage(emojis[4].getImage(), null), null));
							inputEmoji.setGraphic(cloneImage5);
							break;
					}
					inputEmoji.setContentDisplay(ContentDisplay.RIGHT);

					if(!user.equals(username)){
						Label space = new Label();
						space.setMinHeight(35);
						myChat.getChildren().add(space);
						incomingChat.getChildren().add(inputEmoji);
					}

				} else{
					Label outputText = new Label(user + ": " + incomingMessage);
					outputText.setStyle("-fx-font: 10pt 'Arial'; -fx-border-radius: 5.0; -fx-background-color: white; "
							+ "-fx-background-radius: 5.0;");
					outputText.setPadding(new Insets(2,2,2,2));

					if(!user.equals(username)){
						Label space = new Label();
						space.setPrefHeight(20);
						myChat.getChildren().add(space);
						incomingChat.getChildren().add(outputText);

						try{
							String musicFile = "sounds/Note3.mp3";
							Media sound = new Media(new File(musicFile).toURI().toString());
							MediaPlayer mediaPlayer = new MediaPlayer(sound);
							mediaPlayer.play();

						} catch(Exception ex){
							System.out.println("Sound file not found");
						}
					}
				}
			});
		}

		//Stores message into file if chat box fills up
		private void storeMessage(){
			Platform.runLater(() -> {
				try{
					File file = new File("chathistory/" + username);

					//Calculate height of all contents in myChat
					double myChatHeight = 0;
					for(int i = 0; i < myChat.getChildren().size(); i++){
						Label tempLabel = (Label) myChat.getChildren().get(i);
						myChatHeight += tempLabel.getHeight();
					}

					//Calculate height of all contents in incomingChat
					double incomingChatHeight = 0;
					for(int i = 0; i < incomingChat.getChildren().size(); i++){
						Label tempLabel = (Label) incomingChat.getChildren().get(i);
						incomingChatHeight += tempLabel.getHeight();
					}

					//System.out.println(myChatHeight + " : " + incomingChatHeight + " : " + myChat.getHeight());

					if(myChatHeight > (myChat.getHeight() - 80) || incomingChatHeight > (myChat.getHeight() - 80)){
						if(privateChat == false){
							FileWriter output = new FileWriter(file, true);
							Label temp = (Label) incomingChat.getChildren().get(0);
							Label temp2 = (Label) myChat.getChildren().get(0);
							output.write("" + temp.getText() + temp2.getText() + "\n");
							incomingChat.getChildren().remove(0);
							myChat.getChildren().remove(0);
							output.close();
						} else{
							incomingChat.getChildren().remove(0);
							myChat.getChildren().remove(0);
						}
					}

					if(myChatHeight > (myChat.getHeight() - 60) || incomingChatHeight > (myChat.getHeight() - 60)){
						if(privateChat == false){
							FileWriter output = new FileWriter(file, true);
							Label temp = (Label) incomingChat.getChildren().get(0);
							Label temp2 = (Label) myChat.getChildren().get(0);
							output.write("" + temp.getText() + temp2.getText() + "\n");
							incomingChat.getChildren().remove(0);
							myChat.getChildren().remove(0);
							output.close();
						} else{
							incomingChat.getChildren().remove(0);
							myChat.getChildren().remove(0);
						}
					}

					if(myChatHeight > (myChat.getHeight() - 40) || incomingChatHeight > (myChat.getHeight() - 40)){
						if(privateChat == false){
							FileWriter output = new FileWriter(file, true);
							Label temp = (Label) incomingChat.getChildren().get(0);
							Label temp2 = (Label) myChat.getChildren().get(0);
							output.write("" + temp.getText() + temp2.getText() + "\n");
							incomingChat.getChildren().remove(0);
							myChat.getChildren().remove(0);
							output.close();
						} else{
							incomingChat.getChildren().remove(0);
							myChat.getChildren().remove(0);
						}
					}

					if(myChatHeight > (myChat.getHeight() - 20) || incomingChatHeight > (myChat.getHeight() - 20)){
						if(privateChat == false){
							FileWriter output = new FileWriter(file, true);
							Label temp = (Label) incomingChat.getChildren().get(0);
							Label temp2 = (Label) myChat.getChildren().get(0);
							output.write("" + temp.getText() + temp2.getText() + "\n");
							incomingChat.getChildren().remove(0);
							myChat.getChildren().remove(0);
							output.close();
						} else{
							incomingChat.getChildren().remove(0);
							myChat.getChildren().remove(0);
						}
					}

				} catch(Exception ex){
					System.out.println("Error outputing to file");
					ex.printStackTrace();
				}
			});
		}
	}
}