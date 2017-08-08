package assignment7;

/*
 * Project 7 : Socket Programming
 * John Lee jol322
 * Benjamin Thorell blt895
 * 
 * Slip Days Used: 2 (using one here)
 */

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ServerMain extends Application {

	public static void main(String[] args) {
		new Thread(() -> launch(args)).start();
		new Thread(() -> {
			try{		
				new ServerObservable().setUpNetworking();
			}catch(Exception e){	
				e.printStackTrace();
			}
		}).start();
	}

	static TextArea text;
	@Override
	public void start(Stage primaryStage) throws Exception {

		//Displays Welcome
		text = new TextArea("Greetings, Server!");
		text.setStyle("-fx-font: bold italic 10pt 'Castellar'; -fx-text-fill: black;");

		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);
		container.setPrefHeight(240);
		container.setPrefWidth(400);
		container.setPadding(new Insets(20, 5, 30, 5));
		VBox.setVgrow(text, Priority.ALWAYS);
		HBox.setHgrow(text, Priority.ALWAYS);

		container.getChildren().add(text);

		GridPane pane = new GridPane();
		pane.add(container, 0, 0);
		Scene serverScene = new Scene(pane);

		serverScene.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) ->
			container.setPrefHeight((double)newSceneHeight));
		
		serverScene.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) ->
			container.setPrefWidth((double)newSceneWidth));

		primaryStage.setScene(serverScene);
		primaryStage.setTitle("Server");
		primaryStage.show();

		primaryStage.setOnCloseRequest(e -> System.exit(0));
	}

	public static void print(String s){
		text.appendText("\n" + s);
	}

}
