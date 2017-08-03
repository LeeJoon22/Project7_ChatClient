package assignment7;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ClientMain extends Application{

	String message;
	Client c;

	public static void main(String[] args){
		//new Thread(() -> launch(args)).start();
		//new Thread(() -> c = new Client()).start();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		new Thread(() -> c = new Client()).start();

		temp.message = "Hello World";

		GridPane gp = new GridPane();
		TextField t = new TextField("Type something");

		Button send = new Button("Send");
		send.setOnAction((ActionEvent event) -> {
			Platform.runLater(() -> {
				temp.message = t.getText();
				System.out.println("We are setting cm to: " + temp.message);
			});
		});

		gp.add(t, 0, 0);
		gp.add(send, 1, 0);

		primaryStage.setTitle("Client");
		primaryStage.setScene(new Scene(gp, 350, 400));
		primaryStage.show();
	}
}
